package com.justagod.tanks.Rendering;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.justagod.tanks.WorldProviding.HUD;
import com.justagod.tanks.WorldProviding.World;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_BLEND;
import static javax.microedition.khronos.opengles.GL10.GL_BYTE;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;
import static javax.microedition.khronos.opengles.GL10.GL_VERTEX_ARRAY;


/**
 * Создано Юрием в 15.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class WorldRenderer implements GLSurfaceView.Renderer {

    public static final String TAG = "WorldRenderer";
    private int width;
    private int height;


    public WorldRenderer(int width, int height) {
        this.width = width;
        this.height = height;
        Log.i(TAG, String.format("width = %d height = %d", width, height));
        Log.i(TAG, String.format("Needed x scale = %f", getXScale()));
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glEnable(GL10.GL_TEXTURE_2D);            //Enable Texture Mapping ( NEW )
        gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
        gl.glClearDepthf(1.0f);                    //Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL);            //The Type Of Depth Testing To Do
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        HUD.instance.init(width, height);

        Log.w(TAG, "Rendering is started");

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        /*if(height == 0) { 				//Prevent A Divide By Zero By
            height = 1; 					//Making Height Equal One
        }

        gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
        gl.glLoadIdentity(); 					//Reset The Projection Matrix

        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
        gl.glLoadIdentity();*/ 					//Reset The Modelview Matrix
    }

    private float getXScale() {
        float res = (float) ((height * 1.0) / (width * 1.0));
        //res = 1 - (1 - res) / 2;

        return res;
    }




    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();


        HUD.instance.onDraw(width, height, gl);
        Tesselator.pushMatrix(gl);
        {
            gl.glScalef(getXScale(), 1, 1);
            //testQuad(gl);
            //World.getInstance().onDraw(gl);
        }
        Tesselator.popMatrix(gl);
    }

    private void testQuad(GL10 gl) {
        float[] vertexes = {
                -1, -1, 0,
                1, -1, 0,
                1, 1, 0,
                -1, 1, 0
        };

        float[] colors = {
                1, 0, 0, 1,
                0, 1, 0, 1,
                0, 0, 1, 1,
                1, 0, 1, 1
        };

        byte[] indices = {0, 1, 2, 0, 2, 3};

        gl.glEnableClientState(GL_VERTEX_ARRAY);


        gl.glVertexPointer(3, GL_FLOAT, 0, Tesselator.createFloatBufferFrmArray(vertexes));

        gl.glEnableClientState(GL_COLOR_ARRAY);

        gl.glColorPointer(4, GL_FLOAT, 0, Tesselator.createFloatBufferFrmArray(colors));

        gl.glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_BYTE, Tesselator.createIndexBuffer(indices));

        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisable(GL_COLOR_ARRAY);
    }
}
