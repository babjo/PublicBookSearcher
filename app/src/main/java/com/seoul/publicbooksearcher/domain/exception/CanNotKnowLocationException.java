package com.seoul.publicbooksearcher.domain.exception;

import com.seoul.publicbooksearcher.Const;

public class CanNotKnowLocationException extends RuntimeException{

    public CanNotKnowLocationException(){
        super(Const.EXCEPTION_MSG_CAN_NOT_KNOW_LOCATION);
    }
}
