package info.breezes.itebooks.app.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import info.breezes.itebooks.app.Downloader;
import info.breezes.itebooks.app.ITEBooksApp;
import info.breezes.itebooks.app.R;
import info.breezes.itebooks.app.model.Book;
import org.json.JSONObject;

public class BookDetailActivity extends Activity {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private EAN13Writer codeWriter;

    private Book book;

    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private ImageView imageView;
    private ImageView imageView1;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        codeWriter = new EAN13Writer();
        setContentView(R.layout.activity_book_detail);
        book = (Book) getIntent().getSerializableExtra(ITEBooksApp.BOOK);
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        setTitle(book.Title);
        showBookInfo();
        loadBookDetail(book.ID);
    }

    private void showBookInfo() {
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
        ITEBooksApp.current.imageLoader.get(book.Image, imageListener);
        textView.setText(book.Author);
        textView1.setText(book.Publisher);
        textView2.setText(TextUtils.isEmpty(book.SubTitle) ? book.Title : book.SubTitle);
        textView3.setText(book.Description);
        try {
            BitMatrix bitMatrix = codeWriter.encode(book.ISBN, BarcodeFormat.EAN_13, imageView1.getWidth(), imageView1.getHeight());
            imageView1.setImageBitmap(drawBitmap(bitMatrix, bitMatrix.getWidth(), bitMatrix.getHeight()));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap drawBitmap(BitMatrix bitMatrix, int width, int height) {
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private void loadBookDetail(final long bookId) {
        ITEBooksApp.current.requestQueue.add(new JsonObjectRequest("http://it-ebooks-api.info/v1/book/" + bookId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("SRF", response.toString());
                parseBook(book, response);
                ITEBooksApp.current.dbHelp.insertOrUpdate(book);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showBookInfo();
                    }
                });
            }

            private Book parseBook(Book book, JSONObject jsonObject) {
                ITEBooksApp.current.uploadBookInfo(jsonObject);
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
                        Toast.makeText(BookDetailActivity.this, "Get Book Detail Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            downloadBook(book);
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadBook(Book book) {
        Downloader.download(book);
    }
}

