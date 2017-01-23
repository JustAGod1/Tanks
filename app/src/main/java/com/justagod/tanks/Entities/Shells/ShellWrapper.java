package com.justagod.tanks.Entities.Shells;

import com.justagod.tanks.Entities.Entity;
import com.justagod.tanks.WorldProviding.Vectors.Vector2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Создано Юрием в 23.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */
public class ShellWrapper extends Entity{
    @Override
    public void onDraw(GL10 gl) {

    }

    @Override
    public boolean isVectorInBounds(Vector2 vector) {
        return false;
    }

    @Override
    public void onEntityCollision(Entity entity) {

    }
}
