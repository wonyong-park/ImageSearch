package com.sungkyul.imagesearch;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//Vision API의 결과는 영어로 나와 한국어로 번역하기 위한 클래스
public class NaverTranslateTask extends AsyncTask<String, Void, String> {

    public String resultText;
    //Naver
    String clientId = "R3cNZhePZPnGJH1qjueg";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "c9DkDfyza7";//애플리케이션 클라이언트 시크릿값";

    //언어선택도 나중에 사용자가 선택할 수 있게 옵션 처리해 주면 된다.
    String sourceLang = "en";
    String targetLang = "ko";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //AsyncTask 메인처리
    @Override
    protected String doInBackground(String... strings) {

        String sourceText = strings[0];

        try {
            String text = URLEncoder.encode(sourceText, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            //올라온 결과 처리
            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(response.toString())
                    //원하는 데이터 까지 찾아 들어간다.
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("result");
            //안드로이드 객체에 담기
            TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);

            // 알고싶은 정보 : 번역한 결과 ==> items.getTranslatedText();
            return items.getTranslatedText();

        } catch (Exception e) {
            Log.d("error", e.getMessage());
            return null;
        }
    }

    //번역된 결과를 받아서 처리
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //최종 결과 처리부
        //Log.d("background result", s.toString()); //네이버에 보내주는 응답결과가 JSON 데이터이다.

        //JSON데이터를 자바객체로 변환해야 한다.
        //Gson을 사용할 것이다.

        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();
        JsonElement rootObj = parser.parse(s.toString())
                //원하는 데이터 까지 찾아 들어간다.
                .getAsJsonObject().get("message")
                .getAsJsonObject().get("result");
        //안드로이드 객체에 담기
        TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);
    }



    //자바용 그릇
    private class TranslatedItem {
        String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }
    }
}
