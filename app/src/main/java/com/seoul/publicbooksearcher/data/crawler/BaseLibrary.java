package com.seoul.publicbooksearcher.data.crawler;

import android.util.Log;

import com.seoul.publicbooksearcher.data.BaseBookRepository;
import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseLibrary extends BaseBookRepository {

    private static final String TAG = BaseLibrary.class.getName();
    protected BookRepository cache;
    private String libraryId;

    public BaseLibrary(BookRepository cache, String libraryId){
        this.cache = cache;
        this.libraryId = libraryId;
    }

    public String getLibraryId(){
        return libraryId;
    }

    @Override
    public List<Book> selectByKeyword(String keyword) {
        List<Book> books = cache.selectByKeywordAndLibrary(keyword, libraryId);
        try {
            if(books == null) {
                books = getBooks(keyword);
                cache.insertOrUpdateBooks(keyword, getLibraryId(), books);
            }
            else {
                for (Book book : books)
                    book.setStatusCode(getBookState(book.getBookId()));
            }
        } catch (IOException e) {
            throw new RuntimeException(TAG+" : IOException");
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
                .data("locExquery", getLibraryId())
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

            String writerAndPublicationAndYear = dt.parent().select("dd.summary").get(0).text();

            String writer = getWriter(writerAndPublicationAndYear);
            String publication = getPublication(writerAndPublicationAndYear);

            String callNumberAndLocation = dt.parent().select("dd.root").get(0).text();
            Log.i(TAG, callNumberAndLocation);
            String location = getLocation(callNumberAndLocation);
            Log.i(TAG, location);
            String callNumber = getCallNumber(callNumberAndLocation);
            Log.i(TAG, callNumber);

            String bookId = dt.child(1).child(1).attr("href").replaceAll(" ", "").replaceAll("'", "").split(",")[1];
            int statusCode = getBookState(bookId);
            books.add(new Book(title, library, publication, writer, statusCode, bookId, callNumber, location));
        }

        return books;
    }

    private String getPublication(String writerAndPublicationAndYear) {
        return extract(writerAndPublicationAndYear, 1);
    }

    private String getWriter(String writerAndPublicationAndYear) {
        return extract(writerAndPublicationAndYear, 0);
    }

    private String getCallNumber(String callNumberAndLocation){
        /*
        String callNumber = callNumberAndLocation.split("[|]")[0];
        int start = callNumber.lastIndexOf(":");
        return callNumber.substring(start+1).trim();*/
        return extract(callNumberAndLocation, 0);
    }
    private String getLocation(String callNumberAndLocation){
        /*
        String location = callNumberAndLocation.split("[|]")[1];
        int start = location.lastIndexOf(":");
        return location.substring(start+1).trim();*/
        return extract(callNumberAndLocation, 1);
    }

    private String extract(String str, int index){
        String callNumber = str.split("[|]")[index];
        int start = callNumber.lastIndexOf(":");
        return callNumber.substring(start+1).trim();
    }

    private int getBookState(String bookId) throws IOException {
        int stateCode = Integer.parseInt(Jsoup.connect("http://gdllc.sen.go.kr/wsearch-haveBook.do?book_key=" + bookId).get().text());
        Log.i(TAG, "getBookState result : "+stateCode);
        return stateCode;
    }
}
