package com.justagod.tanks.WorldProviding;

import android.util.Log;
import android.view.MotionEvent;

import com.justagod.tanks.R;
import com.justagod.tanks.Rendering.Tesselator;
import com.justagod.tanks.WorldProviding.Vectors.Rect;
import com.justagod.tanks.WorldProviding.Vectors.Vector2;

import javax.microedition.khronos.opengles.GL10;

import static com.justagod.tanks.Rendering.Tesselator.bindIndices;
import static com.justagod.tanks.Rendering.Tesselator.bindTexCoordArray;
import static com.justagod.tanks.Rendering.Tesselator.bindTexture;
import static com.justagod.tanks.Rendering.Tesselator.bindVertexArray;
import static com.justagod.tanks.Rendering.Tesselator.drawElements;
import static com.justagod.tanks.Rendering.Tesselator.popMatrix;
import static com.justagod.tanks.Rendering.Tesselator.pushMatrix;
import static com.justagod.tanks.Rendering.Tesselator.translated;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;

/**
 * Создано Юрием в 21.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class HUD {

    public static final HUD instance = new HUD();
    public static final String TAG = "HUD";
    private static final float INDENT = 0.05f;
    private int width;
    private int height;
    private Rect upperArrow;
    private Rect downArrow;
    private Rect leftArrow;
    private Rect rightArrow;


    private HUD() {}

    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, String.format("x = %.2f y = %.2f", event.getX(), event.getY()));

        Vector2 pos = Tesselator.translateTouchVector(event.getX(), event.getY(), width, height);

        return onTouchJoystick(pos);
    }

    private boolean onTouchJoystick(Vector2 pos) {
        float x = pos.x;
        float y = pos.y;

        Arrow arrow = getTouchedArrow(pos);

        switch (arrow) {
            case UP: {
                World.getInstance().player.moveForward(0.04f);
            }
        }

        Log.i(TAG, String.valueOf(arrow));

        return false;
    }

    public void init(int width, int height) {
        this.width = width;
        this.height = height;

        initArrowsRects();
    }

    private void initArrowsRects() {
        Vector2 size = getJoystickDimensions(width, height);
        Vector2 arrowSize = new Vector2(size.x / 3, size.y / 3);

        float tX = (1 - INDENT);
        float tY = (-1 + INDENT);
        float aX = arrowSize.x;
        float aY = arrowSize.y;

        float widthHeightRatio = height / width;

        Vector2 translation = new Vector2(tX, tY);

        upperArrow = getRectWithTranslation(translation, new Vector2(-aX * 2, aY * 2));
        downArrow = getRectWithTranslation(translation, new Vector2(-aX * 2, 0));

        leftArrow = getRectWithTranslation(translation, new Vector2(-aX * 3, aY));
        leftArrow.getMax().x /= widthHeightRatio;
        leftArrow.getMax().y *= widthHeightRatio;

        rightArrow = getRectWithTranslation(translation, new Vector2(-aX, aY));
        rightArrow.getMax().x /= widthHeightRatio;
        rightArrow.getMax().y *= widthHeightRatio;

    }

    private Arrow getTouchedArrow(Vector2 pos) {
        if (upperArrow.isVectorInBounds(pos)) return Arrow.UP;
        if (downArrow.isVectorInBounds(pos)) return Arrow.DOWN;
        if (rightArrow.isVectorInBounds(pos)) return Arrow.RIGHT;
        if (leftArrow.isVectorInBounds(pos)) return Arrow.LEFT;

        return Arrow.NONE;
    }

    private Rect getRectWithTranslation(Vector2 currTranslation, Vector2 newTranslation) {
        float x = currTranslation.x + newTranslation.x;
        float y = currTranslation.y + newTranslation.y;

        return new Rect(x, y, x + 0.2, y + 0.2);
    }

    public void onDraw(float width, float height, GL10 gl) {
        drawJoystick(gl, width, height);
    }

    private Vector2 getJoystickDimensions(float width, float height) {
        return new Vector2((float) ((height / width) * (0.4)), 0.4f);
    }

    private void drawJoystick(GL10 gl, float width, float height) {
        Vector2 size = getJoystickDimensions(width, height);
        Vector2 arrowSize = new Vector2(size.x / 3, size.y / 3);


        Tesselator.pushMatrix(gl);
        {
            Tesselator.translated(1 - INDENT, -1 + INDENT, 0, gl);

            // HINT: Верхняя стрелка
            pushMatrix(gl);
            {
                translated(arrowSize.x * -2, arrowSize.y * 2.0, 0, gl);

                drawArrow(gl, arrowSize);
            }
            popMatrix(gl);


            // HINT: Левая стрелка
            pushMatrix(gl);
            {
                translated(arrowSize.x * -2, arrowSize.y, 0, gl);
                pushMatrix(gl);
                {
                    Tesselator.rotate(90, 0, 0, 1, gl);

                    drawArrow(gl, new Vector2(arrowSize.x / (height / width), (height / width) * arrowSize.y));
                }
                popMatrix(gl);
            }
            popMatrix(gl);

            // HINT: Правая стрелка
            pushMatrix(gl);
            {
                translated(arrowSize.x * -1, arrowSize.y * 2, 0, gl);
                pushMatrix(gl);
                {
                    Tesselator.rotate(-90, 0, 0, 1, gl);

                    drawArrow(gl, new Vector2(arrowSize.x / (height / width), (height / width) * arrowSize.y));
                }
                popMatrix(gl);
            }
            popMatrix(gl);

            // HINT: Нижняя стрелка
            pushMatrix(gl);
            {
                translated(arrowSize.x * -1, arrowSize.y, 0, gl);
                pushMatrix(gl);
                {
                    Tesselator.rotate(180, 0, 0, 1, gl);

                    drawArrow(gl, arrowSize);
                }
                popMatrix(gl);
            }
            popMatrix(gl);
        }
        Tesselator.popMatrix(gl);

    }

    private void drawArrow(GL10 gl, Vector2 size) {
        float vertexArr[] = {
                0,0,0,
                0,size.y,0,
                size.x,size.y,0,
                size.x,0,0
        };
        float[] textureCoords = {
                0,1,
                0,0,
                1,0,
                1,1
        };
        byte[] indices = {0, 1, 2, 0, 2, 3};

        bindVertexArray(vertexArr, gl);
        bindTexture(R.drawable.arrow, gl);
        bindTexCoordArray(textureCoords, gl);
        bindIndices(indices);

        drawElements(gl, GL_TRIANGLES);
    }

    private enum Arrow {UP, DOWN, LEFT, RIGHT, NONE}
}
