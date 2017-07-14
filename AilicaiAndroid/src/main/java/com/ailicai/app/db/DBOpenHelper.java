package com.ailicai.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {
    // 数据库名称
    public static final String DATABASE_NAME = "MyCollectHouseInfo2.db";
    // 数据库[APP]-[DB]
    // 1.8-3 2.0-4 2.2-5 2.3-6 2.4-7
    // 2.5-8,3.0-9,3.2.1-10,3.3-11,
    // 4.0-12,4.1-13,4.2-14,4.3-15,
    // 5.0-16,5.1-17,5.2-18,5.5-19
    // 5.7-20,5.8-22,6.0-24,6.2-25
    // 6.9-26
    public static final int DATABASE_VERSION = 26;

    private static final String TAG = "DBOpenHelper";

    private Dao<UserInfoTableModel, Integer> userInfoDao = null;
    private RuntimeExceptionDao<UserInfoTableModel, Integer> userInfoRuntimeDao = null;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, UserInfoTableModel.class);

            userInfoDao = getUserInfoDao();
            userInfoRuntimeDao = getUserInfoRuntimeDao();

        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
//        try {
            //增加新字段,直接删除旧数据,防止字段为空异常
            //TableUtils.dropTable(connectionSource, CityDBItem.class, true);

            /*
            if (oldVer < 12) {
                userInfoDao = getUserInfoDao();
                String sqlHead = "ALTER TABLE `userinfo` ADD COLUMN";
                userInfoDao.executeRaw(sqlHead + " isOpenAccount INTEGER");
                userInfoDao.executeRaw(sqlHead + " isSetPayPwd INTEGER");
                userInfoDao.executeRaw(sqlHead + " isRealNameVerify INTEGER");
                userInfoDao.executeRaw(sqlHead + " isBinDebitCard INTEGER");
                userInfoDao.executeRaw(sqlHead + " isAilicaiAllowUser INTEGER");
                userInfoDao.executeRaw(sqlHead + " key1 TEXT");
                userInfoDao.executeRaw(sqlHead + " key2 TEXT");
            }
            */

            onCreate(sqliteDatabase, connectionSource);

//        } catch (SQLException e) {
//            Log.e(DBOpenHelper.class.getName(), "Unable to upgrade database from version " +
//                    oldVer + " to new " + newVer, e);
//        }
    }

    private Dao<UserInfoTableModel, Integer> getUserInfoDao() throws SQLException {
        if (userInfoDao == null)
            userInfoDao = getDao(UserInfoTableModel.class);
        return userInfoDao;
    }

    public RuntimeExceptionDao<UserInfoTableModel, Integer> getUserInfoRuntimeDao() {
        if (userInfoRuntimeDao == null) {
            userInfoRuntimeDao = getRuntimeExceptionDao(UserInfoTableModel.class);
        }
        return userInfoRuntimeDao;
    }

    /**
     * 释放 DAO
     */
    @Override
    public void close() {
        super.close();
        userInfoRuntimeDao = null;
    }

}
