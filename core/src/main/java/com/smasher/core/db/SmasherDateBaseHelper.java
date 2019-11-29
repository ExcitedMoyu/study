package com.smasher.core.db;

import android.util.Log;

import androidx.room.Room;

import com.smasher.core.other.ApplicationContext;

/**
 * @author Smasher
 * on 2019/11/20 0020
 */
public class SmasherDateBaseHelper {
    public static final String TAG = "SmasherDateBaseHelper";

    private SmasherDateBase mDateBase;

    private static final SmasherDateBaseHelper ourInstance = new SmasherDateBaseHelper();

    public static SmasherDateBaseHelper getInstance() {
        return ourInstance;
    }

    private SmasherDateBaseHelper() {
        mDateBase = Room.databaseBuilder(ApplicationContext.getInstance(),
                SmasherDateBase.class, "beizhi_datebase")
                //添加数据库变动迁移
                //.addMigrations(SmasherDateBase.MIGRATION_1_2)

                //下面注释表示允许主线程进行数据库操作，但是不推荐这样做。
                //他可能造成主线程lock以及anr
                //所以我们的操作都是在新线程完成的
                .allowMainThreadQueries()
                .build();
    }


    public void init() {
        Log.d(TAG, "init: ");
    }


    public void update() {

    }


    public SmasherDateBase getDateBase() {
        return mDateBase;
    }
}
