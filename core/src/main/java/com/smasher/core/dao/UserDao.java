package com.smasher.core.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smasher.core.entity.User;

import java.util.List;


/**
 * @author Smasher
 * on 2019/11/20 0020
 */
@Dao
public interface UserDao {


    //region insert

    /**
     * OnConflictStrategy.REPLACE表示如果已经有数据，那么就覆盖掉
     * 数据的判断通过主键进行匹配，也就是uid，非整个user对象
     * 返回Long数据表示，插入条目的主键值（uid）
     *
     * @param user user
     * @return count
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(User user);

    /**
     * 返回List<Long>数据表示被插入数据的主键uid列表
     *
     * @param users users
     * @return list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(User... users);

    /**
     * 返回List<Long>数据表示被插入数据的主键uid列表
     *
     * @param users users
     * @return list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<User> users);
    //endregion


    //---------------------update------------------------

    //region update

    /**
     * 更新已有数据，根据主键（uid）匹配，而非整个EquipWarning对象
     * 返回类型int代表更新的条目数目，而非主键uid的值。
     * 表示更新了多少条目
     *
     * @param user user
     * @return count
     */
    @Update()
    int update(User user);

    /**
     * 同上
     *
     * @param users users
     * @return count
     */
    @Update()
    int updateAll(User... users);

    /**
     * 同上
     *
     * @param users users
     * @return count
     */
    @Update()
    int updateAll(List<User> users);
    //endregion

    //-------------------delete-------------------

    //region delete

    /**
     * 删除EquipWarning数据，数据的匹配通过主键uid实现。
     * 返回int数据表示删除了多少条。非主键uid值。
     *
     * @param user user
     * @return count
     */
    @Delete
    int delete(User user);

    /**
     * 删除EquipWarning数据，数据的匹配通过主键uid实现。
     * 返回int数据表示删除了多少条。非主键uid值。
     *
     * @param users users
     * @return count
     */
    @Delete
    int deleteAll(List<User> users);

    /**
     * 删除EquipWarning数据，数据的匹配通过主键uid实现。
     * 返回int数据表示删除了多少条。非主键uid值。
     *
     * @param users users
     * @return count
     */
    @Delete
    int deleteAll(User... users);
    //endregion

    //-------------------query-------------------


    //region query

    /**
     * 查询所有
     *
     * @return EquipWarning
     */
    @Query("SELECT * FROM user")
    User[] loadAllUsers();


    /**
     * 查询所有
     *
     * @return EquipWarning
     */
    @Query("SELECT * FROM user WHERE id = (:userId) LIMIT 1")
    User[] loadUser(long userId);
    //endregion
}

