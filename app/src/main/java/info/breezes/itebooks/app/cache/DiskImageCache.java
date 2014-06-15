package info.breezes.itebooks.app.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Log;
import android.util.LruCache;
import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import info.breezes.itebooks.utils.DigestUtils;

import java.io.*;
import java.util.*;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class DiskImageCache implements ImageLoader.ImageCache {


    private final File mRootDirectory;

    @Override
    public Bitmap getBitmap(String url) {
        String fileName = DigestUtils.md5(url);
        File imageFile = new File(mRootDirectory, fileName);
        if (imageFile.exists()) {
            try {
                return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String fileName = DigestUtils.md5(url);
        File imageFile = new File(mRootDirectory, fileName);
        if (!imageFile.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception exp) {
                exp.printStackTrace();
                try {
                    imageFile.delete();
                } catch (Exception exp2) {
                    exp2.printStackTrace();
                }
            }
        }
    }


    public DiskImageCache(File rootDirectory) {
        mRootDirectory = rootDirectory;
        initialize();
    }


    public synchronized void clear() {
        File[] files = mRootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        VolleyLog.d("Cache cleared.");
    }


    public synchronized void initialize() {
        if (!mRootDirectory.exists()) {
            if (!mRootDirectory.mkdirs()) {
                Log.e("Unable to create cache dir %s", mRootDirectory.getAbsolutePath());
            }
            return;
        }
    }

}

