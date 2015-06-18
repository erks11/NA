package com.example.sakaierika.na;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sakaierika on 2015/06/14.
 */
public class RssListAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;
    private TextView mTitle;
    private TextView mpubDate;

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

        }
        return view;
    }
}