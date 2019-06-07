package com.xcm.oa.core.db;

import android.database.sqlite.SQLiteDatabase;

import com.xcm.oa.core.config.Path;
import com.xcm.oa.core.io.FileUtil;
import com.xcm.oa.core.other.ApplicationContext;

import java.io.File;

/**
 * @author moyu
 */
public class ConfigDatabase extends BaseDatabase {
    static ConfigDatabase mInstance;

    public static synchronized ConfigDatabase getInstance() {
        if (mInstance == null || mInstance.mDB == null || !mInstance.mDB.isOpen()) {
            mInstance = new ConfigDatabase();
        }
        return mInstance;
    }

    private static final int DataBaseVersion = 0;
    private static final String CREATE_SETTING = "create table if not exists setting (Key text primary key ,Value text);";
    private static final String CREATE_IMEI_TABLE = "create table if not exists ImeiTable" + "(Imei text primary key)";

    private ConfigDatabase() {
        if (this.mDB == null || !this.mDB.isOpen()) {
            try {
                File sdcardFile = new File(Path.getSDCardConfigFilePath());
                File internalFile = ApplicationContext.getInstance().getDatabasePath("Config");

                String parent = internalFile.getParent();
                File parentDir = new File(parent);
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                if (!internalFile.exists() && sdcardFile.exists()) {
                    //如果内部存储文件不在，sd卡上的文件在的话，把sd卡的文件copy过来
                    FileUtil.copyFile(sdcardFile, internalFile, true);
                }
                openDataBase(internalFile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    @Override
    protected void upgradeDB() {
        if (mDB == null) {
            return;
        }

        int oldVersion = mDB.getVersion();
        if (oldVersion != DataBaseVersion) {
            try {
                this.mDB.setVersion(DataBaseVersion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void checkDataBase() {
        createTables();
    }

    @Override
    protected void createTables() {
        if (mDB == null) {
            return;
        }
        try {
            mDB.beginTransaction();
            mDB.execSQL(CREATE_SETTING);
            mDB.execSQL(CREATE_IMEI_TABLE);
            mDB.setVersion(DataBaseVersion);
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDB.endTransaction();
        }
    }

    @Override
    public void closeDB() {
        super.closeDB();
        File sdcardFile = new File(Path.getRootPath() + "Config");
        File internalFile = ApplicationContext.getInstance().getDatabasePath("Config");
        FileUtil.copyFile(internalFile, sdcardFile, true);
    }

    private SQLiteDatabase openDatabase(String path) {
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
    }
}
