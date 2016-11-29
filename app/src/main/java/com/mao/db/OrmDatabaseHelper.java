package com.mao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mao.travelapp.bean.User;

/**
 * Created by mao on 2016/11/24.
 */
public class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper {

    /** 数据库名 */
    private final static String databaseName = "app";
    /** 数据库版本 */
    private final static int databaseVersion = 1;

    private final static Class<?>[] sDBTClass =
            {User.class};

    public OrmDatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        for(Class<?> clazz : sDBTClass) {
            try {
                TableUtils.clearTable(connectionSource, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    public <T> Dao<T, Integer> getTDao(Class<T> clazz) {
        try {
            return getDao(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
