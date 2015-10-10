package com.seoul.publicbooksearcher.domain;

import java.util.List;

public class SeoulLibraryBookSearch {

    public int list_total_count;
    public Result result;
    public List<Row> row;

    public class Result{
        public String code;
        public String message;
    }

    public class Row{
        public String BIB_TYPE;
        public String BIB_TYPE_NAME;
        public String CTRLNO;
        public String TITLE;
        public String AUTHOR;
        public String PUBLER;
        public String NBOOK_YN;
        public String CRITYN;
        public String VODYN;
        public String OLDYN;
        public String ONLNYN;
        public String ABSYN;
        public String STRUCT_YN;
        public String OLD_INTRCN_YN;
        public String URLYN;
        public String CALL_NO;
        public String CLASS_NO;
        public String PUBLER_YEAR;
        public String AUTHOR_NO;
        public String LANG;
        public String LANG_NAME;
        public String CONTRY;
        public String CONTRY_NAME;
        public String EDITON;
        public String PAGE;
        public String ISBN;
        public String APPEND_INFO;
        public String LOCA;
        public String LOCA_NAME;
        public String CREATE_DATE;
    }
}
