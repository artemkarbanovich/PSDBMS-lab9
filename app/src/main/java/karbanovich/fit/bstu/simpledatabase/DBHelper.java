package karbanovich.fit.bstu.simpledatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "Lab_DB.db";
    private static final int SCHEMA = 1;
    private static final String TABLE_NAME = "SimpleTable";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                    "create table " + TABLE_NAME + " (          "
                        + "ID integer primary key autoincrement,"
                        + "F float not null,                    "
                        + "T text not null                    );"
                    );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(SQLiteDatabase db, int id, double f, String t) {
        db.execSQL("insert or replace into " + TABLE_NAME + " (ID, F, T) values (" + id + ", " + f + ", '" + t + "');");
    }

    public void insert(SQLiteDatabase db, double f, String t) {
        db.execSQL("insert or replace into " + TABLE_NAME + " (F, T) values (" + f + ", '" + t + "');");
    }

    public Cursor select(SQLiteDatabase db, String id) {
        return db.query(TABLE_NAME,
                        new String[] {"F", "T"},
                "ID = ?",
                        new String[] {id},
                null,null,null);
    }

    public Cursor selectRaw(SQLiteDatabase db, int id) {
        return db.rawQuery("select F, T from " + TABLE_NAME + " where " + id + " = ID", null);
    }

    public int update(SQLiteDatabase db, String id, double f, String t) {
        ContentValues cv = new ContentValues();
        cv.put("F", f);
        cv.put("T", t);

        return db.update(TABLE_NAME, cv, "ID = ?", new String[] { id });
    }

    public int delete(SQLiteDatabase db, String id) {
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }
}
