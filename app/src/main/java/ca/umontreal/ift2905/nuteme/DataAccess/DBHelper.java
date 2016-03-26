package ca.umontreal.ift2905.nuteme.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 25/03/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "favorites.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_FAVORITES = "favorites";
    public static final String F_ID = "_id";
    public static final String F_IMG_URL = "img_url";
    public static final String F_TITLE = "title";

    private static SQLiteDatabase db = null;


    public DBHelper(Context context){
        super(context,  DB_NAME, null, DB_VERSION);
        if (db == null){
            db = getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_FAVORITES +
                " ( "
                + F_ID + " integer primary key, "
                + F_TITLE + " text, "
                + F_IMG_URL + " text ) ";
        Log.d("SQL", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FAVORITES);
        onCreate(db);
    }

    public int deleteByID(int id){
        return db.delete(TABLE_FAVORITES, F_ID + "=" + id, null);
    }

    public int insertFavorite(Recipe recipe){
        int nb = 0;

        ContentValues cv = new ContentValues();

        cv.clear();
        cv.put(F_ID, recipe.id);
        cv.put(F_TITLE, recipe.title);
        cv.put(F_IMG_URL, recipe.image);

        try{
            db.insertOrThrow(TABLE_FAVORITES, null, cv);
            nb++;
        }catch (SQLException e){
            Log.d("SQL Exception", e.getMessage());
        }

        return nb;
    }

    public Cursor getFavorites(){
        Cursor c = db.rawQuery("select * from " + TABLE_FAVORITES + " order by " + F_TITLE + " asc ", null);
        return c;
    }
}
