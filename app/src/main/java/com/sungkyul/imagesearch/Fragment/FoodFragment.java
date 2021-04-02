package com.sungkyul.imagesearch.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.sungkyul.imagesearch.Fragment.Sub.Food_Sub;
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

public class FoodFragment extends Fragment {

    private static final String TAG = "FoodFragment";

    LinearLayout linear_food;
    LinearLayout linear_scroll;
    DescriptionFragment fragment_description;
    TouristFragment fragment_tourist;
    FragmentManager fm;
    FragmentTransaction transaction;

    TextView food_title;
    TextView food_des;

    ImageView food_image;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food,container,false);

        fragment_description = new DescriptionFragment();
        fragment_tourist = new TouristFragment();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        linear_food = (LinearLayout)v.findViewById(R.id.Linear_food);
        linear_scroll = (LinearLayout)v.findViewById(R.id.linear_scroll);

        Bundle bundle = getArguments();
        List<Description> descriptions = bundle.getParcelableArrayList("des_list");
        List<Food> foods = bundle.getParcelableArrayList("food_list");
        List<Tourist> tourists = bundle.getParcelableArrayList("tourist_list");
        Log.i(TAG, descriptions.get(0).toString());
        Log.i(TAG, foods.get(0).toString());
        Log.i(TAG, tourists.get(0).toString());


        //
        for (Food food : foods){
            Food_Sub n_layout = new Food_Sub(getActivity().getApplicationContext());
            LinearLayout con = (LinearLayout)v.findViewById(R.id.linear_scroll);
            food_title = (TextView)n_layout.findViewById(R.id.food_title);
            food_des = (TextView)n_layout.findViewById(R.id.food_des);
            food_image = (ImageView)n_layout.findViewById(R.id.food_image);

            //이미지 불러오는 쓰레드
            Thread mThread = new Thread(){
                @Override
                public void run() {
                    try {
                        URL url = new URL(food.getFood_img());

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
                food_image.setImageBitmap(bitmap);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            if(food_title != null){
                food_title.setText(food.getFood_key());
                food_des.setText("주소 : " + food.getFood_address() +
                        "\n영업 시간 : " + food.getFood_open() +
                        "\n전화 번호 : " + food.getFood_tel() +
                        "\n메뉴 : " + food.getFood_menu() +
                        "\n순위 : " + food.getFood_rank() +
                        "\n카테고리 : " + food.getFood_category()
                );
            }


            con.addView(n_layout);
        }

        //

        linear_food.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()){
            @Override
            public void onSwipeRight() {
                fragment_description.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_description);
                transaction.commit();
            }

            @Override
            public void onSwipeLeft() {
                fragment_tourist.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_tourist);
                transaction.commit();
            }

        });

        linear_scroll.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()){
            @Override
            public void onSwipeRight() {
                fragment_description.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_description);
                transaction.commit();
            }

            @Override
            public void onSwipeLeft() {
                fragment_tourist.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_tourist);
                transaction.commit();
            }

        });

        return v;
    }

}