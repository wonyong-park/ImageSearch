package com.sungkyul.imagesearch.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.imagesearch.OnSwipeTouchListener;
import com.sungkyul.imagesearch.R;
import com.sungkyul.imagesearch.es.Description;
import com.sungkyul.imagesearch.es.Food;
import com.sungkyul.imagesearch.es.Tourist;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DescriptionFragment extends Fragment {

    private static final String TAG = "DescriptionFragment";

    LinearLayout linear_des;

    FoodFragment fragment_food;
    TouristFragment fragment_tourist;
    FragmentManager fm;
    FragmentTransaction transaction;

    ImageView imageView;
    Bitmap bitmap;

    TextView back_title;
    TextView back_des;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_description,container,false);

        fragment_food = new FoodFragment();
        fragment_tourist = new TouristFragment();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        linear_des = (LinearLayout)v.findViewById(R.id.Linear_des);
        imageView = (ImageView)v.findViewById(R.id.back_image);
        back_title = (TextView)v.findViewById(R.id.back_title);
        back_des = (TextView)v.findViewById(R.id.back_des);

        //이미지 불러오는 쓰레드
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://220.67.115.212:5601/dongjabang/image/naksan_image.jpg");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try{
            mThread.join();
            imageView.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        Bundle bundle = getArguments();
        List<Description> descriptions = bundle.getParcelableArrayList("des_list");
        List<Food> foods = bundle.getParcelableArrayList("food_list");
        List<Tourist> tourists = bundle.getParcelableArrayList("tourist_list");
        Log.i(TAG, descriptions.get(0).toString());
        Log.i(TAG, foods.get(0).toString());
        Log.i(TAG, tourists.get(0).toString());

        back_title.setText(descriptions.get(0).getTitle());
        back_des.setText(descriptions.get(0).getBack_des() +
                "\n 주소 : " + descriptions.get(0).getBack_address() +
                "\n 오픈 시간 : " + descriptions.get(0).getBack_open() +
                "\n 전화 번호 : " + descriptions.get(0).getBack_tel() +
                "\n 리뷰 : " + descriptions.get(0).getBack_review());


        linear_des.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()){
            @Override
            public void onSwipeRight() {
                fragment_tourist.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_tourist);
                transaction.commit();
            }

            @Override
            public void onSwipeLeft() {
                fragment_food.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_food);
                transaction.commit();
            }

        });

        return v;
    }



}