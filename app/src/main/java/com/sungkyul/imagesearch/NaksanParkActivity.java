package com.sungkyul.imagesearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class NaksanParkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naksanpark);

        LinearLayout btnNaksan = findViewById(R.id.naksanparkDes);
        ImageView btnfood1 = findViewById(R.id.naksan_food1);
        ImageView btntou1 = findViewById(R.id.naksan_tou1);

        btnNaksan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NaksanParkActivity.this);
                LayoutInflater factory = LayoutInflater.from(NaksanParkActivity.this);
                final View view = factory.inflate(R.layout.naksandes,null);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(NaksanParkActivity.this);
                LayoutInflater factory = LayoutInflater.from(NaksanParkActivity.this);
                final View view = factory.inflate(R.layout.naksanfood1,null);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(NaksanParkActivity.this);
                LayoutInflater factory = LayoutInflater.from(NaksanParkActivity.this);
                final View view = factory.inflate(R.layout.naksantou1,null);

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
