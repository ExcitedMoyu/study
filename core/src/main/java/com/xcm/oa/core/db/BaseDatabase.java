package com.xcm.oa.core.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author moyu
 */
public abstract class BaseDatabase {

    public static final int SQL_ERROR = -100;
//    private static final int SQL_COUNT_COMMIT = 100;

    protected SQLiteDatabase mDB;
    private ConcurrentHashMap<String, Boolean> tableExist = new ConcurrentHashMap<>();

    private Lock lock = new ReentrantLock();

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = null;
        try {
            cursor = mDB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = mDB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean isOpen() {
        if (mDB == null) {
            return false;
        }
        return mDB.isOpen();
    }

    /***
     * 打开数据库
     * 修复了当DB文件损坏，打开失败的时候，删掉文件，重新创建数据库的逻辑
     *
     * @return boolean
     */
    public boolean openDataBase(File dbFile) {
        if (dbFile == null) {
            return false;
        }
        //是否需要创建表结构
        boolean needCreateTables = false;
        if (!dbFile.exists()) {
            //如果文件不存在，那么创建新文件
            try {
                dbFile.createNewFile();
                needCreateTables = true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            this.mDB = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            //如果打开DB出错，那么删除DB，重新创建一个DB
            try {
                dbFile.delete();
                dbFile.createNewFile();
                //删除以后，重新创建文件，再尝试open DB
                try {
                    this.mDB = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return false;
                }

                needCreateTables = true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }

        if (needCreateTables) {
            //如果需要创建表结构
            createTables();
        }
        //升级db版本号
        upgradeDB();

        //检查表结构
        checkDataBase();
        return true;
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        if (!tableIsExist(table)) {
            return SQL_ERROR;
        }
        long result = -1;
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            result = mDB.insert(table, nullColumnHack, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
        return result;
    }

    public void beginTransaction() {
        try {
            lock.lock();
            mDB.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean inTransaction() {
        try {
            return mDB.inTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setTransactionSuccessful() {
        try {
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endTransaction() throws Exception {
        try {
            mDB.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        if (!tableIsExist(table)) {
            return SQL_ERROR;
        }
        int result = -1;
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            result = mDB.update(table, values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
        return result;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        if (!tableIsExist(table)) {
            return SQL_ERROR;
        }
        int result = 0;
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            result = mDB.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
        return result;
    }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        if (!tableIsExist(table)) {
            return SQL_ERROR;
        }
        long result = -1;
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            result = mDB.replace(table, nullColumnHack, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
        return result;
    }

    public void execSQL(String sql) {
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            mDB.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
    }

    public void execSQL(String sql, Object[] bindArgs) {
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            mDB.execSQL(sql, bindArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(sql, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    private ArrayList<String> opSqlList;

    private static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};

    /**
     * General method for inserting a row into the database.
     *
     * @param table             the table to insert the row into
     * @param nullColumnHack    optional; may be <code>null</code>. SQL doesn't allow
     *                          inserting a completely empty row without naming at least one
     *                          column name. If your provided <code>initialValues</code> is
     *                          empty, no column names are known and an empty row can't be
     *                          inserted. If not set to null, the <code>nullColumnHack</code>
     *                          parameter provides the name of nullable column name to
     *                          explicitly insert a NULL into in the case where your
     *                          <code>initialValues</code> is empty.
     * @param initialValues     this map contains the initial column values for the row. The
     *                          keys should be the column names and the values the column
     *                          values
     * @param conflictAlgorithm for insert conflict resolver
     */
    public void insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT");
        sql.append(CONFLICT_VALUES[conflictAlgorithm]);
        sql.append(" INTO ");
        sql.append(table);
        sql.append('(');

        Object[] bindArgs = null;
        int size = (initialValues != null && initialValues.size() > 0) ? initialValues.size() : 0;
        if (size > 0) {
            bindArgs = new Object[size];
            int i = 0;

            Set<Map.Entry<String, Object>> entrySet = initialValues.valueSet();
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String colName = entry.getKey();
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                bindArgs[i++] = initialValues.get(colName);
            }
            sql.append(')');
            sql.append(" VALUES (");
            for (i = 0; i < size; i++) {
                sql.append((i > 0) ? ",?" : "?");
            }
        } else {
            sql.append(nullColumnHack + ") VALUES (NULL");
        }
        sql.append(')');
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            mDB.execSQL(sql.toString(), bindArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
    }

    /**
     * 插入或更新记录（这个需要为表设置唯一性索引 UNIQUE(field...)）
     *
     * @param table  table
     * @param values values
     * @return long
     */
    public long saveOrUpdate(String table, ContentValues values) {
        if (!tableIsExist(table)) {
            return SQL_ERROR;
        }
        long result = -1;
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            StringBuffer sqlSB = new StringBuffer();
            sqlSB.append("INSERT OR REPLACE INTO ").append(table);
            int valuesSize = values.size();
            if (valuesSize > 0) {
                sqlSB.append(" (");
                for (String key : values.keySet()) {
                    sqlSB.append(key).append(",");
                }
                //删除最后一个逗点
                sqlSB.deleteCharAt(sqlSB.length() - 1);
                sqlSB.append(") ");
                sqlSB.append("VALUES ");
                sqlSB.append(" (");
                for (int i = 0; i < valuesSize; i++) {
                    sqlSB.append("?").append(",");
                }
                //删除最后一个逗点
                sqlSB.deleteCharAt(sqlSB.length() - 1);
                sqlSB.append(")");
            }
            SQLiteStatement stat = mDB.compileStatement(sqlSB.toString());

            int pos = 1;
            for (String key : values.keySet()) {
                Object valueObj = values.get(key);
                if (valueObj instanceof Long) {
                    stat.bindLong(pos, (long) valueObj);
                } else if (valueObj instanceof Integer) {
                    stat.bindLong(pos, (int) valueObj);
                } else if (valueObj instanceof Short) {
                    stat.bindLong(pos, (short) valueObj);
                } else if (valueObj instanceof Byte) {
                    stat.bindLong(pos, (byte) valueObj);
                } else if (valueObj instanceof Boolean) {
                    //true:1  false:0
                    stat.bindLong(pos, ((boolean) valueObj) ? 1 : 0);
                } else if (valueObj instanceof Float) {
                    stat.bindDouble(pos, (float) valueObj);
                }
                if (valueObj instanceof Double) {
                    stat.bindDouble(pos, (double) valueObj);
                } else if (valueObj instanceof Character) {
                    stat.bindDouble(pos, (char) valueObj);
                } else if (valueObj instanceof String) {
                    stat.bindString(pos, (String) valueObj);
                }
                pos++;
            }
            result = stat.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!inTransaction()) {
                lock.unlock();
            }
        }
        return result;
    }

    public void closeDB() {
        try {
            if (!inTransaction()) {
                lock.lock();
            }
            if (this.mDB != null) {
                mDB.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            mDB = null;
        }
    }

    /**
     * createTables
     */
    protected abstract void createTables();

    /**
     * upgradeDB
     */
    protected abstract void upgradeDB();

    /**
     * tableIsExist
     *
     * @param table table
     * @return boolean
     */
    public boolean tableIsExist(String table) {
        if (tableExist == null) {
            tableExist = new ConcurrentHashMap<String, Boolean>();
        }
        if (tableExist.containsKey(table) && tableExist.get(table)) {
            return true;
        }

        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + table + "' ";
            cursor = mDB.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    tableExist.put(table, true);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            cursor = null;
        }

        return false;
    }

    /***
     * 检查数据库的完整性，就是表是否存在
     */
    protected abstract void checkDataBase();

    public SQLiteStatement compileStatement(String sql) {
        return mDB.compileStatement(sql);
    }
}
