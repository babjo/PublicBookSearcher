package com.seoul.publicbooksearcher.domain.usecase;

import com.seoul.publicbooksearcher.data.KeywordRepository;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsResponseDTO;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import rx.Observable;

/**
 * Created by LCH on 2016. 9. 23..
 */

@EBean
public class GetRecentKeywordsUseCase extends UseCase<GetRecentKeywordsRequestDTO> {

    @Bean(RecentSearchKeywordRepository.class)
    KeywordRepository mKeywordRepository;

    @Override
    protected Observable buildUseCaseObservable(GetRecentKeywordsRequestDTO getRecentKeywordsRequestDTO) {
        return Observable.create(subscriber -> {
            try {
                List<String> recentKeywords = mKeywordRepository.selectAll();
                subscriber.onNext(new GetRecentKeywordsResponseDTO(recentKeywords));
                subscriber.onCompleted();
            }catch (Exception e){
                subscriber.onError(e);
            }finally {
                subscriber.onCompleted();
            }
        });
    }
}
