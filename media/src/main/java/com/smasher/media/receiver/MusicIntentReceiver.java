package com.smasher.media.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;

/**
 * @author matao
 * @date 2019/5/25
 */
public class MusicIntentReceiver extends BroadcastReceiver {

    final IntentFilter filter;

    public MusicIntentReceiver() {
        filter = new IntentFilter();

        if (Build.VERSION.SDK_INT >= 21) {
            filter.addAction(AudioManager.ACTION_HEADSET_PLUG);
        } else {
            filter.addAction(Intent.ACTION_HEADSET_PLUG);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
