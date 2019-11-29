package com.smasher.core.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smasher.core.entity.Warning;

import java.util.List;

/**
 * @author Smasher
 * on 2019/11/20 0020
 */
@Dao
public interface WarningDao {


    //region insert

    /**
     * OnConflictStrategy.REPLACE表示如果已经有数据，那么就覆盖掉
     * 数据的判断通过主键进行匹配，也就是uid，非整个EquipWarning对象
     * 返回Long数据表示，插入条目的主键值（uid）
     *
     * @param equipWarning equipWarning
     * @return id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Warning equipWarning);

    /**
     * 返回List<Long>数据表示被插入数据的主键uid列表
     *
     * @param equipWarnings equipWarnings
     * @return list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(Warning... equipWarnings);

    /**
     * 返回List<Long>数据表示被插入数据的主键uid列表
     *
     * @param warnings warnings
     * @return list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Warning> warnings);
    //endregion


    //---------------------update------------------------

    //region update

    /**
     * 更新已有数据，根据主键（uid）匹配，而非整个EquipWarning对象
     * 返回类型int代表更新的条目数目，而非主键uid的值。
     * 表示更新了多少条目
     *
     * @param warning EquipWarning
     * @return count
     */
    @Update()
    int update(Warning warning);

    /**
     * 同上
     *
     * @param warnings warnings
     * @return count
     */
    @Update()
    int updateAll(Warning... warnings);

    /**
     * 同上
     *
     * @param warnings warnings
     * @return count
     */
    @Update()
    int updateAll(List<Warning> warnings);
    //endregion

    //-------------------delete-------------------

    //region delete

    /**
     * 删除EquipWarning数据，数据的匹配通过主键uid实现。
     * 返回int数据表示删除了多少条。非主键uid值。
     *
     * @param equipWarning equipWarning
     * @return count
     */
    @Delete
    int delete(Warning equipWarning);

    /**
     * 删除EquipWarning数据，数据的匹配通过主键uid实现。
     * 返回int数据表示删除了多少条。非主键uid值。
     *
     * @param equipWarnings equipWarnings
     * @return count
     */
    @Delete
    int deleteAll(List<Warning> equipWarnings);

    /**
     * 删除EquipWarning数据，数据的匹配通过主键uid实现。
     * 返回int数据表示删除了多少条。非主键uid值。
     *
     * @param equipWarnings equipWarnings
     * @return count
     */
    @Delete
    int deleteAll(Warning... equipWarnings);
    //endregion

    //-------------------query-------------------


    //region query

    /**
     * 查询所有
     *
     * @return EquipWarning
     */
    @Query("SELECT * FROM Warning")
    List<Warning> loadAllWarning();
    //endregion
}
