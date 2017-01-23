package com.justagod.tanks.Entities.Blocks;

import com.justagod.tanks.Entities.Entity;
import com.justagod.tanks.WorldProviding.Vectors.BlockPos;
import com.justagod.tanks.WorldProviding.Vectors.Vector2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Создано Юрием в 21.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class BlockWrapper extends Entity{
    private final Block mBlock;
    private final BlockPos mPos;

    public BlockWrapper(Block block, BlockPos pos) {

        mBlock = block;
        mPos = pos;
    }

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
