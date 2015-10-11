package com.seoul.publicbooksearcher.data.crawler;

import android.util.Log;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GdLibrary implements BookRepository {

    private static final String TAG = GdLibrary.class.getName();
    private BookRepository cache;

    public GdLibrary(BookRepository cache){
        this.cache = cache;
    }

    @Override
    public List<Book> selectByKeyword(String keyword) {
        List<Book> books = cache.selectByKeyword(keyword);
        try {
            if(books == null) {
                books = getBooks(keyword);
                cache.insertOrUpdateBooks(keyword, books);
            }
            else {
                for (Book book : books)
                    book.setStatusCode(getBookState(book.getBookId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

    private List<Book> getBooks(String keyword) throws IOException {
        List<Book> books = new ArrayList();
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
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String library = element.child(0).text();
            Log.i(TAG, library);

            Element dt = element.child(1).child(0).child(0);
            String title = dt.child(0).child(0).text().replace("\n", "").replace("\r", "").trim();
            Log.i(TAG, title);

            String callNumberAndLocation = dt.parent().child(2).text();
            String callNumber = getCallNumber(callNumberAndLocation);
            String location = getLocation(callNumberAndLocation);

            String bookId = dt.child(1).child(1).attr("href").replaceAll(" ", "").replaceAll("'", "").split(",")[1];
            int statusCode = getBookState(bookId);
            books.add(new Book(title, library, statusCode, bookId, callNumber, location));
        }

        return books;
    }

    private String getCallNumber(String callNumberAndLocation){
        int start = callNumberAndLocation.indexOf(":");
        int end = callNumberAndLocation.lastIndexOf("|");
        return callNumberAndLocation.substring(start, end).trim();
    }
    private String getLocation(String callNumberAndLocation){
        int start = callNumberAndLocation.lastIndexOf(":");
        return callNumberAndLocation.substring(start).trim();
    }

    @Override
    public void insertOrUpdateBooks(String keyword, List<Book> books) {}


    private int getBookState(String bookId) throws IOException {
        int stateCode = Integer.parseInt(Jsoup.connect("http://gdllc.sen.go.kr/wsearch-haveBook.do?book_key=" + bookId).get().text());
        Log.i(TAG, "getBookState result : "+stateCode);
        return stateCode;
    }
}
