package com.seoul.publicbooksearcher.domain.dto;

import java.util.List;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class SearchTitlesResponseDTO {
    private final List<String> titles;

    public SearchTitlesResponseDTO(List<String> crawling) {
        this.titles = crawling;
    }

    public List<String> getTitles() {
        return titles;
    }
}
