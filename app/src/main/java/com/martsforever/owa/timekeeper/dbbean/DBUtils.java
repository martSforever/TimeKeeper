package com.martsforever.owa.timekeeper.dbbean;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by OWA on 2017/5/2.
 */

public class DBUtils {

    private static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("timekeeper.db")
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            });
    private static DbManager dbManager = x.getDb(daoConfig);

    public static DbManager getDbManager() {
        return dbManager;
    }

    public static void deleteById(Class entity, int id) {
        try {
            DBUtils.getDbManager().deleteById(entity, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
