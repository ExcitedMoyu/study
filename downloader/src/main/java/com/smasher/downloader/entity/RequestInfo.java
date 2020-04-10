package com.smasher.downloader.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;

import com.smasher.downloader.annotation.RequestType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author matao
 * @date 2019/4/24
 */
public class RequestInfo implements Parcelable {


    public RequestInfo() {
    }

    private int mCommand = 0;

    private DownloadInfo mDownloadInfo;


    public int getCommand() {
        return mCommand;
    }

    public void setCommand(@RequestType int command) {
        mCommand = command;
    }

    public DownloadInfo getDownloadInfo() {
        return mDownloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        mDownloadInfo = downloadInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCommand);
        dest.writeParcelable(this.mDownloadInfo, flags);
    }


    protected RequestInfo(Parcel in) {
        this.mCommand = in.readInt();
        this.mDownloadInfo = in.readParcelable(DownloadInfo.class.getClassLoader());
    }

    public static final Creator<RequestInfo> CREATOR = new Creator<RequestInfo>() {
        @Override
        public RequestInfo createFromParcel(Parcel source) {
            return new RequestInfo(source);
        }

        @Override
        public RequestInfo[] newArray(int size) {
            return new RequestInfo[size];
        }
    };
}
