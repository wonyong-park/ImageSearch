package com.sungkyul.imagesearch.Fragment;

import android.app.AlertDialog;
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
import com.sungkyul.imagesearch.Fragment.Sub.Tourist_Sub;
import com.sungkyul.imagesearch.OnSwipeTouchListener;
import com.sungkyul.imagesearch.R;
import com.sungkyul.imagesearch.es.Description;
import com.sungkyul.imagesearch.es.Food;
import com.sungkyul.imagesearch.es.Tourist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TouristFragment extends Fragment {

    private static final String TAG = "TouristFragment";

    LinearLayout linear_tourist;
    LinearLayout linear_scroll_tou;

    DescriptionFragment fragment_description;
    FoodFragment fragment_food;
    FragmentManager fm;
    FragmentTransaction transaction;

    TextView tourist_title;
    ImageView tourist_images[];
    ImageView tourist_opens[];
    ImageView tourist_maps[];

    MapDialogFragment TouristMapFragment;

    public static TouristFragment newInstance() {
        TouristFragment tab3 = new TouristFragment();
        return tab3;
    }

    Bitmap bitmap;
    static int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tourist,container,false);

        fragment_description = new DescriptionFragment();
        fragment_food = new FoodFragment();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        linear_tourist = (LinearLayout)v.findViewById(R.id.Linear_tourist);
        linear_scroll_tou = (LinearLayout)v.findViewById(R.id.linear_scroll_tou);

        Bundle bundle = getArguments();
        List<Description> descriptions = bundle.getParcelableArrayList("des_list");
        List<Food> foods = bundle.getParcelableArrayList("food_list");
        List<Tourist> tourists = bundle.getParcelableArrayList("tourist_list");
        Log.i(TAG, descriptions.get(0).toString());
        Log.i(TAG, foods.get(0).toString());
        Log.i(TAG, tourists.get(0).toString());

        //tourist에 들어있는 객체만큼 만들어주기
        count = 0;
        tourist_images = new ImageView[tourists.size()];
        tourist_opens = new ImageView[tourists.size()];
        tourist_maps = new ImageView[tourists.size()];

        //
        for (Tourist tourist : tourists){
            Tourist_Sub n_layout = new Tourist_Sub(getActivity().getApplicationContext());
            LinearLayout con = (LinearLayout)v.findViewById(R.id.linear_scroll_tou);
            tourist_title = (TextView)n_layout.findViewById(R.id.tourist_title);
            tourist_images[count] = (ImageView)n_layout.findViewById(R.id.tourist_image);
            tourist_opens[count] = (ImageView)n_layout.findViewById(R.id.tourist_open);
            tourist_maps[count] = (ImageView)n_layout.findViewById(R.id.tourist_map);


            //이미지 불러오는 쓰레드
            Thread mThread = new Thread(){
                @Override
                public void run() {
                    try {
                        URL url = new URL(tourist.getTourist_img());

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
                tourist_images[count].setImageBitmap(bitmap);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            if(tourist_title != null){
                tourist_title.setText(tourist.getTourist_key());
            }

            //이미지 버튼 클릭시 이벤트
            int index = count;
            tourist_images[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(),tourists.get(index).getTourist_key() + " 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            //오픈시간 이미지 클릭시
            tourist_opens[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity().getApplicationContext(),foods.get(index).getFood_key() + " 의 오픈시간이 클릭됨.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    String noneOpen = tourists.get(index).getTourist_open();
                    String open = noneOpen.replace('.','\n');
                    builder.setTitle("오픈 시간");
                    builder.setMessage(open);
                    builder.setNeutralButton("나가기", null);
                    builder.create().show();
                }
            });

            //지도 이미지 클릭시
            tourist_maps[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Float latitude = Float.parseFloat(tourists.get(index).getTourist_latitude());
                    Float longitude = Float.parseFloat(tourists.get(index).getTourist_longitude());
                    String address = tourists.get(index).getTourist_address();
//                    Toast.makeText(getActivity().getApplicationContext(),latitude + "," + longitude, Toast.LENGTH_SHORT).show();

                    TouristMapFragment = new MapDialogFragment(latitude, longitude,address);
                    TouristMapFragment.show(getFragmentManager(),null);

                }
            });

            count++;
            con.addView(n_layout);
        }

        linear_scroll_tou.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()){

        });



        return v;
    }
}