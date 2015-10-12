package com.seoul.publicbooksearcher.data.cache.book;

import android.content.Context;

public class GdBookCache extends AbstractBookCache {

    public GdBookCache(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return "gd_table";
    }
}
