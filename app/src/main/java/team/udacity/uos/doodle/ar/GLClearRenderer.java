package team.udacity.uos.doodle.ar;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vrtoolkit.cardboard.sensors.HeadTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import team.udacity.uos.doodle.R;

public class GLClearRenderer implements Renderer {

    private int mProgramObject;
    private static final String TAG = "OpenGLRenderer";
    private static final float CAMERA_Z = 0.01f;

    private HeadTracker mHeadTracker;
    private Context arProj;

    public float[] mHeadView = new float[16];

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mCubeTexCoord;

    private int MVPhandler;
    private int[] mTextureDataHandle = new int[64];
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;

    public Bitmap[] mDoodleBitmap = new Bitmap[64];
    public int mDoodleCount = 0;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private float[] mCamera = new float[16];
    private float[] tempMatrix = new float[16];

    private float mAngle;

    private float cubePositionData[] = {
            // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
            // if the points are counter-clockwise we are looking at the "front". If not we are looking at
            // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
            // usually represent the backside of an object and aren't visible anyways.

            // Front face
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Bottom face
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
    };

    private float cubeTextureCoordinateData[] = {
            // Front face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Right face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Back face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Left face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Top face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Bottom face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    private int loadGLShader(Context context, int type, int resId) {
        String code = readRawTextFile(context, resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    private String readRawTextFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }

    public static int loadTexture(final Bitmap bitmap) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    public GLClearRenderer() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(cubePositionData.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(cubePositionData);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mCubeTexCoord = byteBuf.asFloatBuffer();
        mCubeTexCoord.put(cubeTextureCoordinateData);
        mCubeTexCoord.position(0);
    }

    public void setHeadTracker(HeadTracker headTracker) {
        mHeadTracker = headTracker;
    }

    public void setContext(Context context) {
        arProj = context;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgramObject);

        mHeadTracker.getLastHeadView(mHeadView, 0);

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mViewMatrix, 0, mHeadView, 0, mCamera, 0);

        for( int i = 0 ; i < mDoodleCount ; i++ ) {

            Matrix.setIdentityM(mModelMatrix, 0);

            if ( i == 0 ) {
                Matrix.translateM(mModelMatrix, 0, 7.0f, 0, 0);
            } else if ( i == 1 ) {
                Matrix.translateM(mModelMatrix, 0, -7.0f, 0, 0);
            } else if ( i == 2 ) {
                Matrix.translateM(mModelMatrix, 0, 0, 0, 7.0f);
            } else if ( i == 3 ) {
                Matrix.translateM(mModelMatrix, 0, 0, 0, -7.0f);
            } else if ( i == 4 ) break;

            //Matrix.translateM(mModelMatrix, 0, 5.0f*(float)(i-1), 0, 5.0f*(float)i);
            Matrix.rotateM(mModelMatrix, 0, mAngle, 0.f, 1.f, 0.f);

            Matrix.multiplyMM(tempMatrix, 0, mProjMatrix, 0, mViewMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, tempMatrix, 0, mModelMatrix, 0);

            GLES20.glUniformMatrix4fv(MVPhandler, 1, false, mMVPMatrix, 0);

            mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramObject, "uText");
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramObject, "aTexCoord");

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[i]);

            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES20.glUniform1i(mProgramObject, 0);

            //GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        }

        mAngle += 0.5f;
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.setIdentityM(mProjMatrix, 0);
        Matrix.perspectiveM(mProjMatrix, 0, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        int h_vert = loadGLShader(arProj, GLES20.GL_VERTEX_SHADER, R.raw.doodle_vertex);
        int h_frag = loadGLShader(arProj, GLES20.GL_FRAGMENT_SHADER, R.raw.doodle_fragment);

        int h_prog = GLES20.glCreateProgram();
        GLES20.glAttachShader(h_prog, h_vert);
        GLES20.glAttachShader(h_prog, h_frag);

        GLES20.glBindAttribLocation(h_prog, 0, "aPosition");
        GLES20.glBindAttribLocation(h_prog, 1, "aTexCoord");

        GLES20.glLinkProgram(h_prog);
        GLES20.glUseProgram(h_prog);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, mCubeTexCoord);

        for (int i = 0 ; i < mDoodleCount ; i++ ) {
            mTextureDataHandle[i] = loadTexture(mDoodleBitmap[i]);
        }

        GLES20.glEnableVertexAttribArray(0);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //  GLES20.glFrontFace(GLES20.GL_CW);
        //  GLES20.glEnable(GLES20.GL_CULL_FACE);
        MVPhandler = GLES20.glGetUniformLocation(h_prog, "MVP");

        mProgramObject = h_prog;

        if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
            Log.d("OpenGL ES", "OpenGL ES Error");
        }

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
}