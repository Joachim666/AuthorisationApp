package com.sdacademy.otto.joachim.authorisationapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText mlogin;
    EditText mpassword;
    Button mbutton;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlogin = (EditText) findViewById(R.id.loginTextView);
        mpassword = (EditText) findViewById(R.id.passTextView);
        mbutton = (Button) findViewById(R.id.button);
        result = (TextView) findViewById(R.id.result);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData(mlogin.getText().toString(),
                        mpassword.getText().toString());

            }
        });
    }
    private void getUserData(String login, String password){
        String credentials = login + ":" + password;
        byte[] credentialsBytes = credentials.getBytes();
        String encodedCredentials = Base64.encodeToString(
                credentialsBytes, Base64.NO_WRAP);

        new GetUserDAtaTask(encodedCredentials).execute();
    }

    private class GetUserDAtaTask extends AsyncTask<Void,Void,String>{

        String token;
        public GetUserDAtaTask(String token){
            this.token = token;
        }

        @Override
        protected String doInBackground(Void... params) {
             Request.Builder builder = new Request.Builder();
            builder.addHeader("Authorization","Basic " + token);
            builder.url("https://api.github.com/user");
            builder.get();


            Request request = builder.build();
            OkHttpClient client = new OkHttpClient();

            try {
                Response response = client.newCall(request).execute();
                return  response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "Blad sieci";

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result.setText(s);
        }
    }
}


