package com.smasher.media.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaMetadataCompat.Builder;
import android.util.Log;

import com.smasher.oa.core.thread.IExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * @author matao
 * @date 2019/5/24
 */
public class MusicLoader {

    private static final String TAG = "MusicLoader";

    /**
     * 音频库的Uri
     */
    private static Uri mAudioUri = Audio.Media.EXTERNAL_CONTENT_URI;

    private static MusicLoader OUR_INSTANCE;

    public static MusicLoader getInstance() {
        if (OUR_INSTANCE == null) {
            OUR_INSTANCE = new MusicLoader();
        }
        return OUR_INSTANCE;
    }

    private MusicLoader() {
    }


    /**
     * 声明一个内容解析器对象
     */
    private ContentResolver mResolver;
    private Future<ArrayList<MediaMetadataCompat>> mFuture;

    private static String[] mMediaColumn = new String[]{
            // 编号
            Audio.Media._ID,
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
     * 本地所有的音乐
     */
    private List<MediaMetadataCompat> mLocalMusicList;

    public void init(Context context) {
        Log.d(TAG, "init: ");
        mResolver = context.getContentResolver();
        load();
    }


    public List<MediaMetadataCompat> getLocalMusicList() {
        if (mLocalMusicList == null) {
            load();
            try {
                mLocalMusicList = mFuture.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mLocalMusicList != null) {
            Log.d(TAG, "getLocalMusicList: " + mLocalMusicList.size());
        }
        return mLocalMusicList;
    }


    public List<MediaItem> getChildren() {

        getLocalMusicList();

        if (mLocalMusicList == null) {
            return null;
        }

        List<MediaItem> mediaItems = new ArrayList<>();
        for (MediaMetadataCompat item : mLocalMusicList) {
            mediaItems.add(createMediaItem(item));
        }

        Log.d(TAG, "getChildren: " + mediaItems.size());
        return mediaItems;
    }

    private MediaItem createMediaItem(MediaMetadataCompat item) {
        String id = item.getDescription().getMediaId();
        Log.d(TAG, "createMediaItem: " + id);
        MediaMetadataCompat temp = new Builder(item)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id).build();
        return new MediaItem(temp.getDescription(), MediaItem.FLAG_PLAYABLE);
    }


    /**
     *
     */
    private void load() {
        mFuture = IExecutor.getInstance().submitFuture(() -> {

            ArrayList<MediaMetadataCompat> list = null;
            Cursor cursor = null;
            try {
                // 通过内容解析器查询系统的音频库，并返回结果集的游标
                cursor = mResolver.query(mAudioUri, mMediaColumn, null, null, null);
                if (cursor == null) {
                    return list;
                }

                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    MediaMetadataCompat item = getMediaMetadataCompat(cursor);
                    list.add(item);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            }
            return list;
        });
    }

    private MediaMetadataCompat getMediaMetadataCompat(Cursor cursor) {

        long musciId = cursor.getLong(0);
        String title = cursor.getString(1);
        String album = cursor.getString(2);
        long duration = cursor.getInt(3);
        long size = cursor.getLong(4);
        String artist = cursor.getString(5);
        String url = cursor.getString(6);


        Builder mBuilder = new Builder();
        mBuilder.putText(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(musciId))
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putText(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, url);
        return mBuilder.build();
    }

}
