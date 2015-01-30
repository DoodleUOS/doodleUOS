package team.udacity.uos.doodle.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.google.vrtoolkit.cardboard.sensors.HeadTracker;

import team.udacity.uos.doodle.ar.CameraView;
import team.udacity.uos.doodle.ar.GLClearRenderer;

public class CameraProjectActivity extends Activity {

    GLClearRenderer myRenderer = new GLClearRenderer();
    private HeadTracker mHeadTracker;

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
        GLSurfaceView glView = new GLSurfaceView( this );
        // To see the camera preview, the OpenGL surface has to be created translucently.
        // See link above.
        glView.setEGLContextClientVersion(2);
        glView.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
        glView.getHolder().setFormat( PixelFormat.TRANSLUCENT );
        // The renderer will be implemented in a separate class, GLView, which I'll show next.
        glView.setRenderer( myRenderer );
        // Now set this as the main view.
        setContentView( glView );

        // Now also create a view which contains the camera preview...
        CameraView cameraView = new CameraView( this );
        // ...and add it, wrapping the full screen size.
        addContentView( cameraView, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
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
}