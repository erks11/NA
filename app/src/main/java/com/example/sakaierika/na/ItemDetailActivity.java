package com.example.sakaierika.na;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sakaierika on 2015/06/14.
 */
public class ItemDetailActivity extends Activity implements OnItemClickListener,View.OnClickListener{
    private TextView mTitle;
    private TextView mpubDate;
    private TextView mDescr;
    private TextView mLink;
    private TextView mRelation;
    private ImageView mImage;

    private ArrayList mKeyphrase;

    private ArrayList mItems;

    protected String link;
    protected String descr;
    protected String title;

    private static final String TAG = "TestApp";
    private static final String MY_APP_ID = "dj0zaiZpPUhJYU5DRnRoQ2gyOSZzPWNvbnN1bWVyc2VjcmV0Jng9ZWQ-";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        Intent intent = getIntent();

        title = intent.getStringExtra("TITLE");
        mTitle = (TextView) findViewById(R.id.item_title);
        mTitle.setText(title);

        descr = intent.getStringExtra("DESCRIPTION");
        mDescr = (TextView) findViewById(R.id.item_description);
        mDescr.setText(descr);

        String date = intent.getStringExtra("DATE");
        mpubDate = (TextView) findViewById(R.id.item_date);
        mpubDate.setText(date);

        link = intent.getStringExtra("LINK");
        mLink = (TextView) findViewById(R.id.item_link);
        mLink.setText("記事を読む");
        Pattern pattern = Pattern.compile("記事を読む");
        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return link;
            }
        };
        Linkify.addLinks(mLink, pattern, link, null, filter);

        String imgURL = intent.getStringExtra("IMG");
        mImage = (ImageView) findViewById(R.id.item_img);


        KeyphraseClientTask keyphraseclienttask = new KeyphraseClientTask();
        keyphraseclienttask.execute();

    }

    class KeyphraseClientTask extends AsyncTask<Void, Void, KeyphraseData> {
        protected KeyphraseData doInBackground(Void... params) {
            KeyphraseClient keyphraseClient = new KeyphraseClient(MY_APP_ID,title);
            return keyphraseClient.getLatestKeyphrase();
        }
        protected void onPostExecute(KeyphraseData result) {
            // LogCat の出力結果
            mRelation = (TextView)findViewById(R.id.item_relation);
            mRelation.setText(result.getKeyphrase());
            Log.d(TAG, "Keyphrase = " + result.getKeyphrase());
        }
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}