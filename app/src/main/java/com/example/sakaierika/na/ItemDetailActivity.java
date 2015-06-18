package com.example.sakaierika.na;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
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
    private TextView mRe1,mRe2,mRe3;
    private ImageView mImage;

    private CharSequence[] mReTitle = new String[3];
    private CharSequence[] mReLink = new String[3];

    private ArrayList mItems;

    protected String link;
    protected String descr;
    protected String title;
    protected String rTitle;

    Linkify.TransformFilter filter;

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
        filter = new Linkify.TransformFilter() {
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
            mRe1 = (TextView)findViewById(R.id.item_re1);
            mRe2 = (TextView)findViewById(R.id.item_re2);
            mRe3 = (TextView)findViewById(R.id.item_re3);
           result.getmRelativeLink();
            mReTitle  = result.getmRelativeTitle();
            mReLink = result.getmRelativeLink();
            rTitle = mReTitle[0]+"       よむ";
            mRe1.setText(rTitle);
            Pattern pattern = Pattern.compile("よむ");
            filter = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return mReLink[0].toString();
                }
            };
            Linkify.addLinks(mRe1, pattern, mReLink[0].toString(), null, filter);
            rTitle = mReTitle[1]+"       よむ";
            mRe2.setText(rTitle);
            filter = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return mReLink[1].toString();
                }
            };
            Linkify.addLinks(mRe2, pattern, mReLink[1].toString(), null, filter);
            rTitle = mReTitle[2]+"       よむ";
            mRe3.setText(rTitle);
            filter = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return mReLink[2].toString();
                }
            };
            Linkify.addLinks(mRe3, pattern, mReLink[2].toString(), null, filter);
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