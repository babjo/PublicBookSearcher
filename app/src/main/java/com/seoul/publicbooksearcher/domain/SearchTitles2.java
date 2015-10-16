package com.seoul.publicbooksearcher.domain;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTitles2 implements AsyncUseCase<String> {

    private final static String TAG = SearchTitles2.class.getName();

    private final BookRepository bookRepository;
    private AsyncUseCaseListener asyncUseCaseListener;
    private NaverAsyncTask naverAsyncTask;

    public SearchTitles2(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(String keyword, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        if(naverAsyncTask != null)
            naverAsyncTask.cancel(true);
        naverAsyncTask = new NaverAsyncTask();
        naverAsyncTask.execute(keyword);
    }

    private class NaverAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchTitles2.this.bookRepository.selectByKeyword(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);

            List<String> titles = new ArrayList();
            for (Book book : books) {
                titles.add(book.getTitle());
            }

            asyncUseCaseListener.onAfter(titles);
        }
    }
}
