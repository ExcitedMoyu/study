package com.smasher.core.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smasher.core.dao.UserDao;
import com.smasher.core.dao.WarningDao;
import com.smasher.core.entity.User;
import com.smasher.core.entity.Warning;

/**
 * 注解指定了database的表映射实体数据以及版本等信息
 *
 * @author Smasher
 * on 2019/11/20 0020
 */
@Database(entities = {Warning.class, User.class}, version = 1, exportSchema = false)
public abstract class SmasherDateBase extends RoomDatabase {


    /**
     * RoomDatabase提供直接访问底层数据库实现，我们通过定义抽象方法返回具体Dao
     * 然后进行数据库增删该查的实现。
     *
     * @return EquipWarning
     */
    public abstract WarningDao WarningDao();

    /**
     * RoomDatabase提供直接访问底层数据库实现，我们通过定义抽象方法返回具体Dao
     * * 然后进行数据库增删该查的实现。
     *
     * @return UserDao
     */
    public abstract UserDao userDao();


    /**
     * 数据库变动添加Migration
     */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE IF NOT EXISTS user (`roomId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `address` TEXT, `name` TEXT, `telephone` TEXT, `email` TEXT)");
        }
    };

}
