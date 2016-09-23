package com.seoul.publicbooksearcher.domain.usecase;

import com.seoul.publicbooksearcher.domain.dto.SearchTitlesRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchTitlesResponseDTO;
import com.seoul.publicbooksearcher.infrastructure.crawler.title.BandinlunisAutoCompleteCrawler;
import com.seoul.publicbooksearcher.infrastructure.crawler.title.TitleCrawler;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import rx.Observable;

/**
 * Created by LCH on 2016. 9. 23..
 */

@EBean
public class SearchTitlesUseCase extends UseCase<SearchTitlesRequestDTO> {

    @Bean(BandinlunisAutoCompleteCrawler.class)
    TitleCrawler mAutoCompleteCrawler;

    @Override
    protected Observable buildUseCaseObservable(SearchTitlesRequestDTO searchTitlesRequestDTO) {
        unsubscribe();
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(new SearchTitlesResponseDTO(mAutoCompleteCrawler.crawling(searchTitlesRequestDTO.getKeyword())));
            }catch (Exception e){
                subscriber.onError(e);
            }finally {
                subscriber.onCompleted();
            }
        });
    }
}
