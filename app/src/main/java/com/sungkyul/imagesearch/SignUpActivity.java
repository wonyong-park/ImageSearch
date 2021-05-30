package com.sungkyul.imagesearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignUpActivity extends Activity {

    private EditText edit_id,edit_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_join);

        edit_id = (EditText)findViewById(R.id.join_id);
        edit_pass = (EditText)findViewById(R.id.join_password);


        Button loginCheck = (Button)findViewById(R.id.check_button);
        loginCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String result;
                    CustomTask task = new CustomTask();
                    result = task.execute(edit_id.getText().toString()).get();
                    Log.i("리턴 값",result);
                    Log.i("id 값",edit_id.getText().toString());

                    if(result.equals("     false")){
                        Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다", Toast.LENGTH_SHORT).show(); // 실행할 코드
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "사용 불가능한 ID 입니다", Toast.LENGTH_SHORT).show(); // 실행할 코드
                    }
                } catch (Exception e) {

                }
            }
        });

        Button signUp = (Button)findViewById(R.id.join_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String result;
                    CustomTask1 task = new CustomTask1();
                    result = task.execute(edit_id.getText().toString(),edit_pass.getText().toString()).get();
                    Log.i("리턴 값",result);
                    SignUpActivity signUpActivity = (SignUpActivity)SignUpActivity.this;
                    signUpActivity.finish();
                } catch (Exception e) {

                }
            }
        });

        Button end = (Button)findViewById(R.id.delete);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity signUpActivity = (SignUpActivity)SignUpActivity.this;
                signUpActivity.finish();
            }
        });

    }
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://220.67.115.212:5601/dongjabang/getIdByCheck.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
}

class CustomTask1 extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL("http://220.67.115.212:5601/dongjabang/insertNewID.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "id="+strings[0]+"&pwd="+strings[1];
            osw.write(sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();

            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}
