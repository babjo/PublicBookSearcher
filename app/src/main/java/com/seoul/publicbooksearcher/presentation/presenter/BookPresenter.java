package com.seoul.publicbooksearcher.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.seoul.publicbooksearcher.Const;
import com.seoul.publicbooksearcher.domain.usecase.AddRecentKeywordUseCase;
import com.seoul.publicbooksearcher.domain.usecase.GetRecentKeywordsUseCase;
import com.seoul.publicbooksearcher.domain.Location;
import com.seoul.publicbooksearcher.domain.NullSubscriber;
import com.seoul.publicbooksearcher.domain.usecase.SearchBooksUseCase;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.domain.async_usecase.AsyncUseCase;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchTitles;
import com.seoul.publicbooksearcher.domain.async_usecase.SortLibraries;
import com.seoul.publicbooksearcher.domain.dto.AddRecentKeywordRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsResponseDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchBooksRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.SearchBooksResponseDTO;
import com.seoul.publicbooksearcher.domain.exception.BookSearchException;
import com.seoul.publicbooksearcher.domain.exception.CanNotKnowLocationException;
import com.seoul.publicbooksearcher.domain.exception.NotGpsSettingsException;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawler;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawlerCollection;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.activity.MainActivity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

@EBean
public class BookPresenter implements Presenter{

    private Context context;
    private final static String TAG = BookPresenter.class.getName();

    @Bean(BookCrawlerCollection.class)
    BookCrawlerCollection bookCrawlerCollection;

    @Bean(GetRecentKeywordsUseCase.class)
    UseCase<GetRecentKeywordsRequestDTO> mGetRecentKeywordsUseCase;

    @Bean(AddRecentKeywordUseCase.class)
    UseCase<AddRecentKeywordRequestDTO> addRecentKeyword;

    @Bean(SearchBooksUseCase.class)
    UseCase<SearchBooksRequestDTO> mSearchBooksUseCase;

    @Bean(SearchTitles.class)
    AsyncUseCase searchTitles;

    @Bean(SortLibraries.class)
    AsyncUseCase sortLibraries;

    private MainActivity mMainView;

    public BookPresenter(Context context) {
        this.context = context;
    }

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
                    keywordContents += keyword + ", ";
                Log.i(TAG, "Recent Keyword : " + keywordContents);
                mMainView.onLoadRecentKeywords(getRecentKeywordsResponseDTO.getRecentKeywords());
            }
        });
    }

    public void searchTitles(final String keyword){
        searchTitles.execute(keyword, new AsyncUseCaseListener<Void, List<String>, Exception>() {
            @Override
            public void onBefore(Void beforeArgs) {
            }

            @Override
            public void onAfter(List<String> afterArg) {
                Log.i("UPDATE", "3");

                List highlighted = new ArrayList();
                for(String title : afterArg)
                    highlighted.add(highlight(title));
                mMainView.onLoadRecentKeywords(highlighted);
            }

            private Object highlight(String title) {
                return Html.fromHtml(title.replace(keyword, "<font color=\"red\">"+keyword+"</font>"));
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    public void searchBooks(String keyword) {
        Log.i(TAG, "entered keyword = " + keyword + "search start");
        addRecentKeyword.execute(new AddRecentKeywordRequestDTO(keyword), new NullSubscriber());
        Log.i(TAG, "addRecentKeyword = " + keyword);

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

    public void sortLibrariesByDistance() {
        final Handler handler = new Handler(Looper.getMainLooper());
        sortLibraries.execute(null, new AsyncUseCaseListener<Void, Location, RuntimeException>() {
            @Override
            public void onBefore(Void beforeArgs) {
                mMainView.onPreSortLibrariesByDistance();
            }

            @Override
            public void onAfter(Location location) {
                Log.i(TAG, "=========== current location : " + location.latitude + ", " + location.longitude);
                mMainView.onPostSortLibrariesByDistance(location);
            }

            @Override
            public void onError(final RuntimeException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        _onError(e);
                    }
                });
            }

            private void _onError(RuntimeException e) {
                mMainView.onErrorSortLibrariesByDistance();
                if (e instanceof NotGpsSettingsException) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(Const.GPS_SETTINGS_DIALOG_TITLE)
                            .setMessage(Const.GPS_SETTINGS_DIALOG_MESSAGE)
                            .setPositiveButton(Const.GPS_SETTINGS_DIALOG_POSITIVE_BTN, (dialog, id) -> context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                            .setNegativeButton(Const.GPS_SETTINGS_DIALOG_NEGATIVE_BTN, (dialog, id) -> dialog.dismiss()).show();
                } else if (e instanceof CanNotKnowLocationException) {
                    Log.i(TAG, e.getMessage());
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    }
}
