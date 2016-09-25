package com.seoul.publicbooksearcher.infrastructure;

import android.content.Context;

import com.seoul.publicbooksearcher.domain.models.Models;

import org.androidannotations.annotations.EBean;

import io.requery.Persistable;
import io.requery.android.BuildConfig;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

/**
 * Created by LCH on 2016. 9. 12..
 */

@EBean(scope = EBean.Scope.Singleton)
public class Requery {

    private Context mContext;
    private EntityDataStore data;

    public Requery(Context mContext) {
        this.mContext = mContext;
        this.data = createData(mContext);
    }

    private EntityDataStore createData(Context mContext) {
        DatabaseSource dataSource = new DatabaseSource(mContext, Models.DEFAULT, 6);
        dataSource.setLoggingEnabled(true);
        if (BuildConfig.DEBUG) {
            dataSource.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }
        Configuration configuration = dataSource.getConfiguration();
        return new EntityDataStore<Persistable>(configuration);
    }

    public EntityDataStore<Persistable> getData(){
        if(data == null) data = createData(mContext);
        return data;
    }

}
