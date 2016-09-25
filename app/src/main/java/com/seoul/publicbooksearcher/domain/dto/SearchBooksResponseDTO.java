package com.seoul.publicbooksearcher.domain.dto;

import com.seoul.publicbooksearcher.domain.models.SearchResult;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class SearchBooksResponseDTO {
    private SearchResult searchResult;

    public SearchBooksResponseDTO(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public SearchResult getSearchResult(){
        return searchResult;
    }
}
