package com.share.jack.greendao;

import android.util.Log;

import com.share.jack.greendao.generator.DaoSession;

import java.util.List;

/**
 *
 */
public class BaseGreenDao<T> {

    private static final String TAG = "BaseGreenDao";

    public DaoSession daoSession;

    public BaseGreenDao() {
        if (daoSession == null) {
            daoSession = GreenDaoManager.getInstance().getDaoSession();
        }
    }


///////////////////////////////////////////////数据库操作/////////////////////////////////////


    /******************************************插入******************************************/
    /**
     * 插入单个对象
     *
     * @param object
     * @return
     */
    public boolean insertObject(T object) {
        if (null == object) {
            Log.e(TAG, "error to insert object");
            return false;
        }
        try {
            //如果object指定主键与表中已经存在了，就会发生异常（android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: tb_note._id (code 1555)）而插入不进去
            daoSession.insert(object);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "insert object has exception");
        }
        return false;
    }

    /**
     * 插入多个对象，并开启新的线程
     *
     * @param objects
     * @return
     */
    public boolean insertMultiObject(final List<T> objects) {
        if (null == objects || objects.isEmpty()) {
            Log.e(TAG, "error to insert objects");
            return false;
        }
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        // 当主键存在的时候会替换，所以能够很好的执行插入操作
                        daoSession.insertOrReplace(object);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            Log.e(TAG, "insert objects has exception");
        }
        return false;
    }


    /******************************************删除******************************************/
    /**
     * 删除某个数据库表
     *
     * @param cls
     * @return
     */
    public boolean deleteAll(Class cls) {
        try {
            daoSession.deleteAll(cls);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "delete table has exception");
        }
        return false;
    }

    /**
     * 删除某个对象
     *
     * @param object
     * @return
     */
    public boolean deleteObject(T object) {
        if (object == null) {
            Log.e(TAG, "error to delete object");
            return false;
        }
        try {
            daoSession.delete(object);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "delete object has exception");
        }
        return false;
    }

    /**
     * 异步批量删除数据
     *
     * @param objects
     * @return
     */
    public boolean deleteMultiObject(final List<T> objects, Class cls) {
        if (null == objects || objects.isEmpty()) {
            Log.e(TAG, "error to delete objects");
            return false;
        }
        try {
            daoSession.getDao(cls).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.delete(object);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            Log.e(TAG, "delete objects has exception");
        }
        return false;
    }


    /******************************************更新******************************************/
    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     *
     * @param object
     * @return
     */
    public boolean updateObject(T object) {
        if (null == object) {
            Log.e(TAG, "error to update object");
            return false;
        }
        try {
            daoSession.update(object);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "update object has exception");
        }
        return false;
    }

    /**
     * 批量更新数据
     *
     * @param objects
     * @return
     */
    public boolean updateMultiObject(final List<T> objects, Class cls) {
        if (null == objects || objects.isEmpty()) {
            Log.e(TAG, "error to update objects");
            return false;
        }
        try {
            daoSession.getDao(cls).update(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.update(object);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            Log.e(TAG, "update objects has exception");
        }
        return false;
    }


    /******************************************查询******************************************/
    /**
     * 获得某个表名
     *
     * @return
     */
    public String getTableName(Class cls) {
        return daoSession.getDao(cls).getTablename();
    }

    /**
     * 根据主键ID来查询
     *
     * @param id
     * @return
     */
    public T queryById(long id, Class cls) {
        return (T) daoSession.getDao(cls).loadByRowId(id);
    }

    /**
     * 查询某条件下的对象
     *
     * @param cls
     * @return
     */
    public List<T> queryObject(Class cls, String where, String... params) {
        Object obj = null;
        List<T> objects = null;
        try {
            obj = daoSession.getDao(cls);
            if (null == obj) {
                return null;
            }
            objects = daoSession.getDao(cls).queryRaw(where, params);
        } catch (Exception e) {
            Log.e(TAG, "query object has exception");
        }
        return objects;
    }

    /**
     * 查询所有对象
     *
     * @param cls
     * @return
     */
    public List<T> queryAll(Class cls) {
        List<T> objects = null;
        try {
            objects = (List<T>) daoSession.getDao(cls).loadAll();
        } catch (Exception e) {
            Log.e(TAG, "query objects has exception");
        }
        return objects;
    }
}