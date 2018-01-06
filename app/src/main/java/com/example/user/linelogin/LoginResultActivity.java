package com.example.user.linelogin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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

import com.linecorp.linesdk.LineProfile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by USER on 2018-01-05.
 */

public class LoginResultActivity extends AppCompatActivity {
    LineProfile userProfile;
    Intent intent;
    TextView tv;
    ImageView iv;
    Uri pictureUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        intent = getIntent();
        userProfile = intent.getParcelableExtra("line_profile");

        String id = userProfile.getUserId();
        String name = userProfile.getDisplayName();

        iv = (ImageView) findViewById(R.id.u_pic);
        tv = (TextView) findViewById(R.id.u_id);
        tv.setText(id);
        tv = (TextView) findViewById(R.id.u_name);
        tv.setText(name);

        Uri pictureUrl = userProfile.getPictureUrl();

        if (pictureUrl != null) {
            new ImageLoaderTask().execute(pictureUrl.toString());
        }
    }

    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public class ImageLoaderTask extends AsyncTask<String, String, Bitmap> {

        final static String TAG = "ImageLoaderTask";

        protected void onPreExecute() {
            lockScreenOrientation();
        }

        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            ImageView profileImageView = (ImageView) findViewById(R.id.u_pic);
            profileImageView.setImageBitmap(bitmap);
            unlockScreenOrientation();
        }
    }
}
