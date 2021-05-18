package com.sungkyul.imagesearch.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class DescriptionFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DescriptionFragment";

    LinearLayout linear_des;

    FoodFragment fragment_food;
    TouristFragment fragment_tourist;
    FragmentManager fm;
    FragmentTransaction transaction;

    ImageView imageView;
    Bitmap bitmap;

    Button open;

    TextView back_title;
    TextView back_des;
    Button back_tel;
    Button back_open;
    Button back_address;
    String num;
    List<Description> descriptions;

    MapDialogFragment DescriptionMapFragment;


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
        back_tel = (Button)v.findViewById(R.id.back_tel);
        back_open = (Button)v.findViewById(R.id.back_open);
        back_address = (Button)v.findViewById(R.id.back_address);

        Bundle bundle = getArguments();
        descriptions = bundle.getParcelableArrayList("des_list");
        List<Food> foods = bundle.getParcelableArrayList("food_list");
        List<Tourist> tourists = bundle.getParcelableArrayList("tourist_list");
        Log.i(TAG, descriptions.get(0).toString());
        Log.i(TAG, foods.get(0).toString());
        Log.i(TAG, tourists.get(0).toString());

        back_title.setText(descriptions.get(0).getTitle());
        back_des.setText(descriptions.get(0).getBack_des());
        back_tel.setText(descriptions.get(0).getBack_tel());
        back_address.setText(descriptions.get(0).getBack_address());
        num = descriptions.get(0).getBack_tel();

       open = (Button) v.findViewById(R.id.back_open) ;
       open.setOnClickListener((View.OnClickListener) this);

       //전화번호 클릭했을시 이벤트 -> 인텐를 통해서 넘어간다.
        back_tel.setOnClickListener(new View.OnClickListener() {
            String tel = "tel:" + num;
            @Override
            public void onClick(View view) {
                 Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
                 startActivity(intent);
            }
        });

        //주소 클릭시 지도띄워주는 이벤트
        back_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Float latitude = Float.parseFloat(descriptions.get(0).getBack_latitude());
                Float longitude = Float.parseFloat(descriptions.get(0).getBack_longitude());
                String address = descriptions.get(0).getBack_address();
                DescriptionMapFragment = new MapDialogFragment(latitude, longitude,address);
                DescriptionMapFragment.show(getFragmentManager(),null);
            }
        });

        //이미지 불러오는 쓰레드
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(descriptions.get(0).getBack_img());

                    Log.i(TAG, "잘 되고 있나요~~~");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch(MalformedURLException e){
                    Log.i(TAG, "잘 안 되고 있나요~~~");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_open:
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                String noneOpen = descriptions.get(0).getBack_open();
                String open = noneOpen.replace('.','\n');
                builder.setTitle("오픈 시간");
                builder.setMessage(open);
                builder.setNeutralButton("나가기", null);
                builder.create().show();

        }
    }


}