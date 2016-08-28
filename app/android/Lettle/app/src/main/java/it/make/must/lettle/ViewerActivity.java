package it.make.must.lettle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.make.must.model.Lettle;

public class ViewerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    protected LocationManager locationManager;
    private Lettle lettleData;
    private final String TAG = "[LettleBoard]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.viewerMap);
        mapFragment.getMapAsync(this);

        TextView tvTitle = (TextView)findViewById(R.id.viewerTitle);
        TextView tvTitleDate = (TextView)findViewById(R.id.viewerTitleDate);
        TextView tvMsg = (TextView)findViewById(R.id.viewerMsgText);
        TextView tvSender = (TextView)findViewById(R.id.viewerSender);
        TextView tvReceiver = (TextView)findViewById(R.id.viewerReceiver);
        TextView tvDateTime = (TextView)findViewById(R.id.viewerDateTime);

        lettleData = (Lettle) getIntent().getSerializableExtra("lettleObject");
        if(lettleData != null) {
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date tempDate = sdf.parse(lettleData.getEvent().getTimestamp());
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
                String titleDate = sdf2.format(tempDate);
                tvTitleDate.setText(titleDate);
                tvTitle.setText(lettleData.getContents().getTitle());

                tvDateTime.setText(lettleData.getDateTime());
                tvMsg.setText(lettleData.getContents().getMsg());
                String dear = "";
                for(String person : lettleData.getReceiversId()) {
                    dear += person + ". ";
                }

                tvReceiver.setText("Dear@ " + dear);
                tvSender.setText("from. " + lettleData.getSenderId());
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }

        }
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
        /*Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat = location.getLatitude();
        double lon = location.getLongitude();*/


        double lat = lettleData.getEvent().getLatitude();
        double lon = lettleData.getEvent().getLongitude();

        Log.d(TAG, "[NewMessageActivity] LAT = " + lat + " / LON = " + lon);

        LatLng currentPoint = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        googleMap.addMarker(new MarkerOptions().position(currentPoint).snippet("Lat:" + lat + " / Lng:" + lon)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("My Location"));
    }

}
