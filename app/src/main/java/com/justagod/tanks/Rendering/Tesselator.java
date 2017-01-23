package com.justagod.tanks.Rendering;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.support.annotation.DrawableRes;

import com.justagod.tanks.MainActivity;
import com.justagod.tanks.WorldProviding.Vectors.Vector2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_COLOR_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_COORD_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_VERTEX_ARRAY;

/**
 * Создано Юрием в 22.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class Tesselator {

    private static boolean isTextureArrayEnabled;
    private static boolean isCoordsArrayEnabled;
    private static boolean isColorsArrayEnabled;
    private static boolean isIndicesBinded;

    private static boolean isTextureBinded;
    private static HashMap<Integer, Integer> textures = new HashMap<>();

    private static ByteBuffer indices;
    private static int indicesLength;

    public static void allow(int state, GL10 gl) {
        switch (state) {
            case GL_VERTEX_ARRAY: {
                isCoordsArrayEnabled = true;
                break;
            }
            case GL_COLOR_ARRAY: {
                isColorsArrayEnabled = true;
                break;
            }
            case GL_TEXTURE_COORD_ARRAY: {
                isTextureArrayEnabled = true;
                break;
            }
            default: {
                throw new RenderingException(String.format("%d не является тем чем надо", state));
            }
        }
        gl.glEnableClientState(state);
    }

    public static void bindTexCoordArray(float[] coords, GL10 gl) {
        if (!isTextureArrayEnabled) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            isTextureArrayEnabled = true;
        }
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, createFloatBufferFrmArray(coords));
    }

    public static void bindVertexArray(float[] vertexes, GL10 gl) {
        if (!isCoordsArrayEnabled) {
            gl.glEnableClientState(GL_VERTEX_ARRAY);
            isCoordsArrayEnabled = true;
        }
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, createFloatBufferFrmArray(vertexes));
    }

    public static void bindColorArray(float[] colors, int size, GL10 gl) {
        if (!isColorsArrayEnabled) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            isColorsArrayEnabled = true;
        }
        gl.glColorPointer(size, GL10.GL_FLOAT, 0, createFloatBufferFrmArray(colors));
    }

    public static void bindColorArray(float[] colors, GL10 gl) {
        bindColorArray(colors, 3, gl);
    }

    public static void bindIndices(byte[] indices) {
        Tesselator.indices = createIndexBuffer(indices);
        indicesLength = indices.length;
        isIndicesBinded = true;
    }

    public static int bindTexture(@DrawableRes int res, GL10 gl) {
        int textureId;

            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);
            textureId = textures[0];

            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.activity.getResources(), res);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            Tesselator.textures.put(res, textureId);

            bitmap.recycle();


        isTextureBinded = true;

        return textureId;
    }

    public static Vector2 translateTouchVector(float x, float y, float width, float height) {
        x -= width / 2.0;
        y -= height / 2.0;
        x /= width / 2;
        y /= height / 2;
        y *= -1;

        return new Vector2(x, y);
    }

    public static FloatBuffer createFloatBufferFrmArray(float[] floats) {
        FloatBuffer res;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                floats.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        res = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        res.put(floats);
        // set the buffer to read the first coordinate
        res.position(0);

        return res;
    }

    public static ByteBuffer createIndexBuffer(byte[] indices) {
        ByteBuffer res;

        res = ByteBuffer.allocateDirect(indices.length);
        res.put(indices);
        res.position(0);

        return res;
    }

    public static void drawElements(GL10 gl, ByteBuffer indices, int mode, boolean needClear) throws RenderingException {
        if (!isAnythingBinded()) throw new RenderingException("Мне нечего рисовать");
        gl.glDrawElements(mode, indicesLength, GL10.GL_UNSIGNED_BYTE, indices);
        if (needClear) clear(gl);
    }

    public static void drawElements(GL10 gl, int mode, boolean needClear) throws RenderingException {
        if (!isIndicesBinded) throw new RenderingException("Масив индексов не привязан");
        drawElements(gl, indices, mode, needClear);
    }

    public static void drawElements(GL10 gl, int mode) throws RenderingException {
        drawElements(gl, mode, true);
    }

    public static void clear(GL10 gl) {
        if (isColorsArrayEnabled) {
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            isColorsArrayEnabled = false;
        }

        if (isTextureArrayEnabled) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            isTextureArrayEnabled = false;
        }

        if (isCoordsArrayEnabled) {
            gl.glDisableClientState(GL_VERTEX_ARRAY);
            isCoordsArrayEnabled = false;
        }

        if (isIndicesBinded) {
            isIndicesBinded = false;
            indices.clear();
            indices = null;
            indicesLength = 0;
        }


    }

    private static boolean isAnythingBinded() {
        return isCoordsArrayEnabled;
    }

    public static void translated(double x, double y, double z, GL10 gl) {
        gl.glTranslatef((float) x, (float) y, (float) z);
    }

    public static void pushMatrix(GL10 gl) {
        gl.glPushMatrix();
    }

    public static void popMatrix(GL10 gl) {
        gl.glPopMatrix();
    }

    public static void rotate(double angle, double x, double y, double z, GL10 gl) {
        gl.glRotatef((float) angle, (float) x, (float) y, (float) z);
    }

    private static class RenderingException extends RuntimeException {

        public RenderingException() {
            super("Проблемы при рисовании");
        }

        public RenderingException(String message) {
            super(message);
        }
    }
}
