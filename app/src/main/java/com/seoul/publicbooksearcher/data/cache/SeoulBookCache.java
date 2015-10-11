package com.seoul.publicbooksearcher.data.cache;

import android.content.Context;

public class SeoulBookCache extends AbstractBookCache {

    public SeoulBookCache(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return "seoul_table";
    }
}
