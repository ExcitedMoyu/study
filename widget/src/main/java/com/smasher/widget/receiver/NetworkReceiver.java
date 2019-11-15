package com.smasher.widget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.smasher.core.utils.NetworkUtil;

/**
 * @author matao
 */
public class NetworkReceiver extends BroadcastReceiver {


    NetStatusMonitor mNetStatusMonitor;

    @Override
    public void onReceive(Context context, Intent intent) {
        String connectAction = ConnectivityManager.CONNECTIVITY_ACTION;
        String action = intent.getAction();
        if (action.equals(connectAction)) {
            boolean isReachable = NetworkUtil.isNetworkReachable();
            if (mNetStatusMonitor != null) {
                mNetStatusMonitor.onNetChange(isReachable);
            }
        }
    }


    /**
     * 网络状态类型改变的监听接口
     */
    public interface NetStatusMonitor {
        /**
         * 网络状态是否可用
         *
         * @param netStatus netStatus
         */
        void onNetChange(boolean netStatus);
    }


    public void setNetStatusMonitor(NetStatusMonitor netStatusMonitor) {
        mNetStatusMonitor = netStatusMonitor;
    }


}
