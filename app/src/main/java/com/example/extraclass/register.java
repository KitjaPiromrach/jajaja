package com.example.extraclass;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class register extends AppCompatActivity {

    private EditText etdisplayRE;
    private EditText etusernameRE;
    private EditText etpasswordRE;
    private EditText etconpasswordRE;
    private Button btnregis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        bindWidget();
        setListener();
    }
    private void setListener() {
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(validate()){
            new Regis(etusernameRE.getText().toString(),
                    etpasswordRE.getText().toString(),
                    etconpasswordRE.getText().toString(),
                    etdisplayRE.getText().toString()).execute();
            }else {
                Toast.makeText(register.this,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
            }

            }
        });

    }

    private boolean validate(){
        String username = etusernameRE.getText().toString();
        String password = etpasswordRE.getText().toString();
        String passwordConfirm = etconpasswordRE.getText().toString();
        String displayName = etdisplayRE.getText().toString();

        if (username.isEmpty())
            return false;

        if (password.isEmpty())
            return false;

        if (!passwordConfirm.equals(password))
            return false;

        if (displayName.isEmpty())
            return false;

        return true;
    }


    private void bindWidget() {
        etdisplayRE  =(EditText)findViewById(R.id.etdisplayRE);
        etusernameRE  =(EditText)findViewById(R.id.etusernameRE);
        etpasswordRE  =(EditText)findViewById(R.id.etpasswordRE);
        etconpasswordRE  =(EditText)findViewById(R.id.etconpasswordRE);
        btnregis = (Button)findViewById(R.id.btnregis);
    }

    private class Regis extends AsyncTask<Void,Void,String> {

        private String username;
        private String password;
        private String passwordcon;
        private String dispalyname;

        public Regis(String dispalyname, String username, String password, String passwordcon) {
            this.dispalyname = dispalyname;
            this.username = username;
            this.password = password;
            this.passwordcon = passwordcon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request;
            Response response;

            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("password_con", passwordcon)
                    .add("display_name", dispalyname)
                    .build();

            request = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(requestBody)
                    .build();
            try {
                response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    return response.body().string();
                }

            } catch (IOException ex) {
                ex.printStackTrace();

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(register.this, s, Toast.LENGTH_SHORT).show();


            try {
                JSONObject rootobj = new JSONObject(s);
                if (rootobj.has("result")) {
                    JSONObject resultobj = rootobj.getJSONObject("result");
                    if (resultobj.getInt("result") == 1) {
                        Toast.makeText(register.this, resultobj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(register.this, resultobj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }}
