package com.justagod.tanks.WorldProviding.Vectors;

import com.justagod.tanks.WorldProviding.World;

/**
 * Создано Юрием в 22.01.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */
public class BlockPos {
    private int mX;
    private int mY;

    public BlockPos(int x, int y) {
        mX = x;
        mY = y;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockPos blockPos = (BlockPos) o;

        return mX == blockPos.mX && mY == blockPos.mY;

    }

    @Override
    public int hashCode() {
        int result = mX;
        result = 31 * result + mY;
        return result;
    }

    public BlockPos convertToChunkPos() {
        return new BlockPos(mX % (World.CHUNK_SIZE + 1), mY % (World.CHUNK_SIZE + 1));
    }


}
