package com.seoul.publicbooksearcher.presentation.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.seoul.publicbooksearcher.domain.Location;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.domain.async_usecase.AsyncUseCase;
import com.seoul.publicbooksearcher.domain.async_usecase.GetRecentKeywords;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchBooks;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchTitles;
import com.seoul.publicbooksearcher.domain.async_usecase.SortLibraries;
import com.seoul.publicbooksearcher.domain.exception.BookSearchException;
import com.seoul.publicbooksearcher.domain.exception.CantNotKnowLocationException;
import com.seoul.publicbooksearcher.domain.exception.NotGpsSettingsException;
import com.seoul.publicbooksearcher.domain.usecase.AddRecentKeyword;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.component.ActionBarProgressBarView;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class BookPresenter {

    private Context context;
    private final static String TAG = BookPresenter.class.getName();

    @Bean(GetRecentKeywords.class)
    AsyncUseCase getRecentKeywords;

    @Bean(AddRecentKeyword.class)
    UseCase addRecentKeyword;

    @Bean(SearchBooks.class)
    AsyncUseCase searchBooks;

    @Bean(SearchTitles.class)
    AsyncUseCase searchTitles;

    @Bean(SortLibraries.class)
    AsyncUseCase sortLibraries;

    @Bean(BookTitleAutoCompleteTextView.class)
    BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView;

    @Bean(BookListView.class)
    BookListView bookListView;

    private ActionBarProgressBarView actionBarProgressBarView;
    private Activity activity;

    public BookPresenter(Context context) {
        this.context = context;
    }

    @AfterInject
    public void init(){
        bookTitleAutoCompleteTextView.setBookPresenter(this);
    }

    // 의존성 주입
    public void setActionBarProgressBarView(ActionBarProgressBarView actionBarProgressBarView) {
        this.actionBarProgressBarView = actionBarProgressBarView;
    }
    public void setActivityToAd(Activity activity) {
        this.activity = activity;
    }
    private static final String APP_CODE = "gclbRPXF";
    public void showAd() {
        // CaulyAdInfo 생성
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).effect("BottomSlide").build();
        // 전면 광고 생성
        CaulyInterstitialAd interstial = new CaulyInterstitialAd();
        interstial.setAdInfo(adInfo);
        interstial.setInterstialAdListener(new CaulyInterstitialAdListener() {
            @Override
            public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean isChargeableAd) {
                // 광고 수신 성공한 경우 호출됨.
                // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
                if (isChargeableAd == false) {
                    Log.d("CaulyExample", "free interstitial AD received.");
                }
                else {
                    Log.d("CaulyExample", "normal interstitial AD received.");
                }

                ad.show();
            }

            @Override
            public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, int i, String s) {
            }

            @Override
            public void onClosedInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
            }

            @Override
            public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
            }
        });
        // 전면광고 노출 후 back 버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
        // 단, requestInterstitialAd 위에서 추가되어야 합니다.
        // interstitialAd.disableBackKey();
        // 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
        interstial.requestInterstitialAd(activity);
    }

    public void getRecentKeywords() {
        getRecentKeywords.execute(null, new AsyncUseCaseListener<Void, List<String>, Exception>() {
            @Override
            public void onBefore(Void beforeArgs) {
            }
            @Override
            public void onAfter(List<String> keywords) {
                String keywordContents = "";
                for (String keyword : keywords)
                    keywordContents += keyword + ", ";
                Log.i(TAG, "Recent Keyword : " + keywordContents);
                BookPresenter.this.bookTitleAutoCompleteTextView.setTitles(keywords);
            }
            @Override
            public void onError(Exception e) {

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

                    bookTitleAutoCompleteTextView.setTitles(highlighted);
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

        //showAd();
        Log.i(TAG, "entered keyword = " + keyword + "search start");
        addRecentKeyword.execute(keyword);
        Log.i(TAG, "addRecentKeyword = " + keyword);

        searchBooks.execute(keyword, new AsyncUseCaseListener<Long, SearchResult, BookSearchException>() {
            @Override
            public void onBefore(final Long libraryId) {
                bookTitleAutoCompleteTextView.dismissDropDown();
                bookTitleAutoCompleteTextView.clearFocus();

                new Handler().post(new Runnable() { // new Handler and Runnable
                    @Override
                    public void run() {
                        bookListView.collapseAllParents();
                        bookListView.clearLibrary(libraryId);
                    }
                });

                bookListView.progressVisible(libraryId);
                bookListView.hideKeyboard();
            }

            @Override
            public void onAfter(SearchResult searchResult) {
                bookListView.updateLibrary(searchResult.getLibraryId(), searchResult.getBooks());
            }

            @Override
            public void onError(BookSearchException e) {
                bookListView.showError(e.getLibraryId(), e.getMessage());
            }
        });

    }

    public void sortLibraries() {
        final Handler handler = new Handler(Looper.getMainLooper());
        sortLibraries.execute(null, new AsyncUseCaseListener<Void, Location, RuntimeException>() {
            @Override
            public void onBefore(Void beforeArgs) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        actionBarProgressBarView.setSortActionButtonState(true);
                    }
                });
            }

            @Override
            public void onAfter(Location location) {
                Log.i(TAG, "=========== current location : " + location.latitude + ", " + location.longitude);
                bookListView.sort(location);
                hideActionBarProgressBar();

            }

            private void hideActionBarProgressBar() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        actionBarProgressBarView.setSortActionButtonState(false);
                    }
                });
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
                hideActionBarProgressBar();
                if (e instanceof NotGpsSettingsException) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("위치 서비스 사용")
                            .setMessage("위치 정보를 사용하려면, 단말기의 설정에서 '위치 서비스' 사용을 허용해주세요.")
                            .setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else if (e instanceof CantNotKnowLocationException) {
                    Log.i(TAG, "일시적 에러로 사용자 위치를 알 수 없습니다.");
                    Toast.makeText(context, "일시적 에러로 사용자 위치를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
