package com.justagod.tanks.Entities;

import com.justagod.tanks.WorldProviding.Vectors.Vector2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Создано Юрием в 16.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public abstract class Entity {

    public abstract void onDraw(GL10 gl);

    public boolean isUpdatable() {
        return false;
    }

    public abstract boolean isVectorInBounds(Vector2 vector);

    public void updateEntity() {

    }

    public abstract void onEntityCollision(Entity entity);
}
