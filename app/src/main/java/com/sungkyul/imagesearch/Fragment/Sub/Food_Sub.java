package com.sungkyul.imagesearch.Fragment.Sub;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sungkyul.imagesearch.R;

//음식점 객체를 화면에 붙혀주는 클래스
public class Food_Sub extends LinearLayout {

    public Food_Sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public Food_Sub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.food_sub,this,true);
    }
}
