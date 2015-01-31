package team.udacity.uos.doodle.ar;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
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
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;

    private int MVPhandler;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private float[] mCamera = new float[16];
    private float[] tempMatrix = new float[16];

    private float mAngle;

    private float vertices[] = {
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f
    };
    private float colors[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.5f, 0.0f, 1.0f,
            1.0f, 0.5f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f
    };

    private byte indices[] = {
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
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

    public GLClearRenderer() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);

    }

    public void setHeadTracker(HeadTracker headTracker) {
        mHeadTracker = headTracker;
    }
    public void setContext(Context context){ arProj = context; }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgramObject);

        mHeadTracker.getLastHeadView(mHeadView, 0);

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mViewMatrix, 0, mHeadView, 0, mCamera, 0);

        Matrix.translateM(mModelMatrix, 0, 0, 0, 10.0f);
        Matrix.rotateM(mModelMatrix, 0, mAngle, 0.f, 1.f, 0.f);

        Matrix.multiplyMM(tempMatrix, 0, mProjMatrix, 0, mViewMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, tempMatrix, 0, mModelMatrix, 0);

        GLES20.glUniformMatrix4fv(MVPhandler, 1, false, mMVPMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);

        mAngle += 0.5f;
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        //GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        Matrix.setIdentityM(mProjMatrix, 0);
        Matrix.perspectiveM(mProjMatrix, 0, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        int h_vert = loadGLShader(arProj , GLES20.GL_VERTEX_SHADER, R.raw.doodle_vertex);
        int h_frag = loadGLShader(arProj , GLES20.GL_FRAGMENT_SHADER, R.raw.doodle_fragment);

        int h_prog = GLES20.glCreateProgram();
        GLES20.glAttachShader(h_prog, h_vert);
        GLES20.glAttachShader(h_prog, h_frag);

        GLES20.glBindAttribLocation(h_prog, 0, "aPosition");
        GLES20.glBindAttribLocation(h_prog, 1, "aColor");

        GLES20.glLinkProgram(h_prog);
        GLES20.glUseProgram(h_prog);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glVertexAttribPointer(1, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);

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