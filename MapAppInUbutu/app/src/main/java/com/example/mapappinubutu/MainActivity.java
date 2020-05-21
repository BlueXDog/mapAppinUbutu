package com.example.mapappinubutu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adapter.MyInfoAdapter;
import com.example.mapappinubutu.R;
import com.example.model.WeatherInfo;
import com.example.model.WeatherPos;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList<String>dsType;
    ArrayAdapter<String> adapterType;
    Spinner spMap;
    ProgressDialog progressDialog;
    LocationRequest mLastLocationRequest;
    Marker mCurrLocationMarker;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private static final int SIGN_IN_REQUEST_CODE=111;

    private DatabaseReference mDatabase;

    private WeatherInfo bachKhoaInfo,ktqdInfo,ftuInfo;


    //khoi tao fake weatherpost bach khoa
   // WeatherPos posBachKhoa = new WeatherPos("Bach Khoa",21.004801,105.846108,15,12,1224);
    //WeatherPos posVinhHung = new WeatherPos("Vinh Hung",21.004801,105.846108,15,12,1224);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addControls();
        addEvents();
    }

    private void addEvents() {
        spMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xuLyDoiCheDoHienThi(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void xuLyDoiCheDoHienThi(int position) {
        switch (position){
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;

        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                progressDialog.dismiss();

            }
        });


    }



    private void addControls() {
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spMap=findViewById(R.id.spMap);
        dsType=new ArrayList<>();
        dsType.addAll(Arrays.asList(getResources().getStringArray(R.array.arrType)));
        adapterType=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,dsType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMap.setAdapter(adapterType);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("thong bao ");
        progressDialog.setMessage("dang tai ban do");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



    }

    private void xyLyClickInfoWindow(Marker marker) {
        if (marker.getTitle().equals("Bach Khoa")) {
            mMap.setInfoWindowAdapter(new MyInfoAdapter(MainActivity.this,bachKhoaInfo));
            marker.showInfoWindow();
            Toast.makeText(MainActivity.this,"xu ly khi click bach khoa ",Toast.LENGTH_LONG).show();

        }
        else if (marker.getTitle().equals("Ngoai Thuong")) {
            mMap.setInfoWindowAdapter(new MyInfoAdapter(MainActivity.this, ftuInfo));
            marker.showInfoWindow();
            Toast.makeText(MainActivity.this,"xu ly khi click vinh hung ",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        mDatabase= FirebaseDatabase.getInstance().getReference();
            final DatabaseReference bachkhoa=mDatabase.child("weatherinfo").child("bachkhoa");
            bachkhoa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bachKhoaInfo=dataSnapshot.getValue(WeatherInfo.class);
                    Toast.makeText(MainActivity.this,"gia tri cua bach khoa"+bachKhoaInfo.toString(),Toast.LENGTH_LONG).show();
                    Log.i("bachkhoa", "onDataChange: "+bachKhoaInfo.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final DatabaseReference ktqd=mDatabase.child("weatherinfo").child("ktqd");
            ktqd.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ktqdInfo=dataSnapshot.getValue(WeatherInfo.class);
                    Toast.makeText(MainActivity.this,"gia tri cua ktqd"+ktqdInfo.toString(),Toast.LENGTH_LONG).show();
                    Log.i("ktqd", "onDataChange: "+ktqdInfo.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mDatabase.child("weatherinfo").child("ftu").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ftuInfo=dataSnapshot.getValue(WeatherInfo.class);
                    Log.i("ftu","this is data of ftu "+ftuInfo.toString());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient!=null){
            mFusedLocationClient.removeLocationUpdates(mLocationCallBack);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // LatLng vinhhung = new LatLng(20.9975, 105.8798);
       // mMap.addMarker(new MarkerOptions().position(vinhhung).title("Vinh Hung").snippet("my home"));

        LatLng bachkhoa = new LatLng(21.004801, 105.846108);
        Marker bachkhoa_marker=mMap.addMarker(new MarkerOptions().position(bachkhoa).title("Bach Khoa"));

        LatLng ngoaithuong = new LatLng(21.023243, 105.805469);
        Marker ngoaithuong_marker=mMap.addMarker(new MarkerOptions().position(ngoaithuong).title("Ngoai Thuong"));

        bachkhoa_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        ngoaithuong_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Bach Khoa")) {
                    mMap.setInfoWindowAdapter(new MyInfoAdapter(MainActivity.this, bachKhoaInfo));
                    marker.showInfoWindow();
                    Toast.makeText(MainActivity.this,"xu ly khi click bach khoa ",Toast.LENGTH_LONG).show();

                }
                if (marker.getTitle().equals("Ngoai Thuong")) {
                    mMap.setInfoWindowAdapter(new MyInfoAdapter(MainActivity.this,ftuInfo));
                    marker.showInfoWindow();
                    Toast.makeText(MainActivity.this,"xu ly khi click vinh hung ",Toast.LENGTH_LONG).show();
                }
                if (marker.getTitle().equals("Current Position"))
                {
                    Toast.makeText(MainActivity.this,"day la vi tri hien tai ",Toast.LENGTH_LONG).show();
                }
                return false;
            }

        });


        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bachkhoa,14));

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
    }

    private void xuLyCloseInfoWindow(Marker marker) {
        marker.hideInfoWindow();

        Toast.makeText(MainActivity.this,"Hide info window",Toast.LENGTH_LONG).show();

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    LocationCallback mLocationCallBack=new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }

        }
    };
}
