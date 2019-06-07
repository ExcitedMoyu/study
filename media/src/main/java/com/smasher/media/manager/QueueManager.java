package com.smasher.media.manager;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;


import com.smasher.media.loader.MusicLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 2019/6/2.
 *
 * @author moyu
 */
public class QueueManager {
    private QueueListener mQueueListener;
    private MusicLoader mLoader;
    private List<QueueItem> mPlayingQueue;
    private ArrayList<Integer> mListIndex;
    private int mRepeatMode;
    private int mShuffleMode;

    private int mCurrentIndex;
    private int mRealIndex;

    public QueueManager(MusicLoader musicProvider, QueueListener mQueueListener) {
        mLoader = musicProvider;
        mQueueListener = mQueueListener;
        mPlayingQueue = Collections.synchronizedList(new ArrayList<>());
        mListIndex = new ArrayList<>();
    }

    /**
     * Sets the repeat mode for this session.
     *
     * @param repeatMode The repeat mode. Must be one of the followings:
     *                   {@link PlaybackStateCompat#REPEAT_MODE_NONE},
     *                   {@link PlaybackStateCompat#REPEAT_MODE_ONE},
     *                   {@link PlaybackStateCompat#REPEAT_MODE_ALL},
     *                   {@link PlaybackStateCompat#REPEAT_MODE_GROUP}
     */
    public void onSetRepeatMode(int repeatMode) {
        mRepeatMode = repeatMode;
    }


