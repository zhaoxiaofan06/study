package com.shop.www.shopapplication.rest;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;
import com.shop.www.shopapplication.model.LoginReturn;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by think on 2017/10/27.
 */

public class DbClient {
    private static DbClient instance;

    public static synchronized DbClient getInstance(Context c) {
        if (instance == null)
            instance = new DbClient(c);

        return instance;
    }

    public DbHelper dbHelper;

    public DbClient(Context c) {
        this.dbHelper = DbHelper.getInstance(c);
    }

    public void clearLoginReturn() {
        try {
            TableUtils.clearTable(dbHelper.getConnectionSource(), LoginReturn.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LoginReturn lastLoginReturn(Context c) {
        try {
            List<LoginReturn> list = dbHelper.loginReturnsDao().queryForAll();
            if ((list != null) && (list.size() > 0))
                return list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new LoginReturn();
    }
}
