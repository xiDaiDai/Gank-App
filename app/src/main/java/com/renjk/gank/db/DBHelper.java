package com.renjk.gank.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.renjk.gank.bean.GankItem;

import java.sql.SQLException;

/**
 * Created by admin on 2016/6/12.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private static final String DB_NAME = "orm";
    private static DBHelper mDbHelper;
    private Dao<GankItem,Integer> mGankItemDao;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context){
        if (mDbHelper == null) {
            mDbHelper = new DBHelper(context);
        }
        return mDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, GankItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    public Dao<GankItem, Integer> getGankItemDao() throws SQLException {
        if (mGankItemDao == null) {
            mGankItemDao = getDao(GankItem.class);
        }
        return mGankItemDao;
    }
}
