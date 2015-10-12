package com.seoul.publicbooksearcher.domain;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.data.open_api.NaverBookOpenApi;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTitles implements UseCase <Void, String> {

    private final static String TAG = SearchTitles.class.getName();

    private final BookRepository bookRepository;
    private final SearchTitlesListener searchTitlesListener;
    private NaverAsyncTask naverAsyncTask;

    public SearchTitles(SearchTitlesListener searchTitlesListener){
        this.searchTitlesListener = searchTitlesListener;
        this.bookRepository = new NaverBookOpenApi();
    }

    @Override
    public Void execute(String keyword) {
        if(naverAsyncTask != null)
            naverAsyncTask.cancel(true);
        naverAsyncTask = new NaverAsyncTask();
        naverAsyncTask.execute(keyword);

        return null;
    }

    private class NaverAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchTitles.this.bookRepository.selectByKeyword(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);

            List<String> titles = new ArrayList();
            for (Book book : books) {
                titles.add(book.getTitle());
            }

            searchTitlesListener.searchCompleted(titles);
        }
    }
}
