package com.salim3dd.rnd_photo;
/**
 * Created by Salim3dd on 13/12/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "DBPhotos.db";

    public static final String DBLOCATION = Environment.getDataDirectory() + "/data/com.salim3dd.rnd_photo/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

   /* public int get_GetTotalRow() {
        openDatabase();
        Cursor res = mDatabase.rawQuery("select * from T_Photos", null);
        res.moveToFirst();
        int Totnum = res.getCount();
        res.close();
        closeDatabase();
        return Totnum;
    }

    public int get_Remainder() {
        openDatabase();
        Cursor res = mDatabase.rawQuery("select * from T_Photos where isShow like '0'", null);
        res.moveToFirst();
        int Rnum = res.getCount();
        res.close();
        closeDatabase();
        return Rnum;
    }*/

    public List<Integer> get_Remainder_List() {
        List<Integer> List_ID = new ArrayList<>();
        openDatabase();
        Cursor res = mDatabase.rawQuery("select id from T_Photos where isShow like '0'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            List_ID.add(res.getInt(res.getColumnIndex("id")));
            res.moveToNext();
        }
        res.close();
        closeDatabase();
        return List_ID;
    }


    public void get_Next_Search(int Rnd_id) {

        openDatabase();
        Cursor res = mDatabase.rawQuery("select * from T_Photos where id like '" + Rnd_id + "'", null);
        res.moveToFirst();

        int id = res.getInt(res.getColumnIndex("id"));
        String photo_1 = res.getString(res.getColumnIndex("Photo_1"));
        String photo_2 = res.getString(res.getColumnIndex("Photo_2"));
        String qu = res.getString(res.getColumnIndex("Qu"));
        int ANS = res.getInt(res.getColumnIndex("ANS"));

        MainActivity.mBundle.putInt("id", id);
        MainActivity.mBundle.putString("Photo_1", photo_1);
        MainActivity.mBundle.putString("Photo_2", photo_2);
        MainActivity.mBundle.putString("QU", qu);
        MainActivity.mBundle.putInt("ANS", ANS);

        res.close();
        closeDatabase();

    }

    public void update_isShow(String Rnd_id) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isShow", "1");
        mDatabase.update("T_Photos", contentValues, "id= ?", new String[]{Rnd_id});
        closeDatabase();
    }

    public void update_isShow_ReNew() {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isShow", "0");
        mDatabase.update("T_Photos", contentValues, "isShow= ?", new String[]{"1"});
        closeDatabase();
    }


    /*public Integer Delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("mytable","id = ?",new String[]{id});
    }*/
}
