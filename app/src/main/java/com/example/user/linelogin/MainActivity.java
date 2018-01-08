package com.example.user.linelogin;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "1555693759";
    public static final String base_uri="http://10.70.20.127:8080";
    public static final int REQUEST_CODE = 1;

    Button loginBt;
    List<String>user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DBhelper dbhelper = new DBhelper(getApplicationContext(),"User_info.db",null,1);
        user_info=dbhelper.getData();
        if(!user_info.isEmpty())
       {

           Intent transitionIntent = new Intent(this, LoginResultActivity.class);
           transitionIntent.putExtra("id", user_info.get(0));
       transitionIntent.putExtra("name", user_info.get(1));
          transitionIntent.putExtra("uri", Uri.parse(user_info.get(2)));
          startActivity(transitionIntent);
       }
        loginBt = (Button) findViewById(R.id.login_b);
//      Toast.makeText(this,user_info.size(),Toast.LENGTH_LONG).show();
Log.i("size",String.valueOf(user_info.size()));
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent loginIntent = LineLoginApi.getLoginIntent(view.getContext(), CHANNEL_ID);
                    startActivityForResult(loginIntent, REQUEST_CODE);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult login_result = LineLoginApi.getLoginResultFromIntent(data);

        switch (login_result.getResponseCode()) {
            case SUCCESS:
                Intent transitionIntent = new Intent(this, LoginResultActivity.class);
              //  transitionIntent.putExtra("line_profile", login_result.getLineProfile());
                transitionIntent.putExtra("id", login_result.getLineProfile().getUserId());
                transitionIntent.putExtra("name", login_result.getLineProfile().getDisplayName());
                transitionIntent.putExtra("uri", login_result.getLineProfile().getPictureUrl());

                //  final String accessToken = login_result.getLineCredential().getAccessToken().getAccessToken();
                startActivity(transitionIntent);
                break;

            case CANCEL:

                Log.e("ERROR", "LINE Login Canceled by user");
                break;

            default:

                Log.e("ERROR", "Login Failed");
                Log.e("ERROR", login_result.getErrorData().toString());

        }
    }
}
