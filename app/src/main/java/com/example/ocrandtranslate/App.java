package com.example.ocrandtranslate;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.speedystone.greendaodemo.db.DaoMaster;
import com.speedystone.greendaodemo.db.DaoSession;

public class App extends Application {

    public static Context context;

    private DaoSession daoSession;

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Image.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDapSession() {
        return daoSession;
    }


}
