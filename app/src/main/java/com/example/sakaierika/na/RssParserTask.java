package com.example.sakaierika.na;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sakaierika on 2015/06/14.
 */
public class RssParserTask extends AsyncTask<String, Integer, RssListAdapter> {

    private MainActivity mActivity;
    private RssListAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private ListView mListView;
    String text;

    // Constract
    public RssParserTask(MainActivity activity, RssListAdapter adapter, ListView _listView) {
        mActivity = activity;
        mListView = _listView;
        mAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("よみこみちゅう...");
        mProgressDialog.show();
    }

    @Override
    protected RssListAdapter doInBackground(String... params) {
        RssListAdapter result = null;
        try {
            URL url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            result = parseXml(is);
            Log.d("RSS", "result = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(RssListAdapter result) {
        mProgressDialog.dismiss();
        mListView.setAdapter(result);
    }


    public RssListAdapter parseXml(InputStream is) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, null);
            int event = parser.getEventType();
            Item item = null;
            String tag = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        //Log.d("RSS","tag = "+tag);
                        if (tag.equals("item")) {
                            item = new Item();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (item != null) {
                            text = parser.getText();
                            if (text.trim().length() != 0) {
                                if (tag.equals("title")) {
                                    item.setTitle(text);
                                } else if (tag.equals("pubDate")) {
                                    DateFormat input = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
                                    Date d = input.parse(text);
                                    DateFormat output = new SimpleDateFormat("yyyy年MM月dd日 (E) HH:mm:ss");
                                    item.setDate(output.format(d));
                                } else if (tag.equals("description")) {
                                    if (text.contains("img")) {
                                        int startURL, endURL;
                                        startURL = text.indexOf("http");
                                        endURL = text.indexOf(".jpg");
                                        String imgURL = text.substring(startURL, endURL + 4);
                                        Log.d("imgURL",imgURL);
                                        item.setImgURL(imgURL);

                                        int sURL, eURL;
                                        sURL = text.indexOf("<");
                                        endURL = text.indexOf("alt=");
                                        String str = text.substring(sURL, endURL + 5);
                                        text =text.replaceAll(str, "");
                                    }

                                    if (text.contains("br") || text.contains("li") || text.contains("ul")) {
                                        text = text.replaceAll("<br />", "");
                                        text = text.replaceAll("<li>", "❤︎");
                                        text = text.replaceAll("</li>", "\n");
                                        text = text.replaceAll("<ul>", "");
                                        text = text.replaceAll("</ul>", "");

                                        text = text.replaceAll(" ","");
                                        text = text.replaceAll("　","");
                                    }

                                    if (text.contains("article") && text.contains("href")) {
                                        int test, length;
                                        String href;
                                        test = text.indexOf("<a");
                                        length = text.length();
                                        href = text.substring(test, length);
                                        text = text.replace(href, "");
                                    }
                                    item.setDescription(text);
                                } else if (tag.equals("link")) {
                                    item.setLink(text);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            // ListViewに反映するためのArrayAdapterに追加
                            mAdapter.add(item);
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAdapter;
    }

}