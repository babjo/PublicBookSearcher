package com.seoul.publicbooksearcher.presentation.presenter;

import android.text.Html;
import android.util.Log;

import com.seoul.publicbooksearcher.domain.models.Keyword;
import com.seoul.publicbooksearcher.domain.models.NullSubscriber;
import com.seoul.publicbooksearcher.domain.dto.AddRecentKeywordRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsResponseDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchBooksRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchBooksResponseDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchTitlesRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchTitlesResponseDTO;
import com.seoul.publicbooksearcher.domain.exception.BookSearchException;
import com.seoul.publicbooksearcher.domain.usecase.AddRecentKeywordUseCase;
import com.seoul.publicbooksearcher.domain.usecase.GetRecentKeywordsUseCase;
import com.seoul.publicbooksearcher.domain.usecase.SearchBooksUseCase;
import com.seoul.publicbooksearcher.domain.usecase.SearchTitlesUseCase;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawler;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawlerCollection;
import com.seoul.publicbooksearcher.presentation.view.activity.MainActivity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

@EBean
public class BookPresenter implements Presenter{

    private final static String TAG = BookPresenter.class.getName();

    @Bean(BookCrawlerCollection.class)
    BookCrawlerCollection bookCrawlerCollection;

    @Bean(GetRecentKeywordsUseCase.class)
    UseCase<GetRecentKeywordsRequestDTO> mGetRecentKeywordsUseCase;

    @Bean(AddRecentKeywordUseCase.class)
    UseCase<AddRecentKeywordRequestDTO> mAddRecentKeyword;

    @Bean(SearchBooksUseCase.class)
    UseCase<SearchBooksRequestDTO> mSearchBooksUseCase;

    @Bean(SearchTitlesUseCase.class)
    UseCase<SearchTitlesRequestDTO> mSearchTitlesUseCase;

    private MainActivity mMainView;

    public void setMainView(MainActivity mMainView) {
        this.mMainView = mMainView;
    }

    public void getRecentKeywords() {
        mGetRecentKeywordsUseCase.execute(new GetRecentKeywordsRequestDTO(), new Subscriber<GetRecentKeywordsResponseDTO>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(GetRecentKeywordsResponseDTO getRecentKeywordsResponseDTO) {
                String keywordContents = "";
                for (String keyword : getRecentKeywordsResponseDTO.getRecentKeywords())
                    keywordContents += keyword+ ", ";
                Log.i(TAG, "Recent Keyword : " + keywordContents);
                mMainView.onLoadRecentKeywords(getRecentKeywordsResponseDTO.getRecentKeywords());
            }
        });
    }

    public void searchTitles(final String keyword){
        mSearchTitlesUseCase.execute(new SearchTitlesRequestDTO(keyword), new Subscriber<SearchTitlesResponseDTO>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(SearchTitlesResponseDTO searchTitlesResponseDTO) {
                Log.i("UPDATE", "3");

                List highlighted = new ArrayList();
                for(String title : searchTitlesResponseDTO.getTitles())
                    highlighted.add(highlight(title));
                mMainView.onLoadRecentKeywords(highlighted);
            }
            private Object highlight(String title) {
                return Html.fromHtml(title.replace(keyword, "<font color=\"red\">"+keyword+"</font>"));
            }
        });
    }

    public void searchBooks(String keyword) {
        Log.i(TAG, "entered keyword = " + keyword + "search start");
        mAddRecentKeyword.execute(new AddRecentKeywordRequestDTO(keyword), new NullSubscriber());
        Log.i(TAG, "mAddRecentKeyword = " + keyword);

        for (BookCrawler bookCrawler : bookCrawlerCollection.get()){
            mMainView.onPreSearchBooks(bookCrawler.getLibraryId());
            mSearchBooksUseCase.execute(new SearchBooksRequestDTO(keyword, bookCrawler), new Subscriber<SearchBooksResponseDTO>() {
                @Override
                public void onCompleted() {
                }
                @Override
                public void onError(Throwable e) {
                    if(e instanceof BookSearchException)
                        mMainView.onErrorSearchBooks(((BookSearchException) e).getLibraryId(), e.getMessage());
                }
                @Override
                public void onNext(SearchBooksResponseDTO searchBooksResponseDTO) {
                    mMainView.onPostSearchBooks(searchBooksResponseDTO.getSearchResult().getLibraryId(), searchBooksResponseDTO.getSearchResult().getBooks());
                }
            });
        }
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        mMainView = null;
        mGetRecentKeywordsUseCase.unsubscribe();
        mSearchBooksUseCase.unsubscribe();
        mSearchTitlesUseCase.unsubscribe();
        mAddRecentKeyword.unsubscribe();
    }
}
