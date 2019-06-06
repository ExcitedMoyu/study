package com.smasher.media.helper;

import android.os.Environment;
import android.util.Log;

/**
 * Created on 2019/6/6.
 *
 * @author moyu
 */
public class TestHelper {


    public TestHelper() {
    }


    public String getPathString(String TAG) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Path:").append('\n');

        String data = Environment.getDataDirectory().getPath();
        Log.d(TAG, "data: " + data);
        stringBuilder.append("data:").append(data).append('\n').append('\n');

        String dataAbs = Environment.getDataDirectory().getAbsolutePath();
        Log.d(TAG, "data_abs: " + dataAbs);
        stringBuilder.append("dataAbs:").append(dataAbs).append('\n').append('\n');

        String cache = Environment.getDownloadCacheDirectory().getPath();
        Log.d(TAG, "cache: " + cache);
        stringBuilder.append("cache:").append(cache).append('\n').append('\n');

        String cacheAbs = Environment.getDownloadCacheDirectory().getAbsolutePath();
        Log.d(TAG, "cache_abs: " + cacheAbs);
        stringBuilder.append("cacheAbs:").append(cacheAbs).append('\n').append('\n');

        String system = Environment.getRootDirectory().getPath();
        Log.d(TAG, "system: " + system);
        stringBuilder.append("system:").append(system).append('\n').append('\n');

        String systemAbs = Environment.getRootDirectory().getAbsolutePath();
        Log.d(TAG, "system_abs: " + systemAbs);
        stringBuilder.append("systemAbs:").append(systemAbs).append('\n').append('\n');

        String music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
        Log.d(TAG, "music: " + music);
        stringBuilder.append("music:").append(music).append('\n').append('\n');

        String musicAbs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        Log.d(TAG, "music_abs: " + musicAbs);
        stringBuilder.append("musicAbs:").append(musicAbs).append('\n').append('\n');

        return stringBuilder.toString();
    }
}
