package com.justagod.tanks.WorldProviding.Vectors;

/**
 * Создано Юрием в 21.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class Position {

    private Vector2 vector;

    public Vector2 getVector() {
        return vector.clone();
    }

    public float getX() {
        return vector.x;
    }

    public float getY() {
        return vector.y;
    }
}
