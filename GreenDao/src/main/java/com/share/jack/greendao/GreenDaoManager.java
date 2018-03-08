package com.share.jack.greendao;

import com.share.jack.greendao.generator.DaoMaster;
import com.share.jack.greendao.generator.DaoSession;

import cn.share.jack.cyghttp.app.CygApplication;

/**
 *
 */
public class GreenDaoManager {

    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private static final String DB_NAME = "testCyg.db";

    private GreenDaoManager() {
        if (devOpenHelper == null || daoMaster == null || daoSession == null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(CygApplication.getInstance(), DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            daoSession = daoMaster.newSession();
        }
    }

    private static class Singleton {
        private static GreenDaoManager mInstance = new GreenDaoManager();
    }

    public static GreenDaoManager getInstance() {
        return Singleton.mInstance;
    }

    public DaoMaster.DevOpenHelper getDevOpenHelper() {
        return devOpenHelper;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
