package com.example.gaspro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchData extends AsyncTask<Object,String,String> {

    String googleNearByPlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected void onPostExecute(String s) {
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                JSONObject getLocation=jsonObject1.getJSONObject("geometry")
                        .getJSONObject("location");

                String lat= getLocation.getString("lat");
                String lng=getLocation.getString("lng");

                JSONObject getName =jsonArray.getJSONObject(i);
                String name = getName.getString("name");

                JSONObject getVicinity= jsonArray.getJSONObject(i);
                String vicinity = getVicinity.getString("vicinity");

                LatLng latLng= new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                MarkerOptions markerOptions= new MarkerOptions();
                markerOptions.title(name);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.petrolstation));
                markerOptions.position(latLng);
                markerOptions.snippet(vicinity);
                googleMap.addMarker(markerOptions).showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... objects) {

        try{
            googleMap=(GoogleMap) objects[0];
            url=(String) objects[1];
            DownloadUrl downloadUrl= new DownloadUrl();
            googleNearByPlacesData=downloadUrl.retireveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleNearByPlacesData;
    }
}
