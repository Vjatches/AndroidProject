package pl.krakow.uek.project_android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class info_dotus extends AppCompatActivity implements OnMapReadyCallback{
    Intent intent;
    GoogleMap mMap;
    MapView mMapView;
    TextView name, adress, timeOpen, timeClose;
    Button visit;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_dotus);
        mMapView = (MapView)findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync((OnMapReadyCallback) this);

        name = (TextView)findViewById(R.id.name);
        adress = (TextView)findViewById(R.id.adress);
        timeClose = (TextView)findViewById(R.id.timeClose);
        timeOpen = (TextView)findViewById(R.id.timeOpen);
        visit = (Button)findViewById(R.id.visit);

        intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        adress.setText(intent.getStringExtra("adress"));
        timeClose.setText(intent.getStringExtra("timeClose"));
        timeOpen.setText(intent.getStringExtra("timeOpen"));
        url = intent.getStringExtra("url");

        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
                startActivity(browserIntent);
            }
        });

        intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        adress.setText(intent.getStringExtra("adress"));
        timeClose.setText(intent.getStringExtra("timeClose"));
        timeOpen.setText(intent.getStringExtra("timeOpen"));
        url = intent.getStringExtra("url");
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        intent = getIntent();
        String coordinates = intent.getStringExtra("coordinates");
        String[] coord_split = coordinates.split(":");
        Double lat = Double.parseDouble(coord_split[0]);
        Double lng = Double.parseDouble(coord_split[1]);
        LatLng krakow_adress = new LatLng(lat, lng);

        mMap.addMarker(new MarkerOptions().position(krakow_adress).title(intent.getStringExtra("adress")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(krakow_adress));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}


