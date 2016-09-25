package com.seoul.publicbooksearcher.domain.models;

import java.util.Date;

import io.requery.Entity;

/**
 * Created by LCH on 2016. 9. 25..
 */

@Entity
public interface Keyword {
    String getValue();
    Date getCreateAt();
}
