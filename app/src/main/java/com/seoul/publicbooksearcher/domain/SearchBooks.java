package com.seoul.publicbooksearcher.domain;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.seoul.publicbooksearcher.presentation.listener.SearchBooksListener;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchBooks implements UseCase <Void, String> {

    private static final String TAG = SearchBooks.class.getName();
    private final SearchBooksListener searchBooksListener;
    private final SearchTitlesListener searchTitlesListener;

    public SearchBooks(SearchBooksListener searchBooksListener, SearchTitlesListener searchTitlesListener){
        this.searchBooksListener = searchBooksListener;
        this.searchTitlesListener = searchTitlesListener;
    }

    @Override
    public Void execute(String keyword) {
        new LibraryAsyncTask().execute(keyword);
        return null;
    }

    private class LibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBooksListener.searchBefore();
            searchTitlesListener.searchBefore();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return getBooks(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            searchBooksListener.searchCompleted(books);
        }
    }

    @NonNull
    private List<Book> getBooks(String keyword) {
        List<Book> books = new ArrayList();

        try {
            Document doc = Jsoup.connect("http://gdllc.sen.go.kr/wsearch-total.do")
                    .data("site_id", "GO")
                    .data("startCount", "0")
                    .data("sort", "RANK")
                    .data("collection", "sen_library")
                    .data("range", "A")
                    .data("searchField", "ALL")
                    .data("locExquery", "111003,111004,111005,111007,111008,111009,111006,111010,111022,111012,111011,111013,111014,111031,111016,111030,111015,111017,111018,111019,111020,111021")
                    .data("exquery_field", "MAT_CODE_NUM")
                    .data("realQuery", keyword + "|" + keyword)
                    .data("detailView", "0")
                    .data("query", keyword)
                    .post();

            Elements elements = doc.select("ul.resultsty1");
            for(int i=0; i<elements.size(); i++){
                Element element = elements.get(i);
                String library = element.child(0).text();
                Log.i(TAG, library);

                Element dt = element.child(1).child(0).child(0);
                String title = dt.child(0).child(0).text();
                Log.i(TAG, title);

                String bookId = dt.child(1).child(1).attr("href").replaceAll(" ", "").replaceAll("'", "").split(",")[1];
                String statusCode = Jsoup.connect("http://gdllc.sen.go.kr/wsearch-haveBook.do?book_key="+bookId).get().text();
                Log.i(TAG, statusCode);

                books.add(new Book(title, library, Integer.parseInt(statusCode)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }
}
