package com.justagod.tanks.WorldProviding.Vectors;

/**
 * Создано Юрием в 23.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class Rect {
    private Vector2 min;
    private Vector2 max;

    public Rect(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }

    public Rect(float minX, float minY, float maxX, float maxY) {
        this(new Vector2(minX, minY), new Vector2(maxX, maxY));
    }

    public Rect(double minX, double minY, double maxX, double maxY) {
        this((float) minX, (float) minY, (float) maxX, (float) maxY);
    }

    public boolean isVectorInBounds(Vector2 vector) {
        return vector.x >= min.x && vector.y >= min.y && vector.y <= max.y && vector.x <= max.x;
    }

    public Vector2 getMin() {
        return min;
    }

    public Vector2 getMax() {
        return max;
    }

    public void setMin(Vector2 min) {
        this.min = min;
    }

    public void setMax(Vector2 max) {
        this.max = max;
    }
}
