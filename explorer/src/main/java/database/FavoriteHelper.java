package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joker on 2016-10-19.
 * 收藏文件夹数据库helper
 */

public class FavoriteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoriteFolder.db";//数据库名称
    private static final int SCHEMA_VERSION = 1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    public FavoriteHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    //创建收藏文件夹的数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE favorite (_id INTEGER PRIMARY KEY AUTOINCREMENT, folderName TEXT, folderPath TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 获取全部数据
     */
    public Cursor getAll() {
        String sql = "SELECT _id,folderName,folderPath FROM favorite";

        return getReadableDatabase().rawQuery(sql, null);
    }

    public boolean isExist(String folderPath) {
        boolean isExist = false;
        Cursor cursor = getAll();
        if (cursor.moveToNext()) {
            if (folderPath.equals(cursor.getString(cursor.getColumnIndex("folderPath")))) {
                isExist = true;
            }
        }

        return isExist;
    }

    /**
     * 往数据库添加数据
     *
     * @param folderName 文件夹名
     * @param folderPath 文件夹路径
     * @return
     */
    public void insert(String folderName, String folderPath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("folderName", folderName);
        contentValues.put("folderPath", folderPath);
        getWritableDatabase().insert("favorite", null, contentValues);
    }


    public void delete(String folderPath) {
        getWritableDatabase().delete("favorite", "folderPath=?", new String[]{folderPath});
    }
}
