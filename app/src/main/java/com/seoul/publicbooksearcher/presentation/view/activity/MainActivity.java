package com.seoul.publicbooksearcher.presentation.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.seoul.publicbooksearcher.Const;
import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.Location;
import com.seoul.publicbooksearcher.presentation.presenter.BookPresenter;
import com.seoul.publicbooksearcher.presentation.view.MainView;
import com.seoul.publicbooksearcher.presentation.view.component.ActionBarProgressBarView;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class MainActivity extends AppCompatActivity implements MainView{

    private final static String TAG = MainActivity.class.getName();

    @Bean(BookPresenter.class)
    BookPresenter mBookPresenter;

    @Bean(BookTitleAutoCompleteTextView.class)
    BookTitleAutoCompleteTextView mBookTitleAutoCompleteTextView;


    @Bean(BookListView.class)
    BookListView mBookListView;

    private ActionBarProgressBarView mActionBarProgressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBookPresenter.setMainView(this);
        mBookTitleAutoCompleteTextView.setBookPresenter(mBookPresenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mActionBarProgressBarView = new ActionBarProgressBarView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            mBookPresenter.sortLibrariesByDistance();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //((BookListViewAdapter) listView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);

    @Override
    public void onLoadRecentKeywords(List<String> keywords) {
        mBookTitleAutoCompleteTextView.setTitles(keywords);
    }

    @Override
    public void onPreSearchBooks(long libraryId) {
        mBookTitleAutoCompleteTextView.dismissDropDown();
        mBookTitleAutoCompleteTextView.clearFocus();

        new Handler().post(new Runnable() { // new Handler and Runnable
            @Override
            public void run() {
                mBookListView.collapseAllParents();
                mBookListView.clearLibrary(libraryId);
            }
        });

        mBookListView.progressVisible(libraryId);
        mBookListView.hideKeyboard();
    }

    @Override
    public void onPreSortLibrariesByDistance() {
        mActionBarProgressBarView.setSortActionButtonState(true);
    }

    @Override
    public void onPostSearchBooks(Long libraryId, List<Book> books) {
        mBookListView.updateLibrary(libraryId, books);
    }

    @Override
    public void onPostSortLibrariesByDistance(Location location) {
        mActionBarProgressBarView.setSortActionButtonState(false);
        mBookListView.sort(location);
    }

    @Override
    public void onErrorSortLibrariesByDistance() {
        mActionBarProgressBarView.setSortActionButtonState(false);
    }


    @Override
    public void onErrorSearchBooks(Long libraryId, String message) {
        mBookListView.showError(libraryId, message);
    }

    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void showAd() {
        // CaulyAdInfo 생성
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(Const.APP_CODE).effect("BottomSlide").build();
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
        interstial.requestInterstitialAd(this);
    }
}
