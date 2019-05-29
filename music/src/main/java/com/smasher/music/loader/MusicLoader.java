package com.smasher.music.loader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.util.Log;

import com.smasher.music.entity.MediaInfo;

import java.util.ArrayList;

/**
 * @author matao
 * @date 2019/5/24
 */
public class MusicLoader {

    private static final String TAG = "MusicLoader";

    private static MusicLoader OUR_INSTANCE;

    public static MusicLoader getInstance(ContentResolver resolver) {
        if (OUR_INSTANCE == null) {
            mResolver = resolver;
            OUR_INSTANCE = new MusicLoader();
        }
        return OUR_INSTANCE;
    }

    private MusicLoader() {
        Cursor cursor = null;
        try {
            // 通过内容解析器查询系统的音频库，并返回结果集的游标
            cursor = mResolver.query(mAudioUri, mMediaColumn, null, null, null);
            if (cursor == null) {
                return;
            }

            while (cursor.moveToNext()) {
                MediaInfo music = new MediaInfo();
                music.setId(cursor.getLong(0));
                music.setTitle(cursor.getString(1));
                music.setAlbum(cursor.getString(2));
                music.setDuration(cursor.getInt(3));
                music.setSize(cursor.getLong(4));
                music.setArtist(cursor.getString(5));
                music.setUrl(cursor.getString(6));
                Log.d(TAG, music.getTitle() + " " + music.getDuration());
                musicList.add(music);
            }
            cursor.close();


            String foldPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();


        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }

    }


    /**
     * 音频库的Uri
     */
    private static Uri mAudioUri = Audio.Media.EXTERNAL_CONTENT_URI;

    private static final ArrayList<MediaInfo> musicList = new ArrayList<>();


    /**
     * 声明一个内容解析器对象
     */
    private static ContentResolver mResolver;

    private static String[] mMediaColumn = new String[]{
            // 编号
            MediaStore.Audio.Media._ID,
            // 乐曲名
            Audio.Media.TITLE,
            // 专辑名
            Audio.Media.ALBUM,
            // 播放时长
            Audio.Media.DURATION,
            // 文件大小
            Audio.Media.SIZE,
            // 演唱者
            Audio.Media.ARTIST,
            // 文件路径
            Audio.Media.DATA
    };


    /**
     * 获取音乐队列
     */
    public ArrayList<MediaInfo> getMusicList() {
        return musicList;
    }
}
