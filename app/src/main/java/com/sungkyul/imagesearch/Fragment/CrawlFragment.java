package com.sungkyul.imagesearch.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sungkyul.imagesearch.FileUploadUtils;
import com.sungkyul.imagesearch.R;

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

//CrawlFragment는 Vision AI가 성공하지 못했을때 호출되는 프래그먼트
public class CrawlFragment extends Fragment {

    //https://www.google.co.kr/searchbyimage?image_url=http://220.67.115.212:5601/dongjabang/image/user/naksan_image.jpg&encoded_image=&image_content=&filename=&hl=ko

    String TestURL = "http://220.67.115.212:5601/dongjabang/image/user/naksan_image.jpg";
    String GOOGLE_IMAGE_SEARCH_URL = "https://www.google.co.kr/searchbyimage?image_url=" + TestURL + "&encoded_image=&image_content=&filename=&hl=ko";
    private ImageView crawlImage;

    private TextView test_crawl_text;
    File tempSelectFile;
    String str = "";

    String[] keywordlist;
    int[] frequencylist;

    private TextView freq1,freq2,freq3,freq4,freq5;
    private TextView keyword1,keyword2,keyword3,keyword4,keyword5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crawl,container,false);

        crawlImage = v.findViewById(R.id.crawl_image);
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


        keywordlist = new String[5];
        frequencylist = new int[5];

        //test_crawl_text
        test_crawl_text = v.findViewById(R.id.test_crawl_text);

        byte[] byteArray = getArguments().getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        crawlImage.setImageBitmap(bmp);

        /*
        File storage = getActivity().getCacheDir();
        String fileName = "tt.jpg";
        File imgFile = new File(storage, fileName);
        try{
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            FileUploadUtils.goSend(imgFile);
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }
        Log.d("imgpath", getActivity().getCacheDir()+"/"+fileName);
        */

//        try {
//
//
//            // 선택한 이미지 임시 저장
//            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
//            tempSelectFile = new File(Environment.getExternalStorageDirectory()+"/Pictures/Test/", "temp_" + date + ".jpeg");
//            OutputStream out = new FileOutputStream(tempSelectFile);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            FileUploadUtils.goSend(tempSelectFile);
//        }catch(IOException e){
//            e.printStackTrace();
//        }



        getData();
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
            freq1.setText(frequencylist[0] +"");
            freq2.setText(frequencylist[1] +"");
            freq3.setText(frequencylist[2] +"");
            freq4.setText(frequencylist[3] +"");
            freq5.setText(frequencylist[4] +"");
            keyword1.setText(keywordlist[0]);
            keyword2.setText(keywordlist[1]);
            keyword3.setText(keywordlist[2]);
            keyword4.setText(keywordlist[3]);
            keyword5.setText(keywordlist[4]);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(GOOGLE_IMAGE_SEARCH_URL).get();
                Elements contents = doc.select("#Ycyxxc");
//                doc.getElementById("#Ycyxxc").appendText("http://220.67.115.212:5601/dongjabang/image/user/naksan_image.jpg");
                final Elements title_list = doc.select("div.yuRUbf a h3");

//            str = contents.toString() +"kkkkkkkkkkkkkkkkkkkkkk";
//            str = doc.toString();
                for (Element element : title_list){
                    Log.i("title_list =>", element.text());
                    str = str + " " + element.text();
                }

                HashMap<String,Integer> hs =new HashMap<>();


                KeywordExtractor ke = new KeywordExtractor();
                KeywordList kl = ke.extractKeyword(str,true);
                for (int i = 0; i<kl.size(); i++){
                    Keyword kwrd = kl.get(i);
//                    Log.i("형태소 분석 : " , kwrd.getString() +"\t" + kwrd.getCnt());
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