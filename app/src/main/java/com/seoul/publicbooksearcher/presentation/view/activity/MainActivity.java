package com.seoul.publicbooksearcher.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.seoul.publicbooksearcher.Utils;
import com.seoul.publicbooksearcher.domain.models.Book;
import com.seoul.publicbooksearcher.presentation.presenter.BookPresenter;
import com.seoul.publicbooksearcher.presentation.view.MainView;
import com.seoul.publicbooksearcher.presentation.view.component.ActionBarProgressBarView;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class MainActivity extends AppCompatActivity implements MainView, LocationListener {

    private final static String TAG = MainActivity.class.getName();
    public static final int REQUEST_GPS_LOCATION = 1;
    public static final int REMOVE_GPS_UPDATES = 2;

    @Bean(BookPresenter.class)
    BookPresenter mBookPresenter;

    @Bean(BookTitleAutoCompleteTextView.class)
    BookTitleAutoCompleteTextView mBookTitleAutoCompleteTextView;


    @Bean(BookListView.class)
    BookListView mBookListView;

    private ActionBarProgressBarView mActionBarProgressBarView;
    private LocationManager mLocationManager;
    private boolean mIsFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBookPresenter.setMainView(this);
        mBookTitleAutoCompleteTextView.setBookPresenter(mBookPresenter);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(isGPSEnabled) {
                onPreSortLibraries();
                sortLibraries();
            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("위치 서비스 사용")
                        .setMessage("위치 정보를 사용하려면, 단말기의 설정에서 '위치 서비스' 사용을 허용해주세요.")
                        .setPositiveButton("설정하기", (dialog, unusedId) -> {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialog.dismiss();
                        })
                        .setNegativeButton("취소", (dialog, unusedId) -> dialog.cancel()).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_GPS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) sortLibraries();
                else onErrorSortLibraries();
                return;
            }
            case REMOVE_GPS_UPDATES:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) removeGPSUpdates();
                else onErrorSortLibraries();
                return;
            }
        }
    }

    private void onPreSortLibraries() {
        mActionBarProgressBarView.setSortActionButtonState(true);
    }

    private void sortLibraries() {
        mIsFound = false;
        Utils.checkLocationsPermission(this, REQUEST_GPS_LOCATION, () -> {
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location !=null)
                onPostSortLibraries(location);
            else {
                Timer timer = new Timer();
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!mIsFound) // 만약에 GPS로 못찾았다면
                            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, MainActivity.this);
                    }
                }, 2000);
            }
            Log.i(TAG, "request location with GPS !!");
        });
    }

    private void onPostSortLibraries(Location location) {
        mActionBarProgressBarView.setSortActionButtonState(false);
        mBookListView.sort(new com.seoul.publicbooksearcher.domain.models.Location(location.getLatitude(), location.getLongitude()));
        mIsFound = true;
        removeGPSUpdates();
    }

    private void onErrorSortLibraries() {
        Toast.makeText(this, "GPS 권한을 획득하지 못했습니다. 가까운 도서관을 확인하을 위해 GPS 권한을 설정해주세요.", Toast.LENGTH_LONG).show();
        mActionBarProgressBarView.setSortActionButtonState(false);
    }

    private void removeGPSUpdates() {
        Utils.checkLocationsPermission(this, REMOVE_GPS_UPDATES,()->mLocationManager.removeUpdates(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //((BookListViewAdapter) listView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
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
        mBookListView.collapseAllParents();
        mBookListView.clearLibrary(libraryId);
        mBookListView.progressVisible(libraryId);
        mBookListView.hideKeyboard();
    }

    @Override
    public void onPostSearchBooks(Long libraryId, List<Book> books) {
        mBookListView.updateLibrary(libraryId, books);
    }

    @Override
    public void onErrorSearchBooks(Long libraryId, String message) {
        mBookListView.showError(libraryId, message);
    }
    @Override
    public void onLocationChanged(android.location.Location location) {
        onPostSortLibraries(location);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBookPresenter.destroy();
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
