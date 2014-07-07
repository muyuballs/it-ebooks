package info.breezes.itebooks.app.download;

import android.app.*;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.ImageLoader;
import info.breezes.itebooks.app.ITEBooksApp;
import info.breezes.itebooks.app.R;
import info.breezes.itebooks.app.model.Book;
import info.breezes.itebooks.utils.BytesUtils;
import info.breezes.orm.utils.CursorUtils;

import java.io.File;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class DownloadManagerFragment extends Fragment implements AdapterView.OnItemLongClickListener {


    class DownloadAdapter extends CursorAdapter {

        private LayoutInflater inflater;

        public DownloadAdapter(Context context, LayoutInflater inflater, Cursor c) {
            super(context, c, true);
            this.inflater = inflater;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = inflater.inflate(R.layout.fragment_download_item, null);
            Holder holder = new Holder();
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.textView = (TextView) view.findViewById(R.id.textView);
            holder.textView1 = (TextView) view.findViewById(R.id.textView1);
            holder.textView2 = (TextView) view.findViewById(R.id.textView2);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Holder holder = (Holder) view.getTag();
            long id=cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            long currentSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            Cursor cursor1=ITEBooksApp.current.dbHelp.getReadableDatabase().rawQuery("select * from books where exists(select * from download_lib_map where books.isbn=download_lib_map.isbn and download_lib_map.downloadId=?)",new String[]{""+id});
            if(cursor1.moveToFirst()){
                Book book= CursorUtils.readCurrentEntity(Book.class,cursor1);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
                ITEBooksApp.current.imageLoader.get(book.Image, imageListener);
            }else {
                holder.imageView.setImageResource(R.drawable.ic_drawer);
            }
            cursor1.close();
            holder.textView.setText(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)));
            holder.progressBar.setMax((int) totalSize);
            holder.progressBar.setProgress((int) currentSize);
            holder.textView1.setText(BytesUtils.format(currentSize) + "/" + BytesUtils.format(totalSize));
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.textView2.setVisibility(View.VISIBLE);
            holder.textView2.setText(String.format("%.0f", (currentSize * 100.0 / totalSize)) + "%");

        }

        class Holder {
            ImageView imageView;
            TextView textView;
            TextView textView1;
            TextView textView2;
            ProgressBar progressBar;
        }
    }

    private ListView listView;
    private DownloadAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, null);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);
        DownloadManager downloadManager = (DownloadManager) ITEBooksApp.current.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(0x17);//!SUCCESS
        adapter = new DownloadAdapter(getActivity(), inflater, downloadManager.query(query));
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        getActivity().getActionBar().removeAllTabs();
        super.onDetach();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


}
