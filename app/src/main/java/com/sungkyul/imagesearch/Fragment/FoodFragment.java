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

public class FoodFragment extends Fragment {

    private static final String TAG = "FoodFragment";

    LinearLayout linear_food;

    DescriptionFragment fragment_description;
    TouristFragment fragment_tourist;
    FragmentManager fm;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food,container,false);

        fragment_description = new DescriptionFragment();
        fragment_tourist = new TouristFragment();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        linear_food = (LinearLayout)v.findViewById(R.id.Linear_food);

        Bundle bundle = getArguments();
        List<Description> descriptions = bundle.getParcelableArrayList("des_list");
        List<Food> foods = bundle.getParcelableArrayList("food_list");
        List<Tourist> tourists = bundle.getParcelableArrayList("tourist_list");
        Log.i(TAG, descriptions.get(0).toString());
        Log.i(TAG, foods.get(0).toString());
        Log.i(TAG, tourists.get(0).toString());


        linear_food.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()){
            @Override
            public void onSwipeRight() {
                fragment_description.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_description);
                transaction.commit();
            }

            @Override
            public void onSwipeLeft() {
                fragment_tourist.setArguments(bundle);
                transaction.replace(R.id.frameLayout, fragment_tourist);
                transaction.commit();
            }

        });

        return v;
    }
}