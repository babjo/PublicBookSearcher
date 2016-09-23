package com.seoul.publicbooksearcher.domain.dto;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class SearchTitlesRequestDTO {
    private String keyword;

    public SearchTitlesRequestDTO(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
