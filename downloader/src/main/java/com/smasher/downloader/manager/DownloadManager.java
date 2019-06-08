package com.smasher.downloader.manager;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.DrawableRes;

import com.smasher.downloader.entity.DownloadInfo;
import com.smasher.downloader.entity.RequestInfo;
import com.smasher.downloader.listener.DownloadListener;
import com.smasher.downloader.listener.DownloadObserver;
import com.smasher.downloader.path.AppPath;
import com.smasher.downloader.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载管理
 *
 * @author matao
 */
public class DownloadManager implements DownloadListener {

    private static final String TAG = "[DL]DownLoadMG";

    private volatile static DownloadManager mInstance;

    private ArrayList<RequestInfo> requests = new ArrayList<>();
    private List<DownloadObserver> mObservers = new ArrayList<>();

    private boolean enableNotification;
    private String mSavePath;

    @DrawableRes
    private int notificationIcon;

    public static DownloadManager getInstance() {
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }

    private DownloadManager() {
    }


    /**
     * 初始化
     *
     * @param savePath 默认的保存地址
     */
    public void init(Context context, String savePath) {
        //启动服务
        startService(context);

        if (TextUtils.isEmpty(savePath)) {
            AppPath.init("Download[DL]");
            mSavePath = AppPath.getDownloadPath(context);
        } else {
            mSavePath = savePath;
        }
    }


    /**
     * 初始化
     *
     * @param id 图标Id
     */
    public void setNotificationIcon(@DrawableRes int id) {
        notificationIcon = id;
    }


    public int getNotificationIcon() {
        return notificationIcon;
    }

    /**
     * 注册观察者
     */
    public void registerObserver(Context context, DownloadObserver observer) {

        synchronized (mObservers) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
        bindService(context);

    }

    /**
     * 反注册观察者
     */
    public void unRegisterObserver(Context context, DownloadObserver observer) {
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                mObservers.remove(observer);
            }
        }
        unbindService(context);
    }


    public void setNotificationEnable(boolean enable) {
        enableNotification = enable;
        if (mService != null) {
            mService.setNotificationEnable(enable);
            Log.d(TAG, "setNotificationEnable: mService!=null");
        }
    }


    public String getSavePath() {
        return mSavePath;
    }

    /**
     * 提交  下载/暂停  等任务.(提交就意味着开始执行生效)
     *
     * @param context context
     */
    public synchronized void submit(Context context) {
        if (requests.isEmpty()) {
            Log.w(TAG, "没有下载任务可供执行");
            return;
        }
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.SERVICE_INTENT_EXTRA, requests);
        context.startService(intent);
        requests.clear();
    }


    /**
     * 添加 新的下载任务
     *
     * @param info 下载的url
     * @return DownloadManager (方便链式调用)
     */
    public DownloadManager addTask(DownloadInfo info) {
        int requestType = RequestInfo.COMMAND_DOWNLOAD;
        RequestInfo requestInfo = createRequest(info, requestType);
        Log.i(TAG, "addTask() requestInfo=" + requestInfo);
        requests.add(requestInfo);
        return this;
    }

    /**
     * 暂停某个下载任务
     *
     * @param info 下载的url
     * @return DownloadManager (方便链式调用)
     */
    public DownloadManager pauseTask(DownloadInfo info) {
        int requestType = RequestInfo.COMMAND_PAUSE;
        RequestInfo requestInfo = createRequest(info, requestType);
        Log.i(TAG, "pauseTask() -> requestInfo=" + requestInfo);
        requests.add(requestInfo);
        return this;
    }


    private RequestInfo createRequest(DownloadInfo info, int requestType) {
        RequestInfo request = new RequestInfo();
        request.setCommand(requestType);
        request.setDownloadInfo(info);
        return request;
    }


    private DownloadService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
            mService = binder.getService();
            mService.setListener(DownloadManager.this);
            mService.setNotificationEnable(enableNotification);
            Log.d(TAG, "onServiceConnected: " + enableNotification);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };


    private void bindService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DownloadService.class);
        context.bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }


    private void unbindService(Context context) {
        try {
            context.unbindService(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startService(Context context) {
        if (mService == null) {
            Intent intent = new Intent(context, DownloadService.class);
            context.startService(intent);
        }
    }


    private void stopService(Context context) {
        if (mService == null) {
            Intent intent = new Intent(context, DownloadService.class);
            context.stopService(intent);
        }
    }


    // region #Observers

    private void notifyDownloadWait(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownloadWait(info);
            }
        }
    }


    private void notifyDownloadProgressed(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownloadProgressed(info);
            }
        }
    }


    private void notifyDownloadPre(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownloadPre(info);
            }
        }
    }


    private void notifyDownloadFinished(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownLoadFinished(info);
            }
        }
    }

    private void notifyDownloadInstalled(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownLoadInstalled(info);
            }
        }
    }


    private void notifyDownloadError(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownloadError(info);
            }
        }
    }


    private void notifyDownloadPause(DownloadInfo info) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownloadError(info);
            }
        }
    }


    @Override
    public void onDownLoadWait(DownloadInfo info) {
        notifyDownloadWait(info);
    }

    @Override
    public void onDownLoadPre(DownloadInfo info) {
        notifyDownloadPre(info);
    }

    @Override
    public void onDownLoadProgress(DownloadInfo info) {
        notifyDownloadProgressed(info);
    }

    @Override
    public void onDownLoadFinished(DownloadInfo info) {
        notifyDownloadFinished(info);
    }

    @Override
    public void onDownLoadInstalled(DownloadInfo info) {
        notifyDownloadInstalled(info);
    }

    @Override
    public void onDownLoadError(DownloadInfo info) {
        notifyDownloadError(info);
    }

    @Override
    public void onDownLoadPause(DownloadInfo info) {
        notifyDownloadPause(info);
    }
    //end region
}
