package com.smasher.oa.core.db;

import android.content.ContentValues;


import java.util.ArrayList;

public class DBOperation {
    public enum QDOperationType {
        Insert, Update, Delete, Replace,
    }

    public String mTable;
    public QDOperationType mQDOperationType;
    public ContentValues mContentValues;
    public String mSelection;

    public DBOperation(String table, QDOperationType operationType, ContentValues contentValues, String selection) {
        mTable = table;
        mQDOperationType = operationType;
        mContentValues = contentValues;
        mSelection = selection;
    }

    public static void applyBatch(final ArrayList<DBOperation> operations) throws Exception {
        try {
            MainDatabase.getInstance().beginTransaction();

            for (DBOperation dbOperation : operations) {
                if (dbOperation.mQDOperationType == QDOperationType.Insert) {
                    MainDatabase.getInstance().insert(dbOperation.mTable, null, dbOperation.mContentValues);
                } else if (dbOperation.mQDOperationType == QDOperationType.Update) {
                    MainDatabase.getInstance().update(dbOperation.mTable, dbOperation.mContentValues, dbOperation.mSelection, null);
                } else if (dbOperation.mQDOperationType == QDOperationType.Delete) {
                    MainDatabase.getInstance().delete(dbOperation.mTable, dbOperation.mSelection, null);
                } else if (dbOperation.mQDOperationType == QDOperationType.Replace) {
                    MainDatabase.getInstance().replace(dbOperation.mTable, null, dbOperation.mContentValues);
                }
            }

            MainDatabase.getInstance().setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MainDatabase.getInstance().endTransaction();
        }
    }
}
