package com.sungkyul.imagesearch.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sungkyul.imagesearch.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

//CrawlFragment는 Vision AI가 성공하지 못했을때 호출되는 프래그먼트
public class CrawlFragment extends Fragment {

    String GOOGLE_IMAGE_SEARCH_URL = "https://www.google.co.kr/imghp?hl=ko";
    String TestURL = "http://220.67.115.212:5601/dongjabang/image/user/naksan_image.jpg";
    private ImageView crawlImage;

    private TextView test_crawl_text;

    String str = "55";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crawl,container,false);

        crawlImage = v.findViewById(R.id.crawl_image);

        //test_crawl_text
        test_crawl_text = v.findViewById(R.id.test_crawl_text);

        byte[] byteArray = getArguments().getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        crawlImage.setImageBitmap(bmp);

        getData();
        test_crawl_text.setText(str);
        return v;
    }

    private void getData(){
        CrawlJsoup jsoupAsyncTask = new CrawlJsoup();
        jsoupAsyncTask.execute();

    }

    private class CrawlJsoup extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(GOOGLE_IMAGE_SEARCH_URL).get();
                Elements contents = doc.select("#Ycyxxc");
//                doc.getElementById("#Ycyxxc").appendText("http://220.67.115.212:5601/dongjabang/image/user/naksan_image.jpg");
                str = contents.toString() +"kkkkkkkkkkkkkkkkkkkkkk";
                str = doc.toString();
                Log.i("str ==> ", str);
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }
}