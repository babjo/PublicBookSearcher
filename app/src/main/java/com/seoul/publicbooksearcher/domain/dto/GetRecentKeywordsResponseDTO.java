package com.seoul.publicbooksearcher.domain.dto;

import java.util.List;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class GetRecentKeywordsResponseDTO {
    private final List<String> recentKeywords;

    public GetRecentKeywordsResponseDTO(List<String> recentKeywords) {
        this.recentKeywords = recentKeywords;
    }

    public List<String> getRecentKeywords() {
        return recentKeywords;
    }
}
