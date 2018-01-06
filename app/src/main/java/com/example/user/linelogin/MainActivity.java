package com.example.user.linelogin;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "1555693759";
    public static final int REQUEST_CODE = 1;
    Button loginBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBt = (Button) findViewById(R.id.login_b);
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
                transitionIntent.putExtra("line_profile", login_result.getLineProfile());

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
