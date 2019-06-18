package com.example.ocrandtranslate.util;

import com.example.ocrandtranslate.App;
import com.example.ocrandtranslate.ocr.HistoryResponse;
import com.speedystone.greendaodemo.db.DaoSession;
import com.speedystone.greendaodemo.db.HistoryResponseDao;

import java.util.List;

public class SqlUtil {

    public static HistoryResponseDao historyResponseDao;

    public static void setDao() {
        App app = (App) App.getInstance().getApplicationContext();
        DaoSession daoSession = app.getDapSession();
        historyResponseDao = daoSession.getHistoryResponseDao();
    }


    public static void insert(HistoryResponse historyResponse) {
        if (historyResponseDao == null) {
            setDao();
        }
        historyResponseDao.insert(historyResponse);
    }

    public static void delete(HistoryResponse historyResponse) {
        if (historyResponseDao == null) {
            setDao();
        }
        historyResponseDao.delete(historyResponse);
    }

    public static void update(HistoryResponse historyResponse) {
        if (historyResponseDao == null) {
            setDao();
        }
        historyResponseDao.update(historyResponse);
    }

    public static List<HistoryResponse> queryAll() {
        if (historyResponseDao == null) {
            setDao();
        }
        return historyResponseDao.loadAll();
    }
}
