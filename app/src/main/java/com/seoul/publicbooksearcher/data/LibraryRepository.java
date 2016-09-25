package com.seoul.publicbooksearcher.data;

import android.content.Context;

import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.models.Library;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class LibraryRepository {

    private List<Library> libraries;

    public LibraryRepository(Context context){
        this.libraries = new ArrayList();
        libraries.add(new Library(Library.SEOUL_LIB_ID, "서울도서관", 37.5662020, 126.9778190, context.getResources().getColor(R.color.서울도서관)));
        libraries.add(new Library(Library.GANGNAM_LIB_ID, "강남도서관", 37.5136910, 127.0470990, context.getResources().getColor(R.color.강남도서관)));
        libraries.add(new Library(Library.GANGDONG_LIB_ID, "강동도서관", 37.5385690, 127.1437600, context.getResources().getColor(R.color.강동도서관)));
        libraries.add(new Library(Library.GANGSEO_LIB_ID, "강서도서관", 37.5478818, 126.8599269, context.getResources().getColor(R.color.강서도서관)));
        libraries.add(new Library(Library.GODEOK_LEARNING_CENTER_ID, "고덕평생학습관", 37.5600000,127.1600000, context.getResources().getColor(R.color.고덕평생학습관)));
        libraries.add(new Library(Library.GOCHEOK_LIB_ID, "고척도서관", 37.5100000,126.8500000, context.getResources().getColor(R.color.고척도서관)));
        libraries.add(new Library(Library.GURO_LIB_ID, "구로도서관", 37.5000000,126.8900000, context.getResources().getColor(R.color.구로도서관)));
        libraries.add(new Library(Library.GAEPO_LIB_ID, "개포도서관", 37.4800000,127.0600000, context.getResources().getColor(R.color.개포도서관)));
        libraries.add(new Library(Library.NAMSAN_LIB_ID, "남산도서관", 37.5500000, 126.9800000, context.getResources().getColor(R.color.남산도서관)));
        libraries.add(new Library(Library.NOWON_LEARNING_CENTER_ID, "노원평생학습관", 37.6400000,127.0700000, context.getResources().getColor(R.color.노원평생학습관)));
        libraries.add(new Library(Library.DONGDAEMUN_LIB_ID, "동대문도서관", 37.5700000, 127.0200000, context.getResources().getColor(R.color.동대문도서관)));
        libraries.add(new Library(Library.DOBONG_LIB_ID, "도봉도서관", 37.6500000, 127.0100000, context.getResources().getColor(R.color.도봉도서관)));
        libraries.add(new Library(Library.DONGJAK_LIB_ID, "동작도서관", 37.5100000, 126.9400000, context.getResources().getColor(R.color.동작도서관)));
        libraries.add(new Library(Library.MAPO_LEARNING_CENTER_ID, "마포평생학습관", 37.5500000, 126.9200000, context.getResources().getColor(R.color.마포평생학습관)));
        libraries.add(new Library(Library.MAPO_AHYUN_BRANCH_ID, "마포평생아현분관", 37.5500000,126.9600000, context.getResources().getColor(R.color.마포평생아현분관)));
        libraries.add(new Library(Library.SEODAEMUN_LIB_ID, "서대문도서관", 37.5800000,126.9400000, context.getResources().getColor(R.color.서대문도서관)));
        libraries.add(new Library(Library.SONGPA_LIB_ID, "송파도서관", 37.5000000, 127.1300000, context.getResources().getColor(R.color.송파도서관)));
        libraries.add(new Library(Library.YANGCHEON_LIB_ID, "양천도서관", 37.5300000, 126.8800000, context.getResources().getColor(R.color.양천도서관)));
        libraries.add(new Library(Library.SEOULCHILD_LIB_ID, "서울시립어린이도서관", 37.5800000, 126.9700000, context.getResources().getColor(R.color.서울시립어린이도서관)));
        libraries.add(new Library(Library.YEONGDEUNGO_LEARNING_CENTER, "영등포평생학습관", 37.5260740, 126.9073520, context.getResources().getColor(R.color.영등포평생학습관)));
        libraries.add(new Library(Library.YONGSAN_LIB_ID, "용산도서관", 37.5500000, 126.9800000, context.getResources().getColor(R.color.용산도서관)));
        libraries.add(new Library(Library.JUNGDOK_LIB_ID, "정독도서관", 37.5800000, 126.9800000, context.getResources().getColor(R.color.정독도서관)));
        libraries.add(new Library(Library.JONGRO_LIB_ID, "종로도서관", 37.5800000, 126.9700000, context.getResources().getColor(R.color.종로도서관)));
    }

    public List<Library> selectAll(){
        return libraries;
    }
}
