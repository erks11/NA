package com.example.sakaierika.na;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sakaierika on 2015/06/18.
 */
public class KeyphraseClient {
    private String mAppID;
    private String str;
    private Item item;
    String parsedText = "";

    private static final String TAG = "TestApp";


    public KeyphraseClient(String appID,String descr) {
        mAppID = appID;
        str = descr;
    }

    public KeyphraseData getLatestKeyphrase() {
        KeyphraseData result = new KeyphraseData();
        int x = 10;
        Log.d("MyApp", "x=" + x);

        str = str.replaceAll("・","");
        str = str.replaceAll("\n","");
        str = str.replaceAll(" ","");
        str = str.replaceAll("　","");

        str.trim();
        Log.d("str",str);


        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.path("http://jlp.yahooapis.jp/KeyphraseService/V1/extract");
        uriBuilder.appendQueryParameter("appid", mAppID);
        uriBuilder.appendQueryParameter("sentence", str);
        uriBuilder.appendQueryParameter("output", "json");
        //uriBuilder.appendQueryParameter("callback","ResultSet");
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

            //builderはStringBuilderクラスなのでtoString()で文字列に変換
            JSONArray jsonArray = new JSONArray(jsonEntity.toString());
            //JSON Arrayのサイズを表示
            System.out.println("Number of entries " + jsonArray.length());
            //JSON Objectを作成する
            for (int i = 0; i < jsonArray.length(); i++) {
                //getJSONObjectでJSON Arrayに格納された要素をJSON Objectとして取得できる
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //JSON Objectをパースする
                //表示する際はgetString("ほげほげ")で"ほげほげ"をキーとする値を取得できる
                //userのように入れ子になっているときは、getJSONObject()を使って階層を下っていく
                System.out.println(i);
                System.out.println("キーフレーズ：" + jsonObject.getString("key"));
                System.out.println("スコア：" + jsonObject.getString("value"));
                System.out.println();//改行
//            Log.d("JSONSampleActivity", jsonEntity.toString());
//            Log.d(TAG,strjson);
//            int startURL,endURL;
//
//            endURL = strjson.indexOf("100");
//            startURL = strjson.lastIndexOf(",",endURL);
//            result.setKeyphrase(strjson.substring(startURL+2,endURL-2));
//
//            Log.d(TAG,strjson.substring(startURL+2,endURL-2));

//            if (jsonEntity != null) {
//                JSONObject jsonKeyphrase =
//                        jsonEntity.optJSONObject("KeyphraseResult");
//                Log.d("MyApp", "jsonKeyphrase=" + jsonKeyphrase);
//                if (jsonKeyphrase != null) {
//                    result.setKeyphrase(jsonKeyphrase.optString("Keyphrase"));
//                }
//            }
            }
        }catch (ClientProtocolException e) {
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
