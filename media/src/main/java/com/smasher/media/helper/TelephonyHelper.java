package com.smasher.media.helper;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


/**
 * @author matao
 * @date 2019/5/30
 */
public class TelephonyHelper {

    private TelephonyManager mTelephonyManager;

    public TelephonyHelper(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }


    public void setListener(PhoneStateListener listener) {
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }


}
