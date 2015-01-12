package team.udacity.uos.doodle.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.Weather;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.WeatherRequest;
import team.udacity.uos.doodle.util.DBHelper;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.text1)
    TextView mText1;
    @InjectView(R.id.button1)
    Button mButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        final String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Seoul&mode=json&units=metric&cnt=7";

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Response.Listener<List<Weather>> listener = new Response.Listener<List<Weather>>() {
                    @Override
                    public void onResponse(List<Weather> response) {



                        new AsyncTask<Void, Void, List<Weather>>() {
                            @Override
                            protected List<Weather> doInBackground(Void... voids) {
                                DBHelper dbHelper = OpenHelperManager.getHelper(MainActivity.this, DBHelper.class);
                                List<Weather> result = null;
                                try {
                                    Dao<Weather, Integer> dao = dbHelper.getDao(Weather.class);
                                    result = dao.queryForAll();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } finally {
                                    OpenHelperManager.releaseHelper();
                                }
                                return result;
                            }

                            @Override
                            protected void onPostExecute(List<Weather> weathers) {
                                for(Weather item:weathers){
                                    mText1.append(item.day+"/"+item.description+"/"+item.highAndLow);
                                    mText1.append("\n\n");
                                }

                            }
                        }.execute();



                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                WeatherRequest weatherRequest = new WeatherRequest(MainActivity.this, url, listener, errorListener);
                VolleyHelper.getRequestQueue().add(weatherRequest);


            }
        });


        DBHelper dbHelper = OpenHelperManager.getHelper(MainActivity.this, DBHelper.class);
        ConnectionSource connectionSource = new AndroidConnectionSource(dbHelper);

    }

    @Override
    protected void onPause() {
        super.onPause();
        VolleyHelper.getRequestQueue().cancelAll(this);
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
