package team.udacity.uos.doodle.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import team.udacity.uos.doodle.R;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {


    // GPSTracker class
    private GpsInfo gps;

    @Override
    public void onMapReady(GoogleMap map) {


        double latitude =0;
        double longitude = 0;

        gps = new GpsInfo(MapActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {


            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 6));
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Here!!"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
