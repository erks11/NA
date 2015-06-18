package com.example.sakaierika.na;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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
    private NetworkImageView imageView;



    private CharSequence[] mReTitle = new String[3];
    private CharSequence[] mReLink = new String[3];


    private  String imgURL;
    protected String link;
    protected String descr;
    protected String title;
    protected String rTitle;

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    Linkify.TransformFilter filter;

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
        mLink.setText("きじをよむ");
        Pattern pattern = Pattern.compile("きじをよむ");
        filter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return link;
            }
        };
        Linkify.addLinks(mLink, pattern, link, null, filter);


        imgURL = intent.getStringExtra("IMG");
        //Log.d("imgURL", imgURL);


        mRe1 = (TextView) findViewById(R.id.item_re1);
        mRe2 = (TextView) findViewById(R.id.item_re2);
        mRe3 = (TextView) findViewById(R.id.item_re3);

        mRe1.setText("ひっしにかんれんきじをさがしちゅう...");

        KeyphraseClientTask keyphraseclienttask = new KeyphraseClientTask();
        keyphraseclienttask.execute();

    }


    class KeyphraseClientTask extends AsyncTask<Void, Void, KeyphraseData> {
        protected KeyphraseData doInBackground(Void... params) {
            KeyphraseClient keyphraseClient = new KeyphraseClient(MY_APP_ID,title);
            return keyphraseClient.getLatestKeyphrase();
        }
        protected void onPostExecute(KeyphraseData result) {

            mReTitle = result.getmRelativeTitle();
            mReLink = result.getmRelativeLink();
            if (mReTitle != null && mReLink != null) {
                for (int i = 0;i <2 ;i++){
                    if(mReLink[i].equals(link)){
                        mReLink[i] = mReLink[3];
                    }
                }
                rTitle = mReTitle[0] + "\t→\tよむ";
                mRe1.setText(rTitle);
                Pattern pattern = Pattern.compile("よむ");
                filter = new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return mReLink[0].toString();
                    }
                };
                Linkify.addLinks(mRe1, pattern, mReLink[0].toString(), null, filter);
                if(mReTitle[1]!=null) {
                    rTitle = mReTitle[1] + "\t→\tよむ";
                    mRe2.setText(rTitle);
                    filter = new Linkify.TransformFilter() {
                        @Override
                        public String transformUrl(Matcher match, String url) {
                            return mReLink[1].toString();
                        }
                    };
                    Linkify.addLinks(mRe2, pattern, mReLink[1].toString(), null, filter);
                }else{
                    mRe2.setText("ひとつしかかんれんきじがみつからなかったよ!");
                }
                if(mReTitle[2]!=null) {
                    rTitle = mReTitle[2] + "\t→\tよむ";
                    mRe3.setText(rTitle);
                    filter = new Linkify.TransformFilter() {
                        @Override
                        public String transformUrl(Matcher match, String url) {
                            return mReLink[2].toString();
                        }
                    };
                    Linkify.addLinks(mRe3, pattern, mReLink[2].toString(), null, filter);
                }else if(mReTitle[1]!=null) mRe3.setText("ふたつしかかんれんきじがみつからなかったよ!");
            }else  mRe1.setText("かんれんきじがみつからなかったよ!");
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