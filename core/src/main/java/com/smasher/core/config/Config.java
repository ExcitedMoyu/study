package com.smasher.core.config;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;


import com.smasher.core.db.ConfigDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moyu
 */
public class Config {
    public final static String SETTING_CMFU_TOKEN = "SETTING_CMFU_TOKEN";
    public final static String SETTING_IMEI_LIST = "SETTING_IMEI_LIST";

    private static Config mInstance;
    private Map<String, String> settingSet;

    public static synchronized Config getInstance() {
        if (mInstance == null) {
            mInstance = new Config();
        }
        return mInstance;
    }

    private Config() {
    }

    public static boolean QDConfigISNull() {
        return mInstance == null;
    }

    private void loadUserSettingFromDB() {
        settingSet = Collections.synchronizedMap(new HashMap<String, String>());
        Cursor cursor = null;
        try {
            cursor = ConfigDatabase.getInstance().query("setting", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String key = cursor.getString(cursor.getColumnIndex("Key"));
                String value = cursor.getString(cursor.getColumnIndex("Value"));
                settingSet.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getSetting(String key, String defaultValue) {
        if (settingSet == null) {
            loadUserSettingFromDB();
        }
        return settingSet.get(key) == null ? defaultValue : settingSet.get(key);
    }

    public boolean setSetting(String key, String value) {
        if (settingSet == null) {
            loadUserSettingFromDB();
        }
        settingSet.put(key, value);
        return setUserSettingToDB(key, value);
    }

    public void reloadSetting() {
        loadUserSettingFromDB();
    }

    private boolean setUserSettingToDB(String key, String value) {
        ContentValues values = new ContentValues();
        values.put("Key", key);
        values.put("Value", value);
        long result = ConfigDatabase.getInstance().replace("setting", null, values);
        return result > 0;
    }

    public boolean updateImei(String imei) {
        ArrayList<String> list = getImeiList();
        boolean isFind = false;
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if (item.equalsIgnoreCase(imei)) {
                isFind = true;
            }
        }
        if (!isFind) {
            list.add(imei);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i)).append("|");
            }
            String imeiStr = sb.toString();
            imeiStr = imeiStr.substring(0, imeiStr.length() - 1);
            setSetting(SETTING_IMEI_LIST, imeiStr);
        }
        return true;

//		if (imeiSet == null) {
//			loadImeiListFromDB();
//		}
//		if (!imeiSet.contains(imei)) {
//			imeiSet.add(imei);
//			return UpdateImeiToDB(imei);
//		}
//		return true;
    }

    public ArrayList<String> getImeiList() {
        String str = getSetting(SETTING_IMEI_LIST, "");
        String[] array = str.split("\\|");
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            result.add(array[i]);
        }
        return result;
    }

    public String getImei() {
        StringBuffer sb = new StringBuffer();
        // 如果当前imei号无用,遍历数据库历史imei号
        List<String> imeiList = getImeiList();
        if (imeiList != null && imeiList.size() > 0) {
            for (int i = 0; i < imeiList.size(); i++) {
                String imei = imeiList.get(i);
                if (!TextUtils.isEmpty(imei)) {
                    sb.append(";");
                    sb.append(imei);
                }
            }
            return sb.toString().length() > 1 ? sb.toString().substring(1) : "";
        }
        return "";
    }

    public String getUserToken() {
        return getSetting(SETTING_CMFU_TOKEN, "");
    }

    public void resetSetting() {
        try {
            ConfigDatabase.getInstance().beginTransaction();
//            ConfigDatabase.getInstance().delete("setting", "key='SettingBrightness'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingFont'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingFontColor'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingFontSize'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingFullScreen'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingIsNight'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingScreenOrientation'", null);
//            ConfigDatabase.getInstance().delete("setting", "key='SettingSystemBrightness'", null);
            ConfigDatabase.getInstance().setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ConfigDatabase.getInstance().endTransaction();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        loadUserSettingFromDB();
    }

    public void clearSetting() {
        loadUserSettingFromDB();
    }

}
