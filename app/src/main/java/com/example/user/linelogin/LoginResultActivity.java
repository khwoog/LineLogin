package com.example.user.linelogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by USER on 2018-01-05.
 */

public class LoginResultActivity extends AppCompatActivity {
    Intent intent;
    TextView tv;
    ImageView iv;
    Uri pictureUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        final DBhelper dbhelper = new DBhelper(getApplicationContext(), "User_info.db", null, 1);
        intent = getIntent();

        String id = intent.getExtras().getString("id");
        String name = intent.getExtras().getString("name");
        pictureUrl = intent.getParcelableExtra("uri");

        iv = (ImageView) findViewById(R.id.u_pic);
        tv = (TextView) findViewById(R.id.u_id);
        tv.setText(id);
        tv = (TextView) findViewById(R.id.u_name);
        tv.setText(name);

        //DB에 사용자 정보 없을때만 insert
        if (dbhelper.getData().isEmpty())
            dbhelper.insert(id, name, pictureUrl.toString());

        if (pictureUrl != null) {
            //사용자 이미지를 Uri를 이용해 이미지뷰에 로드
            new ImageLoaderTask().execute(pictureUrl.toString());
        }
    }



    public class ImageLoaderTask extends AsyncTask<String, String, Bitmap> {

        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

            } catch (IOException e) {
                Log.i("Error","Error");
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            ImageView profileImageView = (ImageView) findViewById(R.id.u_pic);
            profileImageView.setImageBitmap(bitmap);
        }
    }
}
