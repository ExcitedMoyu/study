package com.xcm.oa.core.entity;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author moyu
 */
public class SettingItem {
    public String Key;
    public String Value;

    public ContentValues getContentValues() {
        ContentValues value = new ContentValues();
        value.put("Key", Key);
        value.put("Value", Value);
        return value;
    }

    public JSONObject getJSONObject() {
        JSONObject value = new JSONObject();
        try {
            value.put("Key", Key);
            value.put("Value", Value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
}
