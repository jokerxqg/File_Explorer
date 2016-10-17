package com.joker.explorer.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.explorer.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

import adapter.SearchFileAdapter;
import bean.SearchFile;
import task.SearchAsyncScan;
import utils.FileUtils;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView listView;
    private SearchView searchView;
    private List<SearchFile> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    void initViews() {
        searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchFile clickSearchFile = list.get(position);
                startActivity(FileUtils.getFileIntent(clickSearchFile.getFileType(), clickSearchFile.getFilePath()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            list = new SearchAsyncScan().execute(query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        SearchFileAdapter adapter = new SearchFileAdapter(this, list);
        listView.setAdapter(adapter);

        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }
}
