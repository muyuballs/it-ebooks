package info.breezes.itebooks.app.download;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.toolbox.ImageLoader;
import info.breezes.itebooks.app.ITEBooksApp;
import info.breezes.itebooks.app.R;
import info.breezes.itebooks.app.model.Book;
import info.breezes.itebooks.utils.BytesUtils;

/**
 * Created by jianxingqiao on 14-6-15.
 */
public class DownloadingFragment extends Fragment implements AdapterView.OnItemLongClickListener {
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
            long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            long currentSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
            Book book = ITEBooksApp.current.dbHelp.query(Book.class).where("Download", url, "=").first();
            if (book != null) {
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
                ITEBooksApp.current.imageLoader.get(book.Image, imageListener);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_drawer);
            }
            holder.textView.setText("[" + cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) + "]" + cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)));
            holder.progressBar.setMax((int) totalSize);
            holder.progressBar.setProgress((int) currentSize);
            holder.textView1.setText(BytesUtils.format(currentSize) + "/" + BytesUtils.format(totalSize));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_pager, null);
        listView = (ListView) view.findViewById(R.id.listView);
        DownloadManager downloadManager = (DownloadManager) ITEBooksApp.current.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_FAILED);
        listView.setAdapter(new DownloadAdapter(getActivity(), inflater, downloadManager.query(query)));
        listView.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}