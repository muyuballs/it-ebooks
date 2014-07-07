package info.breezes.itebooks.app.library;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import info.breezes.orm.utils.CursorUtils;

import java.io.File;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class LibraryFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {


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
            holder.progressBar.setVisibility(View.GONE);
            holder.textView2.setVisibility(View.GONE);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Holder holder = (Holder) view.getTag();
            long id=cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Cursor cursor1=ITEBooksApp.current.dbHelp.getReadableDatabase().rawQuery("select * from books where exists(select * from download_lib_map where books.isbn=download_lib_map.isbn and download_lib_map.downloadId=?)", new String[]{"" + id});
            if(cursor1.moveToFirst()){
                Book book= CursorUtils.readCurrentEntity(Book.class, cursor1);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
                ITEBooksApp.current.imageLoader.get(book.Image, imageListener);
            }else {
                holder.imageView.setImageResource(R.drawable.ic_drawer);
            }
            cursor1.close();
            holder.textView.setText(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)));
            holder.textView1.setText(BytesUtils.format(totalSize));
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
        listView= (ListView) view.findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        DownloadManager downloadManager = (DownloadManager) ITEBooksApp.current.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        adapter=new DownloadAdapter(getActivity(), inflater, downloadManager.query(query));
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor= (Cursor) adapter.getItem(position);
        String name=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
        String filePath=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
        File file=new File(filePath);
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("name",name);
            intent.setDataAndType(Uri.fromFile(file),"application/pdf");
            startActivity(intent);
        }
    }

}
