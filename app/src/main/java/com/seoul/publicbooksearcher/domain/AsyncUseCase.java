package com.seoul.publicbooksearcher.domain;

import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

public interface AsyncUseCase <Argument>{
    void execute(Argument arg, AsyncUseCaseListener asyncUseCaseListener);
}
