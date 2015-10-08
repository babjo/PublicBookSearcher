package com.seoul.publicbooksearcher.presentation.presenter;

import java.util.List;

public interface SearchTitlesPresenter {

    void searchBefore();
    void searchCompleted(List<String> titles);

}
