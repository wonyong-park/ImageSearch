package com.sungkyul.imagesearch.es;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sungkyul.imagesearch.es.data.Hits;
import com.sungkyul.imagesearch.es.data.SearchHit;
import com.sungkyul.imagesearch.es.data.SearchResponse;
import com.sungkyul.imagesearch.es.data.SimpleSearchCommand;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//엘라스틱서치와 관광지 객체를 연결해주는 매니저
public class ESTouristManager implements ITouristManager{

    private static final String SEARCH_Tourist_URL = "http://220.67.115.212:9200/background_tourist/_search/";
    private static final String RESOURCE_Tourist_URL = "http://220.67.115.212:9200/background_tourist/";
    private static final String TAG = "DescriptionSearch";

    private Gson gson;

    public ESTouristManager() {
        this.gson = gson;
    }

    @Override
    public Tourist getTourist(String keyword) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(RESOURCE_Tourist_URL + keyword);

        HttpResponse response;

        try {
            response = httpClient.execute(httpGet);
            SearchHit<Tourist> sr = parseTouristHit(response);
            return sr.getsource();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Tourist> searchTourists(String searchString, String field) {

        List<Tourist> result = new ArrayList<Tourist>();

        Log.i(TAG, "searchString ==> " + searchString);
        Log.i(TAG, "field ==> " + field);

        if (searchString == null || "".equals(searchString)) {
            searchString = "*";
        }

        //http request 객체 사용
        HttpClient httpClient = new DefaultHttpClient();

        try{
            HttpPost searchRequest = createSearchRequest(searchString, field);
            HttpResponse response = httpClient.execute(searchRequest);

            SearchResponse<Tourist> esResponse = parseSearchResponse(response);
            //parseSearchResponse 메소드 == 검색 응답 구문 분석
            Hits<Tourist> hits = esResponse.getHits();

            if (hits != null) {
                if (hits.getHits() != null) {
                    for (SearchHit<Tourist> sesr : hits.getHits()) {
                        result.add(sesr.getsource());
                        Log.i(TAG, "sesr.toStirng() --> " + sesr.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private HttpPost createSearchRequest(String searchString, String field)	throws UnsupportedEncodingException { //UnsupportedEncodingException == 키값의 길이가 16이하일 경우 발생

        HttpPost searchRequest = new HttpPost(SEARCH_Tourist_URL + "?q=title:" + searchString);

        String[] fields = null;
        if (field != null) {
            fields = new String[1];
            fields[0] = field;
        }

        SimpleSearchCommand command = new SimpleSearchCommand(searchString,	fields);

        String query = command.getJsonCommand();
        Log.i(TAG, "Json command: " + query);

        StringEntity stringEntity;
        stringEntity = new StringEntity(query);

        searchRequest.setHeader("Accept", "application/json");
        searchRequest.setHeader("Content-type", "application/json");
        searchRequest.setEntity(stringEntity);

        return searchRequest;
    }

    private SearchResponse<Tourist> parseSearchResponse(HttpResponse response) throws IOException {
        String json;
        json = getEntityContent(response);
        Log.i(TAG, "json ==> " + json);
        Type searchResponseType = new TypeToken<SearchResponse<Tourist>>() {
        }.getType();

        gson = new Gson();

        SearchResponse<Tourist> esResponse = gson.fromJson(json, searchResponseType);


        return esResponse;
    }

    private SearchHit<Tourist> parseTouristHit(HttpResponse response){
        try {
            String json = getEntityContent(response);
            Type searchHitType = new TypeToken<SearchHit<Tourist>>() {}.getType();

            SearchHit<Tourist> sr = gson.fromJson(json, searchHitType);
            return sr;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets content from an HTTP response
     * //Http 응답으로부터 내용을 받는다
     */
    public String getEntityContent(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

}
