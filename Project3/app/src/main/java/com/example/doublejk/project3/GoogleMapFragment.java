package com.example.doublejk.project3;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView addressTv;
    private Marker marker;
    private Restaurant restaurant;
    private SQLiteHelper sqliteHelper;
    private ArrayList<Restaurant> restaurants;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    public static GoogleMapFragment newInstance(SQLiteHelper sqliteHelper, Restaurant restaurant) {
        GoogleMapFragment googleMapFragment = new GoogleMapFragment();
        googleMapFragment.sqliteHelper = sqliteHelper;

        Bundle args = new Bundle();
        args.putParcelable("Restaurant", restaurant);
        googleMapFragment.setArguments(args);

        return googleMapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        addressTv = (TextView) v.findViewById(R.id.addressTv);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        return v;
    }

    public LatLng geoCoding(String address) {
        List<Address> list = null;
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            list = geocoder.getFromLocationName(address, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
    }

    public String geoCoding(LatLng latLng) {
        List<Address> list = null;
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.get(0).getAddressLine(0).toString();
    }

    //마커 생성
    public Marker addMarker(LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true)
                .title(restaurant.getTitle()).snippet(restaurant.getNumber() + "\n" + restaurant.getContent());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); // 마커생성위치로 즉시이동
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        return mMap.addMarker(markerOptions);
    }

    public void addMarkers(ArrayList<Restaurant> restaurants) {
        if (restaurants.size() > 1) {
            for (int i = 0; i < restaurants.size() - 1; i++) {
                Log.d("보자", restaurants.get(i).getAddress());
                MarkerOptions markerOptions = new MarkerOptions().position(geoCoding(restaurants.get(i).getAddress()))
                        .draggable(true).title(restaurants.get(i).getTitle()).snippet(restaurants.get(i).getNumber());
                //MarkerOptions markerOptions = new MarkerOptions().position(geoCoding(restaurants.get(i).getAddress()));
                mMap.addMarker(markerOptions);
            }
        }
    }

    public void showMarkers() {
        restaurants = sqliteHelper.getRestaurants(restaurants);
        addMarkers(restaurants);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        sqliteHelper.select(); //db확인
        showMarkers(); //db에 있는 모든 마커 표시
        mMap.setMyLocationEnabled(true); // 자기위치 표시
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //방금 등록한 마커 표시
        Bundle args = getArguments();
        restaurant = args.getParcelable("Restaurant");
        addressTv.setText(restaurant.getAddress());
        marker = addMarker(geoCoding(restaurant.getAddress()));

        //맵 클릭시
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.remove();
                marker = addMarker(latLng);
                addressTv.setText(geoCoding(latLng));
            }
        });

        //마커 클릭시
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else
                    marker.showInfoWindow();
                return false;
            }
        });

        //마커 드래그시
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                addressTv.setText(geoCoding(marker.getPosition()));
            }
        });
    }
}
