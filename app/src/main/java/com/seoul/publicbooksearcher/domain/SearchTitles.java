package com.seoul.publicbooksearcher.domain;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;

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

public class SearchTitles implements UseCase <Void, String> {

    private final static String TAG = SearchTitles.class.getName();
    private final SearchTitlesListener searchTitlesListener;
    private NaverAsyncTask naverAsyncTask;

    public SearchTitles(SearchTitlesListener searchTitlesListener){
            this.searchTitlesListener = searchTitlesListener;
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
            return getBooks(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);

            List<String> titles = new ArrayList();
            for (Book book : books) {
                titles.add(book.getTitle());
            }

            searchTitlesListener.searchCompleted(titles);
        }
    }

    @NonNull
    private List<Book> getBooks(String keyword) {
        try {
            keyword =  URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://openapi.naver.com/search?key=28c9d970595a4155958faa596c7b38c2&query="+keyword+"&display=10&start=1&target=book";
        Log.i(TAG, "keyword : "+keyword+" and request Url : "+url);
        List<Book> books = new ArrayList<Book>();

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String title = Html.fromHtml(eElement.getElementsByTagName("title").item(0).getTextContent()).toString().replaceAll("\\(.*\\)", "").trim();
                    //String isbn = eElement.getElementsByTagName("isbn").item(0).getTextContent();
                    Log.i(TAG, i + "번째 책 : " + title);
                    books.add(new Book(title));
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return books;
    }
}
