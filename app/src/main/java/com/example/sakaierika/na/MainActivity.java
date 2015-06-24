package com.example.sakaierika.na;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
    private TextView category;
    DrawerLayout drawerLayout;
    ListView drawerListView;

    protected int m_iArray = 10;
    TextView[] m_textView = new TextView[m_iArray];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItems = new ArrayList();
        mAdapter = new RssListAdapter(this, mItems);

       _listview = (ListView)findViewById(R.id.list);

        category = (TextView)findViewById(R.id.cat);
        category.setText(R.string.top);


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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.
                list);

     // タスクを起動する
        RssParserTask task = new RssParserTask(this, mAdapter,_listview);
        task.execute(RSS_FEED_URL);
        _listview.setOnItemClickListener(this);
    }




    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top:
               RSS_FEED_URL = "http://news.livedoor.com/topics/rss/top.xml";
               category.setText(R.string.top);
               Log.d(RSS_FEED_URL,"top");
                break;
            case R.id.dom:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/dom.xml";
                category.setText(R.string.dom);
                Log.d(RSS_FEED_URL,"dom");
                break;
            case R.id.inte:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/int.xml";
                category.setText(R.string.inte);
                Log.d(RSS_FEED_URL,"inte");
                break;
            case R.id.eco:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/eco.xml";
                category.setText(R.string.eco);
                Log.d(RSS_FEED_URL,"eco");
                break;
            case R.id.ent:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/ent.xml";
                category.setText(R.string.ent);
                Log.d(RSS_FEED_URL,"ent");
                break;
            case R.id.spo:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/spo.xml";
                category.setText(R.string.spo);
                Log.d(RSS_FEED_URL,"spo");
                break;
            case R.id.cin:
                RSS_FEED_URL = "http://news.livedoor.com/rss/summary/52.xml";
                category.setText(R.string.cin);
                Log.d(RSS_FEED_URL,"cin");
                break;
            case R.id.gor:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/gourmet.xml";
                category.setText(R.string.gor);
                Log.d(RSS_FEED_URL,"gor");
                break;
            case R.id.love:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/love.xml";
                category.setText(R.string.love);
                Log.d(RSS_FEED_URL,"love");
                break;
            case R.id.trend:
                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/trend.xml";
                category.setText(R.string.trend);
                Log.d(RSS_FEED_URL,"trend");
                break;
//            default:
//                RSS_FEED_URL = "http://news.livedoor.com/topics/rss/top.xml";
//                Log.d(RSS_FEED_URL,"default");
//                break;

        }
        mItems = new ArrayList();
        mAdapter = new RssListAdapter(this, mItems);
        _listview = (ListView)findViewById(R.id.list);
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
