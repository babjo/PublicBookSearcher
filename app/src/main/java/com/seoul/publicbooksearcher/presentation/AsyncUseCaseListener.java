package com.seoul.publicbooksearcher.presentation;

public interface AsyncUseCaseListener<Before, After>{
    void onBefore(Before beforeArgs);
    void onAfter(After afterArg);
    void onError(Exception e);
}
