package com.seoul.publicbooksearcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GodeoLibraryListView godeoLibraryListView = new GodeoLibraryListView(this,  (ListView) findViewById(R.id.book_list));
        final UserRequester godeokLibrary = new GodeokLibrary(godeoLibraryListView);
        final NaverBookAutoCompleteTextView naverBookAutoCompleteTextView = new NaverBookAutoCompleteTextView(this, (AutoCompleteTextView) findViewById(R.id.auto_edit), godeokLibrary);

        ((Button) findViewById(R.id.search_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = naverBookAutoCompleteTextView.getText();
                naverBookAutoCompleteTextView.dismissDropDown();
                naverBookAutoCompleteTextView.clearFocus();
                godeokLibrary.search(keyword);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
