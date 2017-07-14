package com.ailicai.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SearchDB extends BaseTable {

    /**
     * 历史记录数
     */
    public static final int RECORD_COUNT = 10;
    public static final String TABLE_NAME = "tb_search";
    public static final String SEARCH_ID = "search_id";
    public static final String SEARCH_CONTENT = "search_content";
    public static final String SEARCH_DATE = "search_date";
    private static final long serialVersionUID = -8888174717749630072L;
    private String mSearchId;
    private String mSearchContent;
    //搜索这条记录的时间（预留字段，可扩展功能）
    private String mSearchDate;

    public SearchDB() {
        super(TABLE_NAME);
    }

    public SearchDB(String table) {
        super(TABLE_NAME);
    }


    public SearchDB(String table, String mSearchContent, String mSearchDate) {
        super(table);
        this.mSearchContent = mSearchContent;
        this.mSearchDate = mSearchDate;
    }

    public static SearchDB deleteByPrimaryKey(Context ctx, String search_id) {
        DatabaseUtil dbHelper = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        SearchDB cd = null;
        try {
            dbHelper = DatabaseUtil.get(ctx);
            db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, SEARCH_ID + "=?",
                    new String[]{search_id});
        } catch (Exception e) {
            e.printStackTrace();
            cd = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
                dbHelper = null;
            }
        }
        return cd;
    }

    public static boolean clearAll(Context ctx) {
        boolean retId = true;
        DatabaseUtil dbHelper = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            dbHelper = DatabaseUtil.get(ctx);
            db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
            retId = true;
        } catch (Exception e) {
            e.printStackTrace();
            retId = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
                dbHelper = null;
            }
        }
        return retId;
    }

    public static void getAll(Context ctx, ArrayList<SearchDB> list) {
        DatabaseUtil dbHelper = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        SearchDB cd = null;
        try {
            dbHelper = DatabaseUtil.get(ctx);
            db = dbHelper.getWritableDatabase();

            cursor = db.query(TABLE_NAME, new String[]{
                            SEARCH_CONTENT, SEARCH_DATE, SEARCH_ID},
                    null,
                    null, null,
                    null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    cd = new SearchDB();
                    cd.setSearch_content(cursor.getString(0));
                    cd.setSearch_date(cursor.getString(1));
                    cd.setSearch_id(cursor.getString(2));
                    list.add(0, cd);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            cd = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
                dbHelper = null;
            }
        }
    }

    public int save(Context ctx, int count, String deleteId) {
        int retId = SAVE_STATE_INSERT;
        DatabaseUtil dbHelper = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            dbHelper = DatabaseUtil.get(ctx);
            db = dbHelper.getWritableDatabase();
            if (count >= RECORD_COUNT) {
                dbHelper.del(this, new String[]{mSearchId});
            }
            cursor = db.query(TABLE_NAME, new String[]{SEARCH_CONTENT}, SEARCH_CONTENT
                    + "=?", new String[]{mSearchContent}, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                // 数据库存在就删除
                dbHelper.otherdel(this, new String[]{mSearchContent});
            }
            // 保存
            ContentValues values = new ContentValues();
            values.put(SEARCH_CONTENT, mSearchContent);
            values.put(SEARCH_DATE, mSearchDate);
            dbHelper.insert(TABLE_NAME, values);
            retId = SAVE_STATE_INSERT;
            return retId;
        } catch (Exception e) {
            e.printStackTrace();
            retId = SAVE_STATE_EXCEPT;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
                dbHelper = null;
            }
        }
        return retId;
    }

    public String getSearch_id() {
        return mSearchId;
    }

    public void setSearch_id(String mSearchId) {
        this.mSearchId = mSearchId;
    }

    public String getSearch_content() {
        return mSearchContent;
    }

    public void setSearch_content(String mSearchContent) {
        this.mSearchContent = mSearchContent;
    }

    public String getSearch_date() {
        return mSearchDate;
    }

    public void setSearch_date(String mSearchDate) {
        this.mSearchDate = mSearchDate;
    }


}
