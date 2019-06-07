package com.smasher.oa.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.smasher.oa.core.config.Path;
import com.smasher.oa.core.io.FileUtil;
import com.smasher.oa.core.log.Logger;
import com.smasher.oa.core.other.ApplicationContext;

import java.io.File;

/**
 * MainDatabase
 *
 * @author matao
 */

public class MainDatabase extends BaseDatabase {
    private static final String TAG = "MainDatabase:";


    private static final String DATABASE_TAG = "chq";
    private static MainDatabase mInstance;

    public static synchronized MainDatabase getInstance() {
        if (mInstance == null || mInstance.mDB == null || !mInstance.mDB.isOpen()) {
            mInstance = new MainDatabase();
        }
        return mInstance;
    }

    private static final int DATA_BASE_VERSION = 0;


    private MainDatabase() {
        if (this.mDB == null || !this.mDB.isOpen()) {
            Context app = ApplicationContext.getInstance();
            try {
                File sdcardFile = new File(Path.getSDCardDBFilePath());
                File internalFile = app.getDatabasePath(DATABASE_TAG);

                String parent = internalFile.getParent();

                File parentDir = new File(parent);
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                if (!internalFile.exists() && sdcardFile.exists()) {
                    //如果内部存储文件不在，sd卡上的文件在的话，把sd卡的文件copy过来
                    FileUtil.copyFile(sdcardFile, internalFile, true);
                }

                boolean res = openDataBase(internalFile);
            } catch (Exception e) {
                Logger.exception(e);
            } finally {
                Logger.d(TAG, "finally");
            }
        }
    }

    private SQLiteDatabase openDatabase(String path) {
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    protected void upgradeDB() {
        if (mDB == null) {
            return;
        }

        int oldVersion = mDB.getVersion();
//        if (oldVersion <= 110) {
//            try {
//                mDB.execSQL("ALTER TABLE MessageRecord ADD COLUMN ActionTitle text");
//                mDB.execSQL("ALTER TABLE MessageRecord ADD COLUMN PushImage text");
//            } catch (Exception e) {
//                Log.exception(e);
//            }
//        }

        if (oldVersion != DATA_BASE_VERSION) {
            try {
                this.mDB.setVersion(DATA_BASE_VERSION);
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
            mDB.execSQL(CREATE_USER_TABLE);
            mDB.execSQL(CREATE_EQUIP_TABLE);
            mDB.execSQL(CREATE_MATERIAL_TABLE);
            mDB.execSQL(CREATE_ADDRESS_TABLE);
            mDB.execSQL(CREATE_ORDER_TABLE);
            mDB.execSQL(CREATE_PROJECT_TABLE);
            mDB.setVersion(DATA_BASE_VERSION);
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
        File sdcardFile = new File(Path.getRootPath() + Path.NAME);
        File internalFile = ApplicationContext.getInstance().getDatabasePath(Path.NAME);
        FileUtil.copyFile(internalFile, sdcardFile, true);
    }


    private static final String CREATE_USER_TABLE = "create table if not exists UserTable" + "(Id integer primary key autoincrement," +
            "user_id text," +
            "name text," +
            "login_code text," +
            "email text," +
            "mobile text," +
            "phone text," +
            "user_type text," +
            "ref_code text," +
            "ref_name text," +
            "mgr_type text," +
            "pwd_security_level text," +
            "pwd_updated_at text," +

            "created_at text," +
            "created_by text," +
            "updated_at text," +
            "updated_by text," +
            "deleted_at text," +
            "deleted_by text" + ")";

    private static final String CREATE_EQUIP_TABLE = "create table if not exists EquipTable" + "(Id integer primary key autoincrement," +
            "user_id text," +
            "name text," +
            "equipment_type_id integer," +
            "price integer," +
            "fee integer," +
            "expired_at text," +
            "img text," +

            "unit text," +
            "count integer," +
            "created_at text," +
            "created_by text," +
            "updated_at text," +
            "updated_by text," +
            "deleted_at text," +
            "deleted_by text" + ")";

    private static final String CREATE_MATERIAL_TABLE = "create table if not exists EquipTable" + "(Id integer primary key autoincrement," +
            "user_id text," +
            "name text," +
            "material_model_code integer," +
            "material_type_code integer," +
            "area_code text," +
            "area_codes integer," +
            "company_code integer," +
            "company_codes text," +
            "office_code text," +
            "office_codes text," +
            "status text," +

            "unit text," +
            "count integer," +
            "created_at text," +
            "created_by text," +
            "updated_at text," +
            "updated_by text," +
            "deleted_at text," +
            "deleted_by text" + ")";

    private static final String CREATE_ADDRESS_TABLE = "create table if not exists AddressTable" + "(Id integer primary key autoincrement," +
            "user_id text," +
            "address_id text," +
            "name text," +
            "tel text," +
            "tel_backup text," +
            "region text," +
            "address text," +
            "created_at text," +
            "updated_at text," +
            "post_code text," +
            "gender text," +
            "region_code text," +
            "default_addr integer," +
            "deleted_at text," +
            "longitude text," +
            "latitude text," +
            "tags text" + ")";


    private static final String CREATE_ORDER_TABLE = "create table if not exists OrderTable" + "(Id integer primary key autoincrement," +
            "user_id text," +
            "title text," +
            "status text," +
            "deliver_time text," +
            "receive_time text," +
            "logistic text," +
            "address text," +
            "address_detail text," +

            "equipment_trans_order_code text," +
            "order_type integer," +

            "created_at text," +
            "created_by text," +
            "updated_at text," +
            "updated_by text," +
            "deleted_at text," +
            "deleted_by text" + ")";


    private static final String CREATE_PROJECT_TABLE = "create table if not exists ProjectTable" + "(Id integer primary key autoincrement," +
            "user_id text," +
            "name text," +
            "status text," +
            "address text," +
            "region text," +
            "area_code text," +
            "end_time text," +
            "property text," +

            "created_at text," +
            "created_by text," +
            "updated_at text," +
            "updated_by text," +
            "deleted_at text," +
            "deleted_by text" + ")";

}
