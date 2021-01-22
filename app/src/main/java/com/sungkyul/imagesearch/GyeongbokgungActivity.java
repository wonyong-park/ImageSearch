package com.sungkyul.imagesearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GyeongbokgungActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyeongbokgung);

        LinearLayout btnGyongbok = findViewById(R.id.btnGyongbok);
        ImageView btnfood1 = findViewById(R.id.btnfood1);
        ImageView btntou1 = findViewById(R.id.btntou1);

        btnGyongbok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GyeongbokgungActivity.this);
                LayoutInflater factory = LayoutInflater.from(GyeongbokgungActivity.this);
                final View view = factory.inflate(R.layout.gyongbokdes,null);

//                builder.setTitle("경복궁");
                builder.setView(view);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnfood1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GyeongbokgungActivity.this);
                LayoutInflater factory = LayoutInflater.from(GyeongbokgungActivity.this);
                final View view = factory.inflate(R.layout.gyongbokfood1,null);

//                builder.setTitle("동대문 흥부찜닭");
                builder.setView(view);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btntou1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GyeongbokgungActivity.this);
                LayoutInflater factory = LayoutInflater.from(GyeongbokgungActivity.this);
                final View view = factory.inflate(R.layout.gyongboktou1,null);

//                builder.setTitle("동대문 흥부찜닭");
                builder.setView(view);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
