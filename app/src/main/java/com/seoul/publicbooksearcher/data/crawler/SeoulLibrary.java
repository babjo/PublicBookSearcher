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

public class SeoulLibrary implements BookRepository {

    private static final String TAG = SeoulLibrary.class.getName();

    public SeoulLibrary() {
    }

    @Override
    public List<Book> selectByKeyword(String keyword) {
        keyword = replaceSpecial(keyword);
        keyword = keyword.replaceAll(" ", "+");

        List<Book> books = null;
        try {
            books = getBooks(keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

    private List<Book> getBooks(String keyword) throws IOException {
        List<Book> books = new ArrayList();
        Document doc = Jsoup.connect("http://lib.seoul.go.kr/search/laz/result?" +
                "st=KWRD" +
                "&gubunFlag=" +
                "&nation_id=" +
                "&si=1" +
                "&q=" + keyword +
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
                    .post();

            Elements bookNumbers = d.getElementsByTag("call_no");
            Elements bookStates = d.getElementsByTag("book_state");

            Log.i(TAG, "http://lib.seoul.go.kr/search/prevLoc/"+bookId+" 청구번호 : "+ bookNumbers.html());
            Log.i(TAG, "http://lib.seoul.go.kr/search/prevLoc/"+bookId+" 책상태 : "+ bookStates.html());

            for(int j=0; j<bookStates.size(); j++){
                Element bookState = bookStates.get(j);
                if(bookState.html().equals("대출가능"))
                    books.add(new Book(title, library, 1, bookId));
                else
                    books.add(new Book(title, library, 2, bookId));
            }
        }

        return books;
    }

    @Override
    public void insertOrUpdateBooks(String keyword, List<Book> books) {}

    public static String replaceSpecial(String str){
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        str =str.replaceAll(match, " ");
        return str;
    }

}
