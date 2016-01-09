package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.KeywordRepository;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class GetRecentKeywords implements AsyncUseCase<Void> {

    @Bean(RecentSearchKeywordRepository.class)
    KeywordRepository keywordRepository;
    private AsyncUseCaseListener asyncUseCaseListener;

    @Override
    public void execute(Void arg, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        new Task().execute();
    }

    private class Task extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                return keywordRepository.selectAll();
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
