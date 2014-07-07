package info.breezes.itebooks.app;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import info.breezes.itebooks.app.model.Book;
import info.breezes.itebooks.app.model.DownloadLibMap;


/**
 * Created by jianxingqiao on 14-6-14.
 */
public class Downloader {
    static DownloadManager downloadManager = (DownloadManager) ITEBooksApp.current.getSystemService(Context.DOWNLOAD_SERVICE);

    public static long download(Book book) {
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = downloadManager.query(query);
        int urlIndex = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
        boolean found = false;
        while (cursor.moveToNext()) {
            String url = cursor.getString(urlIndex);
            if (url.equals(book.Download)) {
                found = true;
                break;
            }
        }
        cursor.close();
        if (found) {
            return -1;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(book.Download));
        request.setDestinationInExternalFilesDir(ITEBooksApp.current.getApplicationContext(), "books", "download");
        request.addRequestHeader("Referer", "http://it-ebooks.info/book/" + book.ID + "/");
        request.setTitle(book.Title);
        long id= downloadManager.enqueue(request);
        DownloadLibMap dlm=new DownloadLibMap();
        dlm.ISBN=book.ISBN;
        dlm.downloadId=id;
        ITEBooksApp.current.dbHelp.insert(dlm);
        return id;
    }
}
