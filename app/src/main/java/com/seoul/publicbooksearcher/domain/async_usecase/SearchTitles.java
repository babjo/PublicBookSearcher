package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTitles implements AsyncUseCase<String> {

    private final static String TAG = SearchTitles.class.getName();

    private final BookRepository bookRepository;
    private AsyncUseCaseListener asyncUseCaseListener;
    private NaverAsyncTask naverAsyncTask;

    public SearchTitles(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(String keyword, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        try {
            if(naverAsyncTask != null)
                naverAsyncTask.cancel(true);
            naverAsyncTask = new NaverAsyncTask();
            naverAsyncTask.execute(keyword);
        }catch (Exception e){
            asyncUseCaseListener.onError(e);
        }
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

            asyncUseCaseListener.onAfter(titles);
        }
    }
}
