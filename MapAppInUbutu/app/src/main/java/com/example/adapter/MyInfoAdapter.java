package com.example.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mapappinubutu.R;
import com.example.model.WeatherPos;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class MyInfoAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private WeatherPos weatherPos;

    public MyInfoAdapter(Activity context, WeatherPos weatherPos) {
        this.context = context;
        this.weatherPos = weatherPos;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.map_item,null);
        TextView txttenDiaDiem=view.findViewById(R.id.txttenDiaDiem);
        TextView txtNhietDo=view.findViewById(R.id.txtNhietDo);
        TextView txtDoAm=view.findViewById(R.id.txtDoAm);
        txttenDiaDiem.setText(weatherPos.getTenDiaDiem());
        txtNhietDo.setText(String.valueOf(weatherPos.getNhietDo()));
        txtDoAm.setText(String.valueOf(weatherPos.getDoAm()));

        return view;

    }
}
