package com.example.sakaierika.na;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements OnItemClickListener,View.OnClickListener{

    public  String RSS_FEED_URL = "http://news.livedoor.com/topics/rss/top.xml";
    private ArrayList mItems;
    private RssListAdapter mAdapter;
    private  ListView _listview;

    protected int m_iArray = 10;
    TextView[] m_textView = new TextView[m_iArray];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItems = new ArrayList();
        mAdapter = new RssListAdapter(this, mItems);

       _listview = (ListView)findViewById(R.id.listView1);

        int[] resId = { R.id.top,  R.id.dom,
                        R.id.inte, R.id.eco,
                        R.id.ent,  R.id.spo,
                        R.id.cin,  R.id.gor,
                        R.id.love, R.id.trend};

        for(int i = 0; i<m_iArray; i++){
            Log.isLoggable("res",resId[i]);
            m_textView[i] = new TextView(this);    //ボタン生成
            m_textView[i].setId(resId[i]);    //リソースID設定
            findViewById(resId[i]).setOnClickListener(this);
        }

     // タスクを起動する
        RssParserTask task = new RssParserTask(this, mAdapter,_listview);
        task.execute(RSS_FEED_URL);
        _listview.setOnItemClickListener(this);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top:
               RSS_FEED_URL = "http://news.livedoor.com/topics/rss/top.xml";
               Log.d(RSS_FEED_URL,"top");
                break;
            case R.id.dom:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/dom.xml";
                Log.d(RSS_FEED_URL,"dom");
                break;
            case R.id.inte:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/int.xml";
                Log.d(RSS_FEED_URL,"inte");
                break;
            case R.id.eco:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/eco.xml";
                Log.d(RSS_FEED_URL,"eco");
                break;
            case R.id.ent:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/ent.xml";
                Log.d(RSS_FEED_URL,"ent");
                break;
            case R.id.spo:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/spo.xml";
                Log.d(RSS_FEED_URL,"spo");
                break;
            case R.id.cin:
                RSS_FEED_URL = "http://news.livedoor.com/rss/summary/52.xml";
                Log.d(RSS_FEED_URL,"cin");
                break;
            case R.id.gor:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/gourmet.xml";
                Log.d(RSS_FEED_URL,"gor");
                break;
            case R.id.love:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/love.xml";
                Log.d(RSS_FEED_URL,"love");
                break;
            case R.id.trend:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/trend.xml";
                Log.d(RSS_FEED_URL,"trend");
                break;
//            default:
//                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/top.xml";
//                Log.d(RSS_FEED_URL,"default");
//                break;

        }
        mItems = new ArrayList();
        mAdapter = new RssListAdapter(this, mItems);
        _listview = (ListView)findViewById(R.id.listView1);
        RssParserTask task = new RssParserTask(this, mAdapter,_listview);
        task.execute(RSS_FEED_URL);
        _listview.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        Item item = (Item) mItems.get(arg2);
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("TITLE", item.getTitle());
        intent.putExtra("DESCRIPTION", item.getDescription());
        intent.putExtra("DATE", item.getDate());
        intent.putExtra("LINK", item.getLink());
        startActivity(intent);
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
