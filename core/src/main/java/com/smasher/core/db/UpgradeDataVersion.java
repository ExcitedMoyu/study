package com.smasher.core.db;

import com.smasher.core.log.Logger;

/***
 * 新版本要升级一下本地的数据库
 *
 * @author moyu
 */
public class UpgradeDataVersion {
    private static final String TAG = "UpgradeDataVersion:";


    public static void checkDBVersion() {
        MainDatabase.getInstance().checkDataBase();
        int version = MainDatabase.getInstance().mDB.getVersion();
        Logger.d(TAG, "checkDBVersion: " + version);
    }
}
