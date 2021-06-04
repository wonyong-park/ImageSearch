package com.sungkyul.imagesearch.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sungkyul.imagesearch.ListViewAdapter;
import com.sungkyul.imagesearch.R;
import com.sungkyul.imagesearch.es.Description;
import com.sungkyul.imagesearch.es.Food;
import com.sungkyul.imagesearch.es.Tourist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//키워드에 대한 설명 프래그먼트
public class DescriptionFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DescriptionFragment";

    LinearLayout linear_des;

    FoodFragment fragment_food;
    TouristFragment fragment_tourist;
    FragmentManager fm;
    FragmentTransaction transaction;

    ImageView imageView;
    Bitmap bitmap;

    public static ListViewAdapter listviewadapter;
    public static ListView listview;
    Activity activity;


    Button open;

    EditText inputList;

    TextView back_title;
    TextView back_des;
    Button back_tel;
    Button button1;
    Button button2;
    Button back_open;
    Button back_address;
    String num;
    List<Description> descriptions;

    MapDialogFragment DescriptionMapFragment;
    public static DescriptionFragment newInstance() {
        DescriptionFragment tab1 = new DescriptionFragment();
        return tab1;
    }


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
//        inputList = (EditText) v.findViewById(R.id.listinput);

        listviewadapter = new ListViewAdapter(getActivity());

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


//        // 아이템 추가한다.
//        button1 = (Button)v.findViewById(R.id.button1);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // ListView에 입력할 문자열
//                listviewadapter.setName(inputList.getText().toString());
//                inputList.setText("");
//            }
//        });
//
//        button2 = (Button)v.findViewById(R.id.button2);
//        button2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // 뷰 호출
//                v = getActivity().getLayoutInflater().inflate(R.layout.listview, null);
//                // 해당 뷰에 리스트뷰 호출
//                listview = (ListView)v.findViewById(R.id.listview);
//                // 리스트뷰에 어뎁터 설정
//                listview.setAdapter(listviewadapter);
//                // 다이얼로그 생성
//                AlertDialog.Builder listViewDialog = new AlertDialog.Builder(getActivity());
//                // 리스트뷰 설정된 레이아웃
//                listViewDialog.setView(v);
//                // 확인버튼
//                listViewDialog.setPositiveButton("확인", null);
//                // 타이틀
//                listViewDialog.setTitle("리뷰");
//                // 다이얼로그 보기
//                listViewDialog.show();
//            }
//        });

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
class CustomTask extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL("http://220.67.115.212:5601/dongjabang/readComments.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "id="+strings[0];
            osw.write(sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();

            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}