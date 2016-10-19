package com.joker.explorer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joker.explorer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FavoriteFolderAdapter;
import bean.FavoriteFolder;
import database.FavoriteHelper;

public class FavoriteFolderActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_folder);
        initView();
    }

    void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView = (ListView) findViewById(R.id.list_view);
        FavoriteHelper favoriteHelper = new FavoriteHelper(this);
        final List<FavoriteFolder> favoriteFolderList = new ArrayList<>();
        Cursor cursor = favoriteHelper.getAll();

        while (cursor.moveToNext()) {
            FavoriteFolder favoriteFolder = new FavoriteFolder();
            favoriteFolder.setFolderName(cursor.getString(cursor.getColumnIndex("folderName")));
            favoriteFolder.setFolderPath(cursor.getString(cursor.getColumnIndex("folderPath")));
            favoriteFolderList.add(favoriteFolder);
        }
        FavoriteFolderAdapter adapter = new FavoriteFolderAdapter(this, favoriteFolderList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteFolder folder = favoriteFolderList.get(position);
                Intent intentToShowFile = new Intent(FavoriteFolderActivity.this, InternalStorageActivity.class);
                intentToShowFile.putExtra("FavoriteFolder", folder.getFolderPath());
                startActivity(intentToShowFile);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
