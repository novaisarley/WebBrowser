package com.br.arley.mobilewebbrowser.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.br.arley.mobilewebbrowser.R;
import com.br.arley.mobilewebbrowser.db.AppDataBase;
import com.br.arley.mobilewebbrowser.model.History;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    ImageButton btHistory;
    SearchView searchView;
    AppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setObjects();

        setObjectsListeners();

    }

    void setObjects() {
        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "lilbank").allowMainThreadQueries().build();

        btHistory = findViewById(R.id.activity_search_bt_history);
        searchView = findViewById(R.id.activity_search_searchview);
    }

    void setObjectsListeners() {
        btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, HistoryActivity.class));
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = query.trim();
                String formatedUrl = formateUrl(url);
                String date = getFormatedDate();

                if (!url.contains("http://") && !url.contains("https://")){
                    url = "https://www.google.com/search?q=" + url;
                }

                History history = new History(url, formatedUrl, date);
                db.historyDao().insertAll(history);

                Intent i = new Intent(SearchActivity.this, WebActivity.class);
                i.putExtra("url", url);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private String getFormatedDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateO = new Date();
        String time = dateFormat.format(dateO);

        String[] dateArray = time.split(" ");
        String date = dateArray[0] + "  |  " + dateArray[1];

        return date;
    }

    private String formateUrl(String url) {
        String formatedUrl;

        if (url.contains("https://www.")) formatedUrl = url.replace("https://www.", "");
        else if (url.contains("http://www.")) formatedUrl = url.replace("http://www.", "");
        else if (url.contains("https://")) formatedUrl = url.replace("https://", "");
        else if (url.contains("http://")) formatedUrl = url.replace("http://", "");
        else formatedUrl = "www.google.com/search?q=" + url;

        return formatedUrl;
    }


}