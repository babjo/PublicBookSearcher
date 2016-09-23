package com.seoul.publicbooksearcher.domain.dto;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class AddRecentKeywordRequestDTO {
    private String keyword;

    public AddRecentKeywordRequestDTO(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