    /**
     * Sets the shuffle mode for this session.
     *
     * @param shuffleMode The shuffle mode. Must be one of the followings:
     *                    {@link PlaybackStateCompat#SHUFFLE_MODE_NONE},
     *                    {@link PlaybackStateCompat#SHUFFLE_MODE_ALL},
     *                    {@link PlaybackStateCompat#SHUFFLE_MODE_GROUP}
     */
    public void onSetShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        mShuffleMode = shuffleMode;
        changeShuffleMode();
    }


    //region 位置相关

    /**
     * 指定index曲目
     * setCurrentQueueIndex
     *
     * @param index index
     */
    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mQueueListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }


    /**
     * 手动切歌（上一曲/下一曲）
     * skipQueuePosition
     *
     * @param amount amount
     * @return boolean
     */
    public boolean skipQueuePosition(int amount) {
        int index = mCurrentIndex + amount;
        if (index < 0) {
            // skip backwards before the first song will keep you on the first song
            index = 0;
        } else {
            // skip forwards when in last song will cycle back to start of the queue
            index %= mPlayingQueue.size();
        }
        if (!isIndexPlayable(index, mPlayingQueue)) {
            return false;
        }
        mCurrentIndex = index;
        return true;
    }


    public boolean skipNext() {
        return false;
    }


    /**
     * 指定media相关曲目
     *
     * @param mediaId mediaId
     * @return boolean
     */
    public boolean skipQueuePositionByMediaId(String mediaId) {
        mCurrentIndex = getMusicIndexOnQueue(mPlayingQueue, mediaId);
        return true;
    }


    /**
     * 查询对应曲目在队列中的index
     *
     * @param queue   queue
     * @param mediaId mediaId
     * @return queue
     */
    private int getMusicIndexOnQueue(Iterable<QueueItem> queue, String mediaId) {
        int index = 0;
        for (QueueItem item : queue) {
            if (mediaId.equals(item.getDescription().getMediaId())) {
                return index;
            }
            index++;
        }
        return -1;
    }


    //endregion


    public QueueItem getCurrentMusic() {
        if (!isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex);
    }


    /**
     * 返回曲目列表size
     *
     * @return int
     */
    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }


    public void setQueueFromMusic(String mediaId) {
        String queueTitle = "local_music";
        setCurrentQueue(queueTitle, getPlayingQueue(mediaId, mLoader), mediaId);
    }


    /**
     * 设置曲目列表
     *
     * @param title          title
     * @param newQueue       newQueue
     * @param initialMediaId initialMediaId
     */
    public void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue,
                                String initialMediaId) {
        mPlayingQueue = newQueue;
        buildIndexList();
        int index = 0;
        if (initialMediaId != null) {
            index = getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
    }

    /**
     * index列表
     */
    private void buildIndexList() {
        if (mPlayingQueue.size() <= 0) {
            return;
        }
        int size = mPlayingQueue.size();
        if (mListIndex == null) {
            mListIndex = new ArrayList<>();
        } else {
            mListIndex.clear();
        }
        for (int i = 0; i < size; i++) {
            mListIndex.add(i);
        }
        changeShuffleMode();
    }


    private void changeShuffleMode() {
        int size = mPlayingQueue.size();
        if (size == 0) {
            return;
        }

        if (size != mListIndex.size()) {
            buildIndexList();
        }

        switch (mShuffleMode) {
            case PlaybackStateCompat.SHUFFLE_MODE_NONE:
                for (int i = 0; i < size; i++) {
                    mListIndex.set(i, i);
                }
                break;
            case PlaybackStateCompat.SHUFFLE_MODE_ALL:
                Collections.shuffle(mListIndex);
                break;
            default:
                break;
        }

    }


    /**
     * playable
     *
     * @param index index
     * @param queue queue
     * @return boolean
     */
    private boolean isIndexPlayable(int index, List<MediaSessionCompat.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }


    private List<QueueItem> getPlayingQueue(String mediaId, MusicLoader musicLoader) {

        //这里由于自己的需求，直接获取所有的本地音乐
        Iterable<MediaMetadataCompat> tracks = musicLoader.getLocalMusicList();
        return convertToQueue(tracks);
    }


    /**
     * 将含有MediaMetadataCompat数据转换为queue
     */
    public List<QueueItem> convertToQueue(Iterable<MediaMetadataCompat> tracks) {
        List<QueueItem> queue = new ArrayList<>();
        int count = 0;
        for (MediaMetadataCompat track : tracks) {

            // We create a hierarchy-aware mediaID, so we know what the queue is about by looking
            // at the QueueItem media IDs.
            String hierarchyAwareMediaID = track.getDescription().getMediaId();

            MediaMetadataCompat trackCopy = new MediaMetadataCompat.Builder(track)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                    .build();

            // We don't expect queues to change after created, so we use the item index as the
            // queueId. Any other number unique in the queue would work.
            QueueItem item = new QueueItem(trackCopy.getDescription(), count++);
            queue.add(item);
        }
        return queue;

    }


    public List<QueueItem> convertToQueue(List<MediaBrowserCompat.MediaItem> items) {
        List<QueueItem> queue = new ArrayList<>();
        for (MediaBrowserCompat.MediaItem track : items) {
            long QueueId = 0;
            QueueItem item = new QueueItem(track.getDescription(), QueueId);
            queue.add(item);
        }
        return queue;

    }


    public MediaMetadataCompat convertToMediaMetadata(QueueItem queueItem) {
        MediaDescriptionCompat description = queueItem.getDescription();
        Bundle bundle = description.getExtras();
        String mediaId = description.getMediaId();
        Uri uri = description.getMediaUri();

        String title = null;
        String album = null;
        String artist = null;
        String url = null;
        long duration = 0;

        CharSequence charSequence = description.getTitle();
        if (charSequence != null) {
            title = description.getTitle().toString();
        }
        if (uri != null) {
            url = uri.toString();
        }

        if (bundle != null) {
            album = bundle.getString(MediaMetadataCompat.METADATA_KEY_ALBUM);
            artist = bundle.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            duration = bundle.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        }


        MediaMetadataCompat.Builder mBuilder = new MediaMetadataCompat.Builder();
        mBuilder.putText(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putText(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, url);
        return mBuilder.build();
    }


    public interface QueueListener {
        /**
         * metadata
         *
         * @param metadata metadata
         */
        void onMetadataChanged(MediaMetadataCompat metadata);

        /**
         * Error
         */
        void onMetadataRetrieveError();

        /**
         * queueIndex
         *
         * @param queueIndex queueIndex
         */
        void onCurrentQueueIndexUpdated(int queueIndex);

        /**
         * @param title    title
         * @param newQueue newQueue
         */
        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
