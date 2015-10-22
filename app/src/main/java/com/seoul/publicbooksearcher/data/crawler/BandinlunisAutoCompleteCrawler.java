package com.seoul.publicbooksearcher.data.crawler;

import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.seoul.publicbooksearcher.data.BaseBookRepository;
import com.seoul.publicbooksearcher.domain.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BandinlunisAutoCompleteCrawler extends BaseBookRepository {

    private final static String TAG = BandinlunisAutoCompleteCrawler.class.getName();

    @Override
    public List<Book> selectByKeyword(String keyword) {
        String url = "http://222.122.120.242:7571/ksf/api/suggest?callback=&target=complete&domain_no=0&term="+keyword+"&mode=sc&max_count=10&_=1445520411641";
        Log.i(TAG, "keyword : " + keyword + " and request Url : " + url);

        try {
            String body = Jsoup.connect(url).get().body().toString();
            String json = body.substring(1, body.length() - 2);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray suggestionsArray = (JSONArray) jsonObject.getJSONArray("suggestions").get(0);

            List<Book> books = new ArrayList();

            for(int i=0; i<suggestionsArray.length(); i++){
                String titleAndWriter = ((JSONArray) suggestionsArray.get(i)).getString(0);
                int index = titleAndWriter.lastIndexOf('-');
                String title = titleAndWriter.substring(0, index-1);
                books.add(new Book(title));
            }

            return books;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
