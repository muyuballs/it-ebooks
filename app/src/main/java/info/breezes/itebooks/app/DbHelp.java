package info.breezes.itebooks.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import info.breezes.itebooks.app.model.Book;
import info.breezes.itebooks.app.model.DownloadLibMap;
import info.breezes.itebooks.app.model.Library;
import info.breezes.orm.OrmSQLiteHelper;
import info.breezes.orm.utils.TableUtils;

/**
 * Created by jianxingqiao on 14-6-15.
 */
public class DbHelp extends OrmSQLiteHelper {

    public DbHelp(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableUtils.createTable(db, Book.class);
        TableUtils.createTable(db, Library.class);
        TableUtils.createTable(db, DownloadLibMap.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
