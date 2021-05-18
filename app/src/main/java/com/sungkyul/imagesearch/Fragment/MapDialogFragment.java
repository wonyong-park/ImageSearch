package com.sungkyul.imagesearch.Fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sungkyul.imagesearch.R;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback{

    GoogleMap mMap;
    private static View rootView;
    float latitude; //위도
    float longitude; //경도
    String address;
    static SupportMapFragment mapFragment;
    Button btnOk;
    static Marker marker;

    public MapDialogFragment(float latitude, float longitude, String address) {
        // Required empty public constructor
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView != null){
            ViewGroup parent = (ViewGroup)rootView.getParent();
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.maps, container, false);
            mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch (InflateException e){

        }
        btnOk = (Button)rootView.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(MapDialogFragment.this).commit();
                fragmentManager.popBackStack();
                onDestroy();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        mapFragment.getMapAsync(this);
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //마커가 기존에 있다면 지우기위한 부분
        if(marker != null) marker.remove();
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        LatLng latLng = new LatLng(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        Log.i("OnMapReady", "latitude : " + latitude + "\tlongitude : " + longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(address);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        marker = mMap.addMarker(markerOptions);
    }

}