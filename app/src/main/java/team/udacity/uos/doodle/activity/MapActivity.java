package team.udacity.uos.doodle.activity;

import android.os.AsyncTask;
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

import java.util.List;

import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.util.GpsInfo;
import team.udacity.uos.doodle.util.Util;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {


    // GPSTracker class
    private GpsInfo gps;
    private double latitude = 0;
    private double longitude = 0;

    List<Doodle> doodledoodle;
    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap map) {
//        double latitude = 0;
//        double longitude = 0;
        mMap = map;

        gps = new GpsInfo(MapActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            Bundle bundle = getIntent().getExtras();
            latitude = bundle.getDouble("lat");
            longitude = bundle.getDouble("long");

            if(bundle.getInt("value") == 0){        // 일반 클릭
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                new AsyncTask<Void, Void, List<Doodle>>(){

                    @Override
                    protected List<Doodle> doInBackground(Void... params) {
                        List<Doodle> result = Util.getDoodles(getApplicationContext());
                        return result;
                    }

                    @Override
                    protected void onPostExecute(List<Doodle> list) {
                        doodledoodle = list;
                        mapAddMarker(mMap);
                    }
                }.execute();
            } else{         // 타임라인 클릭
                map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Here!!"));
            }
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
//        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Here!!"));

    }

    private void mapAddMarker(GoogleMap map){
        for (Doodle item : doodledoodle){
            map.addMarker(new MarkerOptions().position(new LatLng(item.getDooLat(), item.getDooLong())).title("Here!!"));
            System.out.println("// " + item.getDooNo() + " // " + item.getDooMemNo());
        }
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
