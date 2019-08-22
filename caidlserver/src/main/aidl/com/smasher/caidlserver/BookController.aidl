// BookController.aidl
package com.smasher.caidlserver;

import com.smasher.caidlserver.Book;

interface BookController {
    List<Book> getBookList();
    void addBookInOut(inout Book book);
}
