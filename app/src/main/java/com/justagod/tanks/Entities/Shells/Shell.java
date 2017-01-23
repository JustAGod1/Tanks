package com.justagod.tanks.Entities.Shells;

import com.justagod.tanks.Entities.Entity;
import com.justagod.tanks.Entities.TankEntity;
import com.justagod.tanks.WorldProviding.Vectors.Vector2;

/**
 * Создано Юрием в 23.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */
public class Shell {
    private float mShootDelay;

    public float getShootDelay() {
        return mShootDelay;
    }

    public ShellWrapper createWrapper(Vector2 pos, Vector2 dir, float rotation, Entity entity) {
        return new ShellWrapper();
    }
}
