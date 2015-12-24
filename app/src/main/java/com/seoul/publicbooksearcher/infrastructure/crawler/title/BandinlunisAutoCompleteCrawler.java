package com.seoul.publicbooksearcher.infrastructure.crawler.title;

import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@EBean
public class BandinlunisAutoCompleteCrawler implements TitleCrawler {

    private final static String TAG = BandinlunisAutoCompleteCrawler.class.getName();

    @Override
    public List<String> crawling(String keyword) {
        String url = null;
        try {
            url = "http://222.122.120.242:7571/ksf/api/suggest?callback=&target=complete&domain_no=0&term="+ URLEncoder.encode(keyword, "utf-8") + "&mode=sc&max_count=10&_=";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Log.i(TAG, "keyword : " + keyword + " and request Url : " + url);

        try {
            String text = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36").get().text();
            String json = text.substring(1, text.length() - 2);
            Log.i(TAG, "keyword json : "+json);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray suggestionsArray = (JSONArray) jsonObject.getJSONArray("suggestions").get(0);

            List<String> titles = new ArrayList();

            for(int i=0; i<suggestionsArray.length(); i++){
                String titleAndWriter = ((JSONArray) suggestionsArray.get(i)).getString(0);
                int index = titleAndWriter.lastIndexOf('-');
                String title = titleAndWriter.substring(0, index-1);

                if(!titles.contains(title))
                    titles.add(title);
            }

            return titles;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
