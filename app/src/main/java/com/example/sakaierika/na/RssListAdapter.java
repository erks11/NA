package com.example.sakaierika.na;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by sakaierika on 2015/06/14.
 */
public class RssListAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;
    private TextView mTitle;
    private TextView mpubDate;
    private ImageView mImg;

    public RssListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        // TODO Auto-generated constructor stub
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.itemlayout, null);
        }

        Item item = this.getItem(position);
        if (item != null) {
            String title = item.getTitle().toString();
            mTitle = (TextView) view.findViewById(R.id.title);
            mTitle.setText(title);
            String mpubdate = item.getDate().toString();
            mpubDate = (TextView) view.findViewById(R.id.date);
            mpubDate.setText(mpubdate);
            String img = item.getImgURL().toString();
            Log.d("img", img);
            if (img.contains("http")) {
                mImg = (ImageView) view.findViewById(R.id.img);
                URL url = null;
                InputStream istream;
                try {
                    //画像のURLを直うち
                    url = new URL("http://image.news.livedoor.com/newsimage/f/c/fc85c_248_ca2339d6b6c6b05a86f77c3cbff59d0f-s.jpg");
                    //インプットストリームで画像を読み込む
                    istream = url.openStream();
                    //読み込んだファイルをビットマップに変換
                    Bitmap oBmp = BitmapFactory.decodeStream(istream);
                    //ビットマップをImageViewに設定
                    mImg.setImageBitmap(oBmp);
                    //インプットストリームを閉じる
                    istream.close();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
            }
        }
        return view;
    }
}
