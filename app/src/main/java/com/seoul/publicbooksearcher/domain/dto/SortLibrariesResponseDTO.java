package com.seoul.publicbooksearcher.domain.dto;

import com.seoul.publicbooksearcher.domain.Location;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class SortLibrariesResponseDTO {
    private final Location location;

    public SortLibrariesResponseDTO(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
