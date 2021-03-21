package com.sungkyul.imagesearch.es;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sungkyul.imagesearch.es.data.Hits;
import com.sungkyul.imagesearch.es.data.SearchHit;
import com.sungkyul.imagesearch.es.data.SearchResponse;
import com.sungkyul.imagesearch.es.data.SimpleSearchCommand;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ESFoodManager implements IFoodManager{

    private static final String SEARCH_FOOD_URL = "http://220.67.115.212:9200/background_food/_search/";
    private static final String RESOURCE_FOOD_URL = "http://220.67.115.212:9200/background_food/";
    private static final String TAG = "FoodSearch";

    private Gson gson;

    public ESFoodManager() {
        this.gson = gson;
    }

    //구체적인 키워드를 가지고 음식점 검색
    @Override
    public Food getFood(String keyword) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(RESOURCE_FOOD_URL + keyword);

        HttpResponse response;

        try {
            response = httpClient.execute(httpGet);
            SearchHit<Food> sr = parseFoodHit(response);
            return sr.getsource();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Food> searchFoods(String searchString, String field) {

        List<Food> result = new ArrayList<Food>();

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

//            //로그확인용
//            HttpEntity entity = response.getEntity();
//            String _response  = EntityUtils.toString(entity);
//            JSONObject jObject = new JSONObject(_response);
//            Log.i(TAG, "response => " + jObject);
//            /////

            SearchResponse<Food> esResponse = parseSearchResponse(response);
            //parseSearchResponse 메소드 == 검색 응답 구문 분석
            Hits<Food> hits = esResponse.getHits();

            if (hits != null) {
                if (hits.getHits() != null) {
                    for (SearchHit<Food> sesr : hits.getHits()) {
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

        HttpPost searchRequest = new HttpPost(SEARCH_FOOD_URL + "?q=title:" + searchString);

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



    private SearchHit<Food> parseFoodHit(HttpResponse response){
        try {
            String json = getEntityContent(response);
            Type searchHitType = new TypeToken<SearchHit<Food>>() {}.getType();

            SearchHit<Food> sr = gson.fromJson(json, searchHitType);
            return sr;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Parses the response of a search
     * //검색 응답 구문 분석
     */
    private SearchResponse<Food> parseSearchResponse(HttpResponse response) throws IOException {
        String json;
        json = getEntityContent(response);
        Log.i(TAG, "json ==> " + json);
        Type searchResponseType = new TypeToken<SearchResponse<Food>>() {
        }.getType();

        gson = new Gson();

        SearchResponse<Food> esResponse = gson.fromJson(json, searchResponseType);


        return esResponse;
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
