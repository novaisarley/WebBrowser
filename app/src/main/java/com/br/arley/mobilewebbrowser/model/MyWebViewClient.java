package com.br.arley.mobilewebbrowser.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.room.Room;
import com.br.arley.mobilewebbrowser.db.AppDataBase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyWebViewClient extends android.webkit.WebViewClient {

    private Activity activity = null;
    private EditText edtUrl;
    private ImageView imgError;
    private TextView tvErrorMsg;
    private Button btGoBack;
    private ProgressBar progressBar;


    public MyWebViewClient(Activity activity, EditText edtUrl, ImageView imgError, TextView tvErrorMsg, ProgressBar progressBar, Button btGoback) {
        this.activity = activity;
        this.edtUrl = edtUrl;
        this.imgError = imgError;
        this.tvErrorMsg = tvErrorMsg;
        this.progressBar = progressBar;
        this.btGoBack = btGoback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        AppDataBase db = Room.databaseBuilder(view.getContext(), AppDataBase.class, "lilbank").allowMainThreadQueries().build();
        String url = request.getUrl().toString();
        String formatedUrl = formateUrl(url.trim());
        String date = getFormatedDate();

        db.historyDao().insertAll(new History(url, formatedUrl, date));

        return super.shouldOverrideUrlLoading(view, request);
    }

    private String formateUrl(String url){
        String formatedUrl;

        if (url.contains("https://www."))formatedUrl = url.replace("https://www.", "");
        else if (url.contains("http://www."))formatedUrl = url.replace("http://www.", "");
        else if (url.contains("https://"))formatedUrl = url.replace("https://", "");
        else if (url.contains("http://"))formatedUrl = url.replace("http://", "");
        else formatedUrl = url;

        return formatedUrl;
    }

    private String getFormatedDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateO = new Date();
        String time = dateFormat.format(dateO);

        String[] dateArray = time.split(" ");
        String date = dateArray[0] + "  |  " + dateArray[1];

        return date;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        edtUrl.setText("Carregando...");
        progressBar.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        edtUrl.setText(url);
        progressBar.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }

    /*@Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        edtUrl.setEnabled(false);
        view.setVisibility(View.INVISIBLE);
        imgError.setVisibility(View.VISIBLE);
        tvErrorMsg.setVisibility(View.VISIBLE);
        tvErrorMsg.setText("Que pena. \nAlgo ruim aconteceu : ( \n\n" + error.getDescription().toString());
        btGoBack.setVisibility(View.VISIBLE);
        super.onReceivedError(view, request, error);
    }*/

}
