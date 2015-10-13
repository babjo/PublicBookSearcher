package com.seoul.publicbooksearcher.presentation;

public interface UseCaseListener <Before, After>{
    void executeBefore(Before beforeArgs);
    void executeAfter(After afterArgs);
    void error(Exception e);
}
