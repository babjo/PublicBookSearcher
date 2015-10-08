package com.seoul.publicbooksearcher.domain;

public interface UseCase <Result, Argument> {

    Result execute(Argument arg);

}
