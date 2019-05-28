// ObserverListener.aidl.listener
package com.smasher.aidl.listener;

// Declare any non-default types here with import statements

import com.smasher.aidl.entity.Book;

interface ObserverListener {

   void onNewBookAdd(in Book book);

   void onAllBook();
}
