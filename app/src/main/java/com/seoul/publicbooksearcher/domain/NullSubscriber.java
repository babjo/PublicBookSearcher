package com.seoul.publicbooksearcher.domain;

import rx.Subscriber;

/**
 * Created by LCH on 2016. 9. 22..
 */

public class NullSubscriber extends Subscriber {
    @Override
    public void onCompleted() {
    }
    @Override
    public void onError(Throwable e) {
    }
    @Override
    public void onNext(Object o) {
    }
}
