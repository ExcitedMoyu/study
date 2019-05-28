// IBookManager.aidl
package com.smasher.aidl.manager;

// Declare any non-default types here with import statements
import com.smasher.aidl.entity.Book;
import com.smasher.aidl.listener.ObserverListener;

interface IBookManager {
         //得到全部数据（全部书籍）
        List<Book> getBookList();

        //添加数据的操作（添加书籍）
        void addBook(in Book book);

        //注册监听，用于监听新数据的变化。是典型的观察者模式的运用
        void registerListener(ObserverListener listener);

        //解注册
        void unregisterListener(ObserverListener listener);

        int getTestInt();
}
