package it.make.must.lettle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import it.make.must.util.PostLettle;

public class NewMessageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = "[LettleBoard]";

    private EditText editTextReceiver;
    private EditText editTextTitle;
    private EditText editTextMsg;

    private GoogleMap googleMap;
    protected LocationManager locationManager;

    private Location location;
    private double lat;
    private double lon;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findViewById(R.id.sendButton).setOnClickListener(sendButtonClickListener);
        editTextReceiver = (EditText) findViewById(R.id.editTextReceiver);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMsg = (EditText) findViewById(R.id.editTextMsg);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.getApplicationContext(), "권한이 없네~~~", Toast.LENGTH_SHORT).show();
            return;
        }

        googleMap.setMyLocationEnabled(true);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lat = location.getLatitude();
        lon = location.getLongitude();
        LatLng currentPoint = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        googleMap.addMarker(new MarkerOptions().position(currentPoint).snippet("Lat:" + location.getLatitude() + " / Lng:" + location.getLongitude())
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("My Location"));
    }


    Button.OnClickListener sendButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            PostLettle client = new PostLettle();
            client.mContext = v.getContext();
            String mReceiver = editTextReceiver.getText().toString();
            String mTitle = editTextTitle.getText().toString();
            String mMsg = editTextMsg.getText().toString();

            Log.d(TAG, "[NewMessageActivity] mReceiver = " + mReceiver);
            Log.d(TAG, "[NewMessageActivity] mTitle = " + mTitle);
            Log.d(TAG, "[NewMessageActivity] mMsg = " + mMsg);

            client.execute("http://52.79.196.78:3000/v1/lettle", mReceiver, mTitle, mMsg, String.valueOf(lat), String.valueOf(lon));
        }
    };


}
