package com.seoul.publicbooksearcher;

import java.util.List;

public interface UserRequestListener {

    void searchCompleted(List<Book> books);

}
