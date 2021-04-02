package com.sungkyul.imagesearch.Fragment.Sub;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sungkyul.imagesearch.R;

public class Tourist_Sub extends LinearLayout {

    public Tourist_Sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public Tourist_Sub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tourist_sub,this,true);
    }
}
