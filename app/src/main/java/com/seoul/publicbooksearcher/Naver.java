package com.seoul.publicbooksearcher;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

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

public class Naver implements UserRequester {

    private final static String TAG = Naver.class.getName();
    private NaverListener naverListener;

    public Naver(NaverListener naverListener) {
        this.naverListener = naverListener;
    }

    @Override
    public void search(String keyword) {
        new NaverAsyncTask().execute(keyword);
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
            naverListener.searchCompleted(books);
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
                    String title = eElement.getElementsByTagName("title").item(0).getTextContent();
                    String title2 = Html.fromHtml(title).toString();
                    String isbn = eElement.getElementsByTagName("isbn").item(0).getTextContent();
                    Log.i(TAG, i + "번째 책 : " + title2);
                    books.add(new Book(title2, isbn));
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
