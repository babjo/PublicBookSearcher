package com.seoul.publicbooksearcher.presentation.listener;

import java.util.List;

public interface SearchTitlesListener {

    void searchBefore();
    void searchCompleted(List<String> titles);

}
