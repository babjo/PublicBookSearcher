package com.seoul.publicbooksearcher.domain;

import android.app.Service;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.seoul.publicbooksearcher.presentation.listener.SearchBooksListener;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
        new SeoulLibraryAsyncTask().execute(keyword);
        new GdLibraryAsyncTask().execute(keyword);
        return null;
    }

    private class GdLibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBooksListener.searchBefore();
            searchTitlesListener.searchBefore();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return searchGdlibrary(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            searchBooksListener.searchCompleted(books);
        }
    }

    private class SeoulLibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBooksListener.searchBefore();
            searchTitlesListener.searchBefore();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return searchSeoullibrary(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            searchBooksListener.searchCompleted(books);
        }
    }


    private List<Book> searchSeoullibrary(String keyword) {
        List<Book> books = new ArrayList();
        try {

            Document doc = Jsoup.connect("http://lib.seoul.go.kr/search/laz/result?" +
                    "st=KWRD" +
                    "&gubunFlag=" +
                    "&nation_id=" +
                    "&si=1" +
                    "&q="+keyword+
                    "&b0=and" +
                    "&weight0=0.5" +
                    "&si=2" +
                    "&q=" +
                    "&b1=and" +
                    "&weight1=0.5" +
                    "&si=3" +
                    "&q=" +
                    "&weight2=0.5" +
                    "&_lmt0=on" +
                    "&lmtsn=000000000001" +
                    "&lmtst=OR" +
                    "&lmt0=m%3Bzb" +
                    "&_lmt0=on" +
                    "&_lmt0=on" +
                    "&_lmt0=on" +
                    "&_lmt0=on" +
                    "&_lmt0=on" +
                    "&_lmt0=on" +
                    "&inc=TOTAL" +
                    "&_inc=on" +
                    "&_inc=on" +
                    "&_inc=on" +
                    "&_inc=on" +
                    "&_inc=on" +
                    "&_inc=on" +
                    "&lmt1=TOTAL" +
                    "&lmtsn=000000000003" +
                    "&lmtst=OR" +
                    "&rf=" +
                    "&rt=" +
                    "&range=000000000021" +
                    "&cpp=10" +
                    "&msc=500")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Cookie", "PCID=14444572212485265095190; RC_RESOLUTION=1920*1080; RC_COLOR=24; JSESSIONID=KlREWyoGhVhRDOOvrRqa4RfQ4nfFYza09DJTDwqtN6j2E52dOgoGRHwmCFZXIUYk.replibwas_servlet_engine6")
                    .get();

            Elements elements = doc.select("div.briefData");
            for(int i=0; i<elements.size(); i++){
                Element element = elements.get(i);

                String library = "서울도서관";
                Log.i(TAG, "library : " + library);

                Elements titleTag = element.select("dd.searchTitle");
                String title =  titleTag.get(0).text().replace("\n", "").replace("\r", "").trim();
                Log.i(TAG, "title : " + title);

                Elements bookLocationTag = element.select("dd.locCursor");
                String[] javascriptArgs = bookLocationTag.get(0).child(0).attr("onmousedown").replaceAll("'", "").replace("javascript:callLocation(", "").replace(")", "").split(",");
                String bookId = javascriptArgs[2];
                String location = javascriptArgs[3];
                Document d = Jsoup.connect("http://lib.seoul.go.kr/search/prevLoc/" + bookId)
                        .data("loc", location)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("Cookie", "PCID=14444572212485265095190; RC_RESOLUTION=1920*1080; RC_COLOR=24; JSESSIONID=KlREWyoGhVhRDOOvrRqa4RfQ4nfFYza09DJTDwqtN6j2E52dOgoGRHwmCFZXIUYk.replibwas_servlet_engine6")
                        .post();

                Elements bookNumbers = d.getElementsByTag("call_no");
                Elements bookStates = d.getElementsByTag("book_state");

                Log.i(TAG, "http://lib.seoul.go.kr/search/prevLoc/"+bookId+" 청구번호 : "+ bookNumbers.html());
                Log.i(TAG, "http://lib.seoul.go.kr/search/prevLoc/"+bookId+" 책상태 : "+ bookStates.html());

                for(int j=0; j<bookStates.size(); j++){
                    Element bookState = bookStates.get(j);
                    if(bookState.html().equals("대출가능"))
                        books.add(new Book(title, library, Integer.parseInt("1")));
                    else
                        books.add(new Book(title, library, Integer.parseInt("2")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

    private List<Book> searchGdlibrary(String keyword) {
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
                String title = dt.child(0).child(0).text().replace("\n", "").replace("\r", "").trim();
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
