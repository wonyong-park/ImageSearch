package com.sungkyul.imagesearch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sungkyul.imagesearch.R;

//NoResultFragment는 VisionAI가 성공하거나 키워드로 검색했을시 엘라스틱 서치에 데이터가 없다면 호출되는 프래그먼트
public class NoResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noresult, container, false);
    }
}