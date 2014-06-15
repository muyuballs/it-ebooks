package info.breezes.itebooks.app.main;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import info.breezes.itebooks.app.ITEBooksApp;
import info.breezes.itebooks.app.R;

import info.breezes.itebooks.app.activity.BookDetailActivity;
import info.breezes.itebooks.app.model.Book;
import info.breezes.itebooks.utils.URLUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

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

        public void appendData(ArrayList<Book> books) {
            this.books.addAll(books);
            notifyDataSetChanged();
        }

        class Holder {
            ImageView imageView;
            TextView textView;
            TextView textView1;
        }
    }

    private ListView listView;
    private SearchResultAdapter searchResultAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String queryText;
    private Handler handler;
    private int totalCount;
    private int currentPage = 1;

    public SearchResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = (ListView) view.findViewById(R.id.listView);
        searchResultAdapter = new SearchResultAdapter(getActivity(), getActivity().getLayoutInflater());
        listView.setAdapter(searchResultAdapter);
        listView.setOnItemClickListener(this);
        searchBooks(queryText);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        queryText = getArguments().getString(SearchManager.QUERY);
    }

    private void searchBooks(String queryText) {
        ITEBooksApp.current.requestQueue.add(new JsonObjectRequest("http://it-ebooks-api.info/v1/search/" + URLUtils.encode(queryText, "UTF-8") + "/page/" + (currentPage + 1), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("SRF", response.toString());
                totalCount = response.optInt("Total");
                currentPage = response.optInt("Page");
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
                        if (currentPage == 1) {
                            searchResultAdapter.setData(books);
                        } else {
                            searchResultAdapter.appendData(books);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Search Books Error", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }));
    }

    @Override
    public void onRefresh() {
        searchBooks(queryText);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = searchResultAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra(ITEBooksApp.BOOK, book);
        startActivity(intent);
    }


}
