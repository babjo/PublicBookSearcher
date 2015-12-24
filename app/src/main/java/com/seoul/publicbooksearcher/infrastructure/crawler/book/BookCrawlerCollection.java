package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */

@EBean
public class BookCrawlerCollection {


    public BookCrawler[] get() {
        return new BookCrawler[]{
                seoulLibraryBookCrawler,
                gangnamLibraryBookCrawler,
                gangdongLibraryBookCrawler,
                gangseoLibraryBookCrawler,
                godeokLifelongLearningCenterBookCrawler,
                gocheokLibraryBookCrawler,
                guroLibraryBookCrawler,
                gaepoLibraryBookCrawler,
                namsanLibraryBookCrawler,
                nowonLifelongLearningCenterBookCrawler,
                dongdaemunLibraryBookCrawler,
                dobongLibraryBookCrawler,
                dongjakLibraryBookCrawler,
                mapoLifelongLearningBookCrawler,
                mapoLifelongAhyunBranchBookCrawler,
                seodaemunLibraryBookCrawler,
                songpaLibraryBookCrawler,
                yangcheonLibraryBookCrawler,
                seoulChildrenLibraryBookCrawler,
                yeongdeungpoLifelongLearningCenterBookCrawler,
                yongsanLibraryBookCrawler,
                jungdokLibraryBookCrawler,
                jongRoLibraryBookCrawler
        };
    }

    @Bean(SeoulLibraryBookCrawler.class)
    BookCrawler seoulLibraryBookCrawler;

    @Bean(GangnamLibraryBookCrawler.class)
    BookCrawler gangnamLibraryBookCrawler;

    @Bean(GangdongLibraryBookCrawler.class)
    BookCrawler gangdongLibraryBookCrawler;

    @Bean(GangseoLibraryBookCrawler.class)
    BookCrawler gangseoLibraryBookCrawler;

    @Bean(GodeokLifelongLearningCenterBookCrawler.class)
    BookCrawler godeokLifelongLearningCenterBookCrawler;

    @Bean(GocheokLibraryBookCrawler.class)
    BookCrawler gocheokLibraryBookCrawler;

    @Bean(GuroLibraryBookCrawler.class)
    BookCrawler guroLibraryBookCrawler;

    @Bean(GaepoLibraryBookCrawler.class)
    BookCrawler gaepoLibraryBookCrawler;

    @Bean(NamsanLibraryBookCrawler.class)
    BookCrawler namsanLibraryBookCrawler;

    @Bean(NowonLifelongLearningCenterBookCrawler.class)
    BookCrawler nowonLifelongLearningCenterBookCrawler;

    @Bean(DongdaemunLibraryBookCrawler.class)
    BookCrawler dongdaemunLibraryBookCrawler;

    @Bean(DobongLibraryBookCrawler.class)
    BookCrawler dobongLibraryBookCrawler;

    @Bean(DongjakLibraryBookCrawler.class)
    BookCrawler dongjakLibraryBookCrawler;

    @Bean(MapoLifelongLearningBookCrawler.class)
    BookCrawler mapoLifelongLearningBookCrawler;

    @Bean(MapoLifelongAhyunBranchBookCrawler.class)
    BookCrawler mapoLifelongAhyunBranchBookCrawler;

    @Bean(SeodaemunLibraryBookCrawler.class)
    BookCrawler seodaemunLibraryBookCrawler;

    @Bean(SongpaLibraryBookCrawler.class)
    BookCrawler songpaLibraryBookCrawler;

    @Bean(YangcheonLibraryBookCrawler.class)
    BookCrawler yangcheonLibraryBookCrawler;

    @Bean(SeoulChildrenLibraryBookCrawler.class)
    BookCrawler seoulChildrenLibraryBookCrawler;

    @Bean(YeongdeungpoLifelongLearningCenterBookCrawler.class)
    BookCrawler yeongdeungpoLifelongLearningCenterBookCrawler;

    @Bean(YongsanLibraryBookCrawler.class)
    BookCrawler yongsanLibraryBookCrawler;

    @Bean(JungdokLibraryBookCrawler.class)
    BookCrawler jungdokLibraryBookCrawler;

    @Bean(JongRoLibraryBookCrawler.class)
    BookCrawler jongRoLibraryBookCrawler;

}
