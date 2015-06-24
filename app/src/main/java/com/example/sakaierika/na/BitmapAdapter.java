package com.example.sakaierika.na;

/**
 * Created by sakaierika on 2015/06/24.
*/

 import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.List;

 public class BitmapAdapter extends ArrayAdapter<Bitmap> {
 private int resourceId;
 int height;

 public BitmapAdapter(Context context, int resource, List<Bitmap> objects, int height) {
 super(context, resource, objects);
 resourceId = resource;
 this.height = height;
 }

 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
 if (convertView == null) {
 LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 convertView = inflater.inflate(resourceId, null);
 }

 ImageView view = (ImageView) convertView;
 view.setMinimumHeight(height);
 view.setImageBitmap(getItem(position));
 view.setScaleType(ScaleType.FIT_END);

 return view;
 }


 }