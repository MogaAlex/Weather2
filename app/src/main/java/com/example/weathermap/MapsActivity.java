package com.example.weathermap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.weathermap.model.WeatherData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Api mApi = Api.Instance.getApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button londonButton = findViewById(R.id.london_button);
        londonButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                mApi.getWeatherDataByCity("New York","1cd94ec4eef20871769a30d0b6939451","metrics")
                        .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe(new Consumer<WeatherData>()
                {
                            @Override
                            public void accept(final WeatherData weatherData) throws Exception{
                                LatLng latLng = new LatLng(weatherData.getCoord().getLat(),weatherData.getCoord().getLon());
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker" + weatherData.getMain().getTemp()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                Toast.makeText(MapsActivity.this, weatherData.getName() + " " + weatherData.getMain().getTemp(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
    }
}
