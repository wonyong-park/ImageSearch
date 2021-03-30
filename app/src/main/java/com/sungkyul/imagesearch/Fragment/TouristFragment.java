package com.sungkyul.imagesearch.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sungkyul.imagesearch.OnSwipeTouchListener;
import com.sungkyul.imagesearch.R;
import com.sungkyul.imagesearch.es.Description;
import com.sungkyul.imagesearch.es.Food;
import com.sungkyul.imagesearch.es.Tourist;

import java.util.List;

public class TouristFragment extends Fragment {

    private static final String TAG = "TouristFragment";

    LinearLayout linear_tourist;

    DescriptionFragment fragment_description;
    FoodFragment fragment_food;
    FragmentManager fm;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tourist,container,false);

        fragment_description = new DescriptionFragment();
        fragment_food = new FoodFragment();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        linear_tourist = (LinearLayout)v.findViewById(R.id.Linear_tourist);

        Bundle bundle = getArguments();
        List<Description> descriptions = bundle.getParcelableArrayList("des_list");
        List<Food> foods = bundle.getParcelableArrayList("food_list");
        List<Tourist> tourists = bundle.getParcelableArrayList("tourist_list");
        Log.i(TAG, descriptions.get(0).toString());
        Log.i(TAG, foods.get(0).toString());
        Log.i(TAG, tourists.get(0).toString());

        linear_tourist.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()){
            @Override
            public void onSwipeRight() {
                fragment_food.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_food);
                transaction.commit();
            }

            @Override
            public void onSwipeLeft() {
                fragment_description.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_description);
                transaction.commit();
            }

        });

        return v;
    }
}