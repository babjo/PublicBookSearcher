package com.seoul.publicbooksearcher.domain;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.presentation.UseCaseListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTitles implements UseCase <Void, String> {

    private final static String TAG = SearchTitles.class.getName();

    private final BookRepository bookRepository;
    private final UseCaseListener searchTitlesListener;
    private NaverAsyncTask naverAsyncTask;

    public SearchTitles(UseCaseListener searchTitlesListener, BookRepository bookRepository){
        this.searchTitlesListener = searchTitlesListener;
        this.bookRepository = bookRepository;
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

            searchTitlesListener.executeAfter(titles);
        }
    }
}
