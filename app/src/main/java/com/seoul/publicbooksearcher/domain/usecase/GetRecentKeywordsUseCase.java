package com.seoul.publicbooksearcher.domain.usecase;

import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsResponseDTO;
import com.seoul.publicbooksearcher.domain.models.KeywordEntity;
import com.seoul.publicbooksearcher.infrastructure.Requery;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import io.requery.query.Tuple;
import rx.Observable;

/**
 * Created by LCH on 2016. 9. 23..
 */

@EBean
public class GetRecentKeywordsUseCase extends UseCase<GetRecentKeywordsRequestDTO> {

    @Bean
    Requery mRequery;

    @Override
    protected Observable buildUseCaseObservable(GetRecentKeywordsRequestDTO getRecentKeywordsRequestDTO) {
        return Observable.create(subscriber -> {
            try {
                List<Tuple> results = mRequery.getData().select(KeywordEntity.VALUE).distinct().orderBy(KeywordEntity.CREATE_AT.desc()).get().toList();
                List<String> keywords = new ArrayList();
                for (Tuple result : results) keywords.add(result.get(0));
                subscriber.onNext(new GetRecentKeywordsResponseDTO(keywords));
                subscriber.onCompleted();
            }catch (Exception e){
                subscriber.onError(e);
            }finally {
                subscriber.onCompleted();
            }
        });
    }
}
