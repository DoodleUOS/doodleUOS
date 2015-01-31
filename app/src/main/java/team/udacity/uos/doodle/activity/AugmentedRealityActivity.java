package team.udacity.uos.doodle.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.google.vrtoolkit.cardboard.sensors.HeadTracker;

import java.util.List;

import team.udacity.uos.doodle.ar.CameraView;
import team.udacity.uos.doodle.ar.GLClearRenderer;
import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.util.Util;
import team.udacity.uos.doodle.view.SimpleDoodleView;

public class AugmentedRealityActivity extends Activity {

    GLClearRenderer myRenderer = new GLClearRenderer();
    private HeadTracker mHeadTracker;

    private int cnt = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        this.mHeadTracker = HeadTracker.createFromContext(this);
        myRenderer.setHeadTracker(mHeadTracker);
        myRenderer.setContext(this);

        // When working with the camera, it's useful to stick to one orientation.
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );

        // Next, we disable the application's title bar...
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        // ...and the notification bar. That way, we can use the full screen.
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // Now let's create an OpenGL surface.
        final GLSurfaceView glView = new GLSurfaceView( this );
        // To see the camera preview, the OpenGL surface has to be created translucently.
        // See link above.
        glView.setEGLContextClientVersion(2);
        glView.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
        glView.getHolder().setFormat( PixelFormat.TRANSLUCENT );
        // The renderer will be implemented in a separate class, GLView, which I'll show next.
        glView.setRenderer( myRenderer );
        // Now set this as the main view.

        Log.i("Help", "logcat starts");
        new AsyncTask<Void, Void, List<Doodle>>(){

            @Override
            protected List<Doodle> doInBackground(Void... params) {
                List<Doodle> result = Util.getDoodles(AugmentedRealityActivity.this);
                for(Doodle item : result){
                    Log.i("Help", "item : " + item.getDooLoca());
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Doodle> list) {
                for (Doodle item : list){
                    if (cnt > 3) break;
                    else {
                        myRenderer.mDoodleBitmap[cnt] = getScreenViewBitmap(new SimpleDoodleView(AugmentedRealityActivity.this, item));
                        cnt++;
                    }
                }
                setContentView( glView );

                // Now also create a view which contains the camera preview...
                CameraView cameraView = new CameraView( AugmentedRealityActivity.this );
                // ...and add it, wrapping the full screen size.
                addContentView( cameraView, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mHeadTracker.startTracking();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mHeadTracker.stopTracking();
    }

    private Bitmap getScreenViewBitmap(View v) {
        v.setDrawingCacheEnabled(true);

        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());

        v.setDrawingCacheEnabled(false); // clear drawing cache

        return b;
    }
}