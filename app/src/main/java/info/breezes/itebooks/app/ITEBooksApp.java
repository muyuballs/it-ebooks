package info.breezes.itebooks.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import info.breezes.itebooks.app.cache.DiskImageCache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class ITEBooksApp extends Application {

    public static String BreezesServer = "http://www.breezes.info:10076/";

    public static final String BOOK = "book";

    public static final String FILE = "file";

    public static final String BooksDbName = "books.db";

    public static final String GlobalSharedName = "it_ebooks.shared";

    public static final int BooksDbVersion = 1;

    public static ITEBooksApp current;

    public RequestQueue requestQueue;
    public ImageLoader imageLoader;

    private DiskImageCache imageCache;

    public DbHelp dbHelp;

    private BreezesBooksApiListener apiListener;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imageCache = new DiskImageCache(getExternalFilesDir("images"));
        imageLoader = new ImageLoader(requestQueue, imageCache);
        dbHelp = new DbHelp(getApplicationContext(), BooksDbName, BooksDbVersion);
        apiListener = new BreezesBooksApiListener();
        sharedPreferences = getSharedPreferences(GlobalSharedName, MODE_PRIVATE);
        current = this;
        if (!installed()) {
            install();
        }
    }

    public String getClientId() {
        String clientId = sharedPreferences.getString("clientId", null);
        if (clientId == null) {
            clientId = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("clientId", clientId);
            editor.commit();
        }
        return clientId;
    }

    private boolean installed() {
        return sharedPreferences.getBoolean("installed", false);
    }

    private void setInstalled() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("installed", true);
        editor.commit();
    }

    public static void uploadBookInfo(JSONObject book) {
        current.requestQueue.add(new JsonObjectRequest(Request.Method.POST, ITEBooksApp.BreezesServer + "books", book, ITEBooksApp.current.apiListener, ITEBooksApp.current.apiListener));
    }

    public static void uploadSearchInfo(String keywords) {
        if (!TextUtils.isEmpty(keywords)) {
            JSONObject record = new JSONObject();
            try {
                record.put("clientId", current.getClientId());
                record.put("keywords", keywords);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            current.requestQueue.add(new JsonObjectRequest(Request.Method.PUT, ITEBooksApp.BreezesServer + "searchRecord", record, ITEBooksApp.current.apiListener, ITEBooksApp.current.apiListener));
        }
    }

    public static void install() {
        JSONObject record = new JSONObject();
        try {
            record.put("clientId", current.getClientId());
            record.put("clientName", getClientNames());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        current.requestQueue.add(new JsonObjectRequest(Request.Method.PUT, ITEBooksApp.BreezesServer + "install", record, ITEBooksApp.current.apiListener, ITEBooksApp.current.apiListener));
    }

    private static String getClientNames() {
        AccountManager accountManager = (AccountManager) current.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccounts();
        JSONArray array = new JSONArray();
        try {
            for (Account account : accounts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", account.name);
                jsonObject.put("type", account.type);
                array.put(jsonObject);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return array.toString();
    }

    class BreezesBooksApiListener implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onResponse(JSONObject response) {
            if (response.has("response")) {
                boolean b = response.optJSONObject("response").optBoolean("installation", false);
                if (b) {
                    current.setInstalled();
                }
            }
            Log.d("BreezesApi Response:", response.toString());
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("BreezesApi Error:", error.toString());
        }
    }

}
