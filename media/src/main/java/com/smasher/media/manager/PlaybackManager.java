package com.smasher.media.manager;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Created on 2019/6/2.
 *
 * @author moyu
 */
public class PlaybackManager {


    private static PlaybackManager OUR_INSTANCE;

    public static PlaybackManager getInstance() {
        if (OUR_INSTANCE == null) {
            OUR_INSTANCE = new PlaybackManager();
        }
        return OUR_INSTANCE;
    }

    private PlaybackManager() {
        mBuilder = new PlaybackStateCompat.Builder();
        mBuilder.setActions(MEDIA_SESSION_ACTIONS);
        mBuilder.setState(PlaybackStateCompat.STATE_NONE, 0, 1f);
        mState = mBuilder.build();
    }


    private static final long MEDIA_SESSION_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SEEK_TO;


    private PlaybackStateCompat.Builder mBuilder;
    private PlaybackStateCompat mState;
    private MediaSessionCompat mSession;

    public void setMediaSession(MediaSessionCompat session) {
        mSession = session;
    }


    public void setState(int state) {
        mBuilder.setState(state, 0, 1f);
        mState = mBuilder.build();
        if (mSession != null) {
            mSession.setPlaybackState(mState);
        }
    }

    public PlaybackStateCompat getState() {
        return mState;
    }
}
