package info.breezes.itebooks.app.search;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import info.breezes.annotation.LayoutView;
import info.breezes.annotation.LayoutViewHelper;
import info.breezes.itebooks.app.IFragmentHost;
import info.breezes.itebooks.app.ITEBooksApp;
import info.breezes.itebooks.app.R;
import info.breezes.itebooks.app.activity.BookDetailActivity;
import info.breezes.itebooks.app.model.Book;
import info.breezes.itebooks.utils.URLUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    class SearchResultAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private ArrayList<Book> books;

        public SearchResultAdapter(Context context, LayoutInflater inflater) {
            this.context = context;
            this.layoutInflater = inflater;
            books = new ArrayList<Book>();
        }

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Book getItem(int position) {
            return books.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).ID;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = layoutInflater.inflate(R.layout.book_item, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            Book item = getItem(position);
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
            ITEBooksApp.current.imageLoader.get(item.Image, imageListener);
            holder.textView.setText(item.Title);
            holder.textView1.setText(item.Description);
            return convertView;
        }

        public void setData(ArrayList<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }

        public int appendData(ArrayList<Book> books) {
            int i = 0;
            for (Book book : books) {
                if (!this.books.contains(book)) {
                    i++;
                    this.books.add(book);
                }
            }
            notifyDataSetChanged();
            return i;
        }

        class Holder {
            ImageView imageView;
            TextView textView;
            TextView textView1;
        }
    }

    @LayoutView(R.id.listView)
    private ListView listView;
    private View footer;
    @LayoutView(R.id.search_progress_layout)
    private LinearLayout progressLayout;

    private SearchResultAdapter searchResultAdapter;
    private Context mContext;
    private String queryText;
    private Handler handler;
    private int totalCount = -1;
    private int currentPage = 0;
    private boolean searching = false;
    private int newBookCount;

    private IFragmentHost fragmentHost;

    public SearchResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, null);
        mContext = getActivity();
        LayoutViewHelper.InitLayout(view, this);
        footer = LayoutInflater.from(mContext).inflate(R.layout.listview_footer, null);
        listView.addFooterView(footer, null, false);
        listView.setFooterDividersEnabled(false);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        searchResultAdapter = new SearchResultAdapter(mContext, inflater);
        listView.setAdapter(searchResultAdapter);
        searchBooks(queryText);
        ITEBooksApp.current.uploadSearchInfo(queryText);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        queryText = getArguments().getString(SearchManager.QUERY);
        if (fragmentHost != null) {
            fragmentHost.setTitle(queryText);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        fragmentHost = (IFragmentHost) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        fragmentHost = null;
        super.onDetach();
    }

    private void searchBooks(String queryText) {
        if (searching) {
            return;
        }
        if (currentPage*10 >= totalCount && totalCount != -1) {
            return;
        }
        searching = true;
        currentPage++;
        String url = "http://it-ebooks-api.info/v1/search/" + URLUtils.encode(queryText, "UTF-8") + "/page/" + ((currentPage-1)*10);
        Log.d("SRU:", url);
        ITEBooksApp.current.requestQueue.add(new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("SRF", response.toString());
                totalCount = response.optInt("Total");
                JSONArray bookArray = response.optJSONArray("Books");
                final ArrayList<Book> books = new ArrayList<Book>();
                if (totalCount > 0) {
                    for (int i = 0; i < bookArray.length(); i++) {
                        JSONObject jsonObject = bookArray.optJSONObject(i);
                        Book book = parseBook(jsonObject);
                        books.add(book);
                        ITEBooksApp.current.dbHelp.insertOrUpdate(book);
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (totalCount > 0) {
                            newBookCount = searchResultAdapter.appendData(books);
                        } else {
                            Toast.makeText(mContext, "Oh,no book found.", Toast.LENGTH_SHORT).show();
                        }
                        if (newBookCount < 1) {
                            listView.removeFooterView(footer);
                        }
                    }
                });
                searching = false;
            }

            private Book parseBook(JSONObject jsonObject) {
                Book book = new Book();
                book.ID = jsonObject.optLong("ID");
                book.Author = jsonObject.optString("Author");
                book.Description = jsonObject.optString("Description");
                book.Download = jsonObject.optString("Download");
                book.Image = jsonObject.optString("Image");
                if (jsonObject.has("isbn")) {
                    book.ISBN = jsonObject.optString("isbn");
                } else {
                    book.ISBN = jsonObject.optString("ISBN");
                }
                book.Page = jsonObject.optString("Page");
                book.Publisher = jsonObject.optString("Publisher");
                book.SubTitle = jsonObject.optString("SubTitle");
                book.Title = jsonObject.optString("Title");
                book.Year = jsonObject.optString("Year");
                return book;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searching = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.removeFooterView(footer);
                        Toast.makeText(mContext, "Search Books Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
    }

    private boolean hasMore() {
        return (currentPage * 10 < totalCount && totalCount != -1) && newBookCount > 0;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = searchResultAdapter.getItem(position);
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra(ITEBooksApp.BOOK, book);
        startActivity(intent);
    }

    private int scrollState;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollState != SCROLL_STATE_IDLE) {
            if (firstVisibleItem + visibleItemCount == totalItemCount && !searching && hasMore()) {
                if (!TextUtils.isEmpty(queryText) && !searching) {
                    searchBooks(queryText);
                }
            }
        }
    }


}
