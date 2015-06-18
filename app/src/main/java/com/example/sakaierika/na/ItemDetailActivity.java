package com.example.sakaierika.na;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
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

    private CharSequence[] mseReTitle = new String[3];
    private CharSequence[] mseReLink = new String[3];

    private ArrayList mItems;

    protected String link;
    protected String descr;
    protected String title;
    protected String rTitle;

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

        String imgURL = intent.getStringExtra("IMG");
        mImage = (ImageView) findViewById(R.id.item_img);

        try {
            URL image_url = new URL(imgURL);

            InputStream is = (InputStream) image_url.getContent();

            Drawable drawable = Drawable.createFromStream(is,"");

            is.close();

            mImage.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();
        }


//        BitmapWorkerTask test = new BitmapWorkerTask(mImage);
//        test.execute();

        mRe1 = (TextView) findViewById(R.id.item_re1);
        mRe2 = (TextView) findViewById(R.id.item_re2);
        mRe3 = (TextView) findViewById(R.id.item_re3);

        mRe1.setText("ひっしにかんれんきじをさがしちゅう...");

        KeyphraseClientTask keyphraseclienttask = new KeyphraseClientTask();
        keyphraseclienttask.execute();

    }

    public void loadBitmap(Context context, String filePath, ImageView imageView, Bitmap loadingBitmap) {
        // 同じタスクが走っていないか、同じ ImageView で古いタスクが走っていないかチェック
        if (cancelPotentialWork(filePath, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), loadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(filePath);
        }
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> mImageViewReference;
        int mWidth;
        int mHeight;
        String mFilePath;

        public BitmapWorkerTask(ImageView imageView) {
            mImageViewReference = new WeakReference<ImageView>(imageView);
            mWidth = imageView.getWidth();
            mHeight = imageView.getHeight();
        }

        // バックグラウンドで画像をデコード
        @Override
        protected Bitmap doInBackground(String... params) {
            mFilePath = params[0];
            return decodeSampledBitmapFromFile(mFilePath, mWidth, mHeight);
        }

        // ImageView に Bitmap をセット
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // キャンセルされていたらなにもしない
            if (isCancelled()) {
                bitmap = null;
            }

            if (mImageViewReference != null && bitmap != null) {
                final ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                    // ImageView からタスクを取り出す
                    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                    if (this == bitmapWorkerTask && imageView != null) {
                        // 同じタスクなら ImageView に Bitmap をセット
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static boolean cancelPotentialWork(String filePath, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.mFilePath;
            if (!bitmapData.equals(filePath)) {
                // 以前のタスクをキャンセル
                bitmapWorkerTask.cancel(true);
            } else {
                // 同じタスクがすでに走っているので、このタスクは実行しない
                return false;
            }
        }
        // この ImageView に関連する新しいタスクを実行する
        return true;
    }


    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
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
//                for (int i = 0;i <2 ;i++){
//                    if(mReLink[i].equals(link)){
//                        mReLink[i] = mReLink[3];
//                    }
//                }
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