package com.seoul.publicbooksearcher;

import java.util.List;

public interface UserRequestListener {

    void searchBefore();
    void searchCompleted(List<Book> books);

}
