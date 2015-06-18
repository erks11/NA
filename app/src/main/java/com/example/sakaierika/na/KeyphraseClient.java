package com.example.sakaierika.na;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by sakaierika on 2015/06/18.
 */
public class KeyphraseClient {
    private String mAppID;
    private String str;
    private static String BASEURL = "http://news.livedoor.com/search/article/?ie=euc-jp&word=";
    private static String USERAGENT;

    private String strsarch;
    private String secondstrsarch;

    private static final String TAG = "NA";

    public KeyphraseClient(String appID,String title) {
        mAppID = appID;
        str = title;
    }


    public KeyphraseData getLatestKeyphrase() {
        KeyphraseData result = new KeyphraseData();

        str = str.replaceAll("\n", "");
        str = str.replaceAll(" ", "");
        str = str.replaceAll("　", "");

        Log.d("str", str);

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.path("http://jlp.yahooapis.jp/KeyphraseService/V1/extract");
        uriBuilder.appendQueryParameter("appid", mAppID);
        uriBuilder.appendQueryParameter("sentence", str);
        uriBuilder.appendQueryParameter("output", "json");
        String uri = Uri.decode(uriBuilder.build().toString());
        Log.d("MyApp", "uri=" + uri);
        // Request HTTP GET
        HttpUriRequest httpGet = new HttpGet(uri);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
            String stringEntity = EntityUtils.toString(httpResponse.getEntity());
            Log.d(TAG, "stringEntity=" + stringEntity);
            JSONObject jsonEntity = new JSONObject(stringEntity);
            Log.d(TAG, "jsonEntity=" + jsonEntity);
            String strjson = jsonEntity.toString();

            Log.d(TAG, "strjson=" + strjson);
            strjson = strjson.substring(1, strjson.length() - 1);
            String set[] = strjson.split("[,:]");


            Log.d("msg","int = "+Integer.parseInt(set[1]));

            for (int i = 1; i < set.length; i = i + 2) {
                if (set[i].equals("100")) {
                    strsarch = set[i - 1];
                    set[i] = "0";
                }

            }
            strsarch = strsarch.substring(1, strsarch.length() - 1);

            USERAGENT = System.getProperty("http.agent");

            Elements links = Jsoup.connect(BASEURL + URLEncoder.encode(strsarch, "euc_jp")).userAgent(USERAGENT).get().select("ul.articlelist>li>a");
            Log.d("url", BASEURL + URLEncoder.encode(strsarch, "euc_jp"));
            Log.d("msg",String.valueOf( links.isEmpty()));

            if (links.isEmpty()!= true) {
                Log.d("msg","trueじゃない！！");
                int i = 0;
                String[] title = new String[4];
                String[] url = new String[4];
                for (Element link : links) {
                    if ( i < 4) {
                        if(link == null) ;
                        title[i] = link.text();
                        url[i] = link.absUrl("href");
                        // url = URLDecoder.decode(url.substring(url.indexOf("=") + 1, url.indexOf('&')), "UTF-8");
                        Log.d("title=", title[i]);
                        Log.d("url=", url[i]);
                        i++;
                    } else break;
                }
                result.setmRelativeTitle(title);
                result.setmRelativeLink(url);
            } else {
                int max = 50;
                for (int i = 1; i < set.length; i = i + 2) {
                    if (max < Integer.parseInt(set[i])) {
                         max = Integer.parseInt(set[i]);
                         secondstrsarch = set[i-1];
                    }
                    Log.d("msg","int = "+Integer.parseInt(set[i]));
                }

                Elements secondlinks = Jsoup.connect(BASEURL + URLEncoder.encode(secondstrsarch, "euc_jp")).userAgent(USERAGENT).get().select("ul.articlelist>li>a");
                Log.d("url", BASEURL + URLEncoder.encode(secondstrsarch, "euc_jp"));
                Log.d("msg",String.valueOf( secondlinks.isEmpty()));

                if (secondlinks.isEmpty()!= true) {
                    Log.d("msg","trueじゃない！！");
                    int i = 0;
                    String[] title = new String[4];
                    String[] url = new String[4];
                    for (Element secondlink : secondlinks) {
                        if (i < 4) {
                            title[i] = secondlink.text();
                            url[i] = secondlink.absUrl("href");
                            // url = URLDecoder.decode(url.substring(url.indexOf("=") + 1, url.indexOf('&')), "UTF-8");
                            Log.d("title=", title[i]);
                            Log.d("url=", url[i]);
                            i++;
                        } else break;
                    }
                    result.setmRelativeTitle(title);
                    result.setmRelativeLink(url);
                }else {
                    result.setmRelativeTitle(null);
                    result.setmRelativeLink(null);

                }

            }
        }  catch (ClientProtocolException e) {
            Log.e(TAG, "App ERROR!");
        } catch (IOException e) {
            Log.e(TAG, "App ERROR!!");
        } catch (JSONException e) {
            Log.e(TAG, "App ERROR!!!");
        }catch (Exception e) {
             Log.e(TAG, "App ERROR!!!!");
            }

        return result;
    }

}
