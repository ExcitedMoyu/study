package com.smasher.media.manager;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;


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

    private int mCurrentIndex;

    public QueueManager(MusicLoader musicProvider, QueueListener mQueueListener) {
        mLoader = musicProvider;
        mQueueListener = mQueueListener;
        mPlayingQueue = Collections.synchronizedList(new ArrayList<>());
    }

    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mQueueListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }

    public QueueItem getCurrentMusic() {
        if (!isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex);
    }


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


    public void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue,
                                String initialMediaId) {
        mPlayingQueue = newQueue;
        int index = 0;
        if (initialMediaId != null) {
            index = getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
    }


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


    public boolean skipQueuePositionByMediaId(String mediaId) {
        mCurrentIndex = getMusicIndexOnQueue(mPlayingQueue, mediaId);
        return true;
    }


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
