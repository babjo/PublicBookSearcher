package com.seoul.publicbooksearcher.domain.usecase;

import com.seoul.publicbooksearcher.domain.models.Library;
import com.seoul.publicbooksearcher.domain.models.SearchResult;
import com.seoul.publicbooksearcher.domain.dto.SearchBooksRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchBooksResponseDTO;
import com.seoul.publicbooksearcher.domain.exception.BookSearchException;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawler;

import org.androidannotations.annotations.EBean;

import rx.Observable;

@EBean
public class SearchBooksUseCase extends UseCase<SearchBooksRequestDTO> {

    private static final String TAG = SearchBooksUseCase.class.getName();

    @Override
    protected Observable buildUseCaseObservable(SearchBooksRequestDTO searchBooksRequestDTO) {
        return Observable.create(subscriber -> {
            BookCrawler bookCrawler = searchBooksRequestDTO.getBookCrawler();
                try {
                    subscriber.onNext(new SearchBooksResponseDTO(new SearchResult(new Library(bookCrawler.getLibraryId(), bookCrawler.crawling(searchBooksRequestDTO.getKeyword())))));
                } catch (Exception e){
                    subscriber.onError(new BookSearchException(e.getMessage(), bookCrawler.getLibraryId()));
                } finally {
                    subscriber.onCompleted();
                }
        }).retry(2);
    }
}

