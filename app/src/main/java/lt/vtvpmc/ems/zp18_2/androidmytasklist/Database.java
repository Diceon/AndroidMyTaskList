package lt.vtvpmc.ems.zp18_2.androidmytasklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String db_name = "todoAppDB";
    private static final int db_ver = 1;
    private static final String db_table = "tasks";
    private static final String db_column = "taskName";
    // ID | TASKNAME


    public Database(Context context) {
        super(context, db_name, null, db_ver);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL);", db_table, db_column);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", db_name);
        db.execSQL(query);
        onCreate(db);
    }

    public void deleteData(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(db_table, db_column + " = ?", new String[]{task});
        db.close();
    }

    public void insertData(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column, task);
        db.insertWithOnConflict(db_table, null,
                values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<String> getAlltasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(db_table, new String[]{db_column}, null, null, null
                , null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(db_column);
            allTasks.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

}
