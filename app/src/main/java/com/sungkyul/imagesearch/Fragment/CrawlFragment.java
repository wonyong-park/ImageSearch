package com.sungkyul.imagesearch.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sungkyul.imagesearch.FileUploadUtils;
import com.sungkyul.imagesearch.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.netty.shaded.io.netty.handler.codec.http2.Http2MultiplexHandler;
import okhttp3.MultipartBody;

//CrawlFragment는 Vision AI가 성공하지 못했을때 호출되는 프래그먼트
public class CrawlFragment extends Fragment {

    //https://www.google.co.kr/searchbyimage?image_url=http://220.67.115.212:5601/dongjabang/image/user/naksan_image.jpg&encoded_image=&image_content=&filename=&hl=ko

    String initURL = "";
    String GOOGLE_IMAGE_SEARCH_URL = "https://www.google.co.kr/searchbyimage?image_url=" + initURL + "&encoded_image=&image_content=&filename=&hl=ko";
    private ImageView crawlImage;

    //형태소 분석할 문자를 담아놓는 String
    String str = "";

    String[] keywordlist;
    int[] frequencylist;

    private TextView rank1,rank2,rank3,rank4,rank5;
    private TextView freq1,freq2,freq3,freq4,freq5;
    private TextView keyword1,keyword2,keyword3,keyword4,keyword5;
    private LinearLayout linear_result;
    private TextView txtNokeyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crawl,container,false);

        linear_result = v.findViewById(R.id.linear_result);
        crawlImage = v.findViewById(R.id.crawl_image);
        rank1 = v.findViewById(R.id.rank_1);
        rank2 = v.findViewById(R.id.rank_2);
        rank3 = v.findViewById(R.id.rank_3);
        rank4 = v.findViewById(R.id.rank_4);
        rank5 = v.findViewById(R.id.rank_5);
        keyword1 = v.findViewById(R.id.keyword1);
        keyword2 = v.findViewById(R.id.keyword2);
        keyword3 = v.findViewById(R.id.keyword3);
        keyword4 = v.findViewById(R.id.keyword4);
        keyword5 = v.findViewById(R.id.keyword5);
        freq1 = v.findViewById(R.id.freq1);
        freq2 = v.findViewById(R.id.freq2);
        freq3 = v.findViewById(R.id.freq3);
        freq4 = v.findViewById(R.id.freq4);
        freq5 = v.findViewById(R.id.freq5);
        txtNokeyword = v.findViewById(R.id.txtNokeyword);

        //초기 INVISIBLE
        linear_result.setVisibility(View.INVISIBLE);
        txtNokeyword.setVisibility(View.INVISIBLE);

        keywordlist = new String[5];
        frequencylist = new int[5];

        byte[] byteArray = getArguments().getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        crawlImage.setImageBitmap(bmp);
        getArguments().remove("image");
        str = "";
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String fileName = "temp"+ date + ".jpg";
        GOOGLE_IMAGE_SEARCH_URL = "https://www.google.co.kr/searchbyimage?image_url=http://220.67.115.212:5601/dongjabang/image/user/" + fileName + "&encoded_image=&image_content=&filename=&hl=ko";
        try{

            File storage = getActivity().getCacheDir();
            File imgFile = new File(storage, fileName);
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            FileUploadUtils.goSend(imgFile);
            //딜레이 10초(업로드한 파일을 검색이 가능하도록하기 위한 시간)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            },10000);

        }catch (FileNotFoundException e){

        }catch (IOException e){

        }


        return v;
    }

    private void getData(){
        CrawlJsoup jsoupAsyncTask = new CrawlJsoup();
        jsoupAsyncTask.execute();

    }


    private class CrawlJsoup extends AsyncTask<Void, Void, Void>{

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {

            if(frequencylist[0] != 0 && !keywordlist[0].equals("")){
                if(frequencylist[0] != 0){
                    freq1.setText(frequencylist[0] +"");
                    keyword1.setText(keywordlist[0]);
                }else{
                    rank1.setVisibility(View.INVISIBLE);
                    freq1.setVisibility(View.INVISIBLE);
                }

                if(frequencylist[1] != 0){
                    freq2.setText(frequencylist[1] +"");
                    keyword2.setText(keywordlist[1]);
                }else{
                    rank2.setVisibility(View.INVISIBLE);
                    freq2.setVisibility(View.INVISIBLE);
                }

                if(frequencylist[2] != 0) {
                    freq3.setText(frequencylist[2] + "");
                    keyword3.setText(keywordlist[2]);
                }else{
                    rank3.setVisibility(View.INVISIBLE);
                    freq3.setVisibility(View.INVISIBLE);
                }

                if(frequencylist[3] != 0) {
                    freq4.setText(frequencylist[3] + "");
                    keyword4.setText(keywordlist[3]);
                }else{
                    rank4.setVisibility(View.INVISIBLE);
                    freq4.setVisibility(View.INVISIBLE);
                }

                if(frequencylist[4] != 0){
                    freq5.setText(frequencylist[4] +"");
                    keyword5.setText(keywordlist[4]);
                }else{
                    rank5.setVisibility(View.INVISIBLE);
                    freq5.setVisibility(View.INVISIBLE);
                }

                linear_result.setVisibility(View.VISIBLE);

            }else{
                txtNokeyword.setVisibility(View.VISIBLE);
            }


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.i("SEARCH_URL", GOOGLE_IMAGE_SEARCH_URL);
                Document doc = Jsoup.connect(GOOGLE_IMAGE_SEARCH_URL).get();
                Elements contents = doc.select("#Ycyxxc");
                final Elements title_list = doc.select("div.yuRUbf a h3");

                for (Element element : title_list){
                    Log.i("title_list =>", element.text());
                    str = str + " " + element.text();
                }

                HashMap<String,Integer> hs =new HashMap<>();

                //형태소 분석 부분
                KeywordExtractor ke = new KeywordExtractor();
                KeywordList kl = ke.extractKeyword(str,true);
                for (int i = 0; i<kl.size(); i++){
                    Keyword kwrd = kl.get(i);
                    hs.put(kwrd.getString(), kwrd.getCnt());
                }

                //정렬 부분
                List<Map.Entry<String,Integer>> list_entries = new ArrayList<Map.Entry<String,Integer>>(hs.entrySet());
                Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });

                int count = 0;

                //배열에 할당 부분
                for (Map.Entry<String, Integer> entry : list_entries){
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                    keywordlist[count] = entry.getKey();
                    frequencylist[count] = (int) entry.getValue();
                    count++;
                    if(count == 5) break;
                }



            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }
}