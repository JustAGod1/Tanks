package com.justagod.tanks.Entities.Blocks;

import android.support.annotation.DrawableRes;

import com.justagod.tanks.WorldProviding.World;

/**
 * Создано Юрием в 21.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class Block {
    @DrawableRes
    private int texture;


    public Block setTexture(@DrawableRes int texture) {
        this.texture = texture;
        return this;
    }

    public void onDestroy(BlockWrapper wrapper) {
        World.getInstance().removeEntity(wrapper);
    }
}
