package com.ailicai.app.db;


import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Gerry on 2015/7/9.
 */
public class UserInfoDBUtil {
    RuntimeExceptionDao<UserInfoTableModel, Integer> userInfoRuntimeDao = null;
    private Context mContext;

    public UserInfoDBUtil(Context context) {
        this.mContext = context;
        DBOpenHelper mDBHelper = OpenHelperManager.getHelper(mContext, DBOpenHelper.class);
        userInfoRuntimeDao = mDBHelper.getUserInfoRuntimeDao();
    }

    /**
     * 插入一条数据
     *
     * @param userInfo
     */
    public void insert(UserInfoTableModel userInfo) {
        userInfoRuntimeDao.createOrUpdate(userInfo);
    }

    /**
     * 删除一条数据
     *
     * @param userInfo
     * @return
     */
    public int delete(UserInfoTableModel userInfo) {
        try {
            DeleteBuilder<UserInfoTableModel, Integer> deleteBuilder = userInfoRuntimeDao.deleteBuilder();
            deleteBuilder.where().eq("userId", userInfo.getUserId());
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除指定用户ID的数据
     *
     * @param userId
     * @return
     */
    public int delete(long userId) {
        try {
            DeleteBuilder<UserInfoTableModel, Integer> deleteBuilder = userInfoRuntimeDao.deleteBuilder();
            deleteBuilder.where().eq("userId", userId);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除全部
     */
    public void deleteAllUser() {
        userInfoRuntimeDao.delete(queryAllUser());
    }

    /**
     * 查询所有的
     */
    public List<UserInfoTableModel> queryAllUser() {
        List<UserInfoTableModel> usersInfo = userInfoRuntimeDao.queryForAll();
        return usersInfo;
    }

    /**
     * 查询指定用户手机号的用户
     * 用户ID 和 手机号一一对应
     */
    public UserInfoTableModel queryUserByMobile(long userId) {
        List<UserInfoTableModel> usersInfo = userInfoRuntimeDao.queryForEq("userId", userId);
        if (usersInfo != null && usersInfo.size() > 0) {
            UserInfoTableModel model = usersInfo.get(0);
            return model;
        }
        return null;
    }


}
