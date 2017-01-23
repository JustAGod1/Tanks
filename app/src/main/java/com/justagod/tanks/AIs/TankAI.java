package com.justagod.tanks.AIs;

import com.justagod.tanks.Entities.TankEntity;

/**
 * Создано Юрием в 23.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */
public abstract class TankAI {
    private TankEntity mTankEntity;

    public abstract void update();

    public void setTankEntity(TankEntity tankEntity) {
        mTankEntity = tankEntity;
    }
}
