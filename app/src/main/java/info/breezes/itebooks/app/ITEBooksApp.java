package info.breezes.itebooks.app;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import info.breezes.itebooks.app.cache.DiskImageCache;
import info.breezes.itebooks.app.download.DownloadReceiver;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class ITEBooksApp extends Application {

    public static final String BOOK = "book";

    public static final String FILE ="file" ;

    public static final String BooksDbName="books.db";

    public static final int BooksDbVersion=1;

    public static ITEBooksApp current;

    public RequestQueue requestQueue;
    public ImageLoader imageLoader;
    private DiskImageCache imageCache;

    public DbHelp dbHelp;


    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imageCache = new DiskImageCache(getExternalFilesDir("images"));
        imageLoader = new ImageLoader(requestQueue, imageCache);
        dbHelp=new DbHelp(getApplicationContext(),BooksDbName,BooksDbVersion);
        current = this;
    }


}
