package net.orion.create_limited.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BlockTraversalUtil {

    public static Set<BlockPos> findConnected(Level level, BlockPos start, Predicate<BlockState> predicate) {
        Set<BlockPos> visited = new HashSet<>();
        floodFill(level, start, predicate, visited);
        return visited;
    }

    private static void floodFill(Level level, BlockPos pos, Predicate<BlockState> predicate, Set<BlockPos> visited) {
        if (visited.contains(pos)) return;
        BlockState state = level.getBlockState(pos);
        if (!predicate.test(state)) return;

        visited.add(pos);

        for (Direction direction : CARDINAL_DIRECTIONS) {
            BlockPos next = pos.relative(direction);
            floodFill(level, next, predicate, visited);
        }
    }

    private static final Direction[] CARDINAL_DIRECTIONS = {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN
    };

}