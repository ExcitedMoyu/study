package com.smasher.aidl.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @author matao
 * @date 2019/5/28
 */
public class Book implements Parcelable {

    private long mBookId;
    private String mBookName;
    private String mAuthor;

    public Book(String bookName) {
        mBookName = bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mBookId);
        dest.writeString(this.mBookName);
        dest.writeString(this.mAuthor);
    }

    public Book() {
    }

    public Book(Parcel in) {
        this.mBookId = in.readLong();
        this.mBookName = in.readString();
        this.mAuthor = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "bookName:" + mBookName + "}";
    }
}
