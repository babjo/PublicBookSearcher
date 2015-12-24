package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.infrastructure.crawler.title.BandinlunisAutoCompleteCrawler;
import com.seoul.publicbooksearcher.infrastructure.crawler.title.TitleCrawler;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class SearchTitles implements AsyncUseCase<String> {

    private final static String TAG = SearchTitles.class.getName();

    @Bean(BandinlunisAutoCompleteCrawler.class)
    TitleCrawler autoCompleteCrawler;

    private AsyncUseCaseListener asyncUseCaseListener;
    private BandiAsyncTask naverAsyncTask;


    @Override
    public void execute(String keyword, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        try {
            if(naverAsyncTask != null)
                naverAsyncTask.cancel(true);
            naverAsyncTask = new BandiAsyncTask();
            naverAsyncTask.execute(keyword);
        }catch (Exception e){
            asyncUseCaseListener.onError(e);
        }
    }

    private class BandiAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                return autoCompleteCrawler.crawling(params[0]);
            }catch (Exception e){
                asyncUseCaseListener.onError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> titles) {
            super.onPostExecute(titles);

            if(titles != null) {
                asyncUseCaseListener.onAfter(titles);
            }
        }
    }
}
