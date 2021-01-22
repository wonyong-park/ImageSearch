package com.sungkyul.imagesearch;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NoResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noresult);

        Intent getIntent = getIntent();
        String keyword = getIntent.getStringExtra("value");
        EditText edit_noresult_keword = findViewById(R.id.edit_noresult_keword);
        edit_noresult_keword.setText(keyword);
    }
}
