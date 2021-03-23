package com.sungkyul.imagesearch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sungkyul.imagesearch.R;

//CrawlFragment는 Vision AI가 성공하지 못했을때 호출되는 프래그먼트
public class CrawlFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crawl, container, false);
    }
}