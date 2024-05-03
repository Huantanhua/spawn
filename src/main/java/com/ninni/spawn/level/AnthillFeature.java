package com.ninni.spawn.level;

import com.mojang.serialization.Codec;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class AnthillFeature extends Feature<AnthillConfig> {

    public AnthillFeature(Codec<AnthillConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<AnthillConfig> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        RandomSource randomSource = featurePlaceContext.random();
        int radius = 1;
        if (!world.getBlockState(blockPos.below()).is(BlockTags.DIRT)) {
            return false;
        }
        BlockPos.withinManhattan(blockPos.below(), radius, 3, radius).forEach((pos) -> {
            if (world.getBlockState(pos).is(BlockTags.DIRT) && randomSource.nextBoolean()) {
                world.setBlock(pos, Blocks.COARSE_DIRT.defaultBlockState(), 3);
            }
        });
        if (randomSource.nextFloat() <= featurePlaceContext.config().anthill_chance()) {
            world.setBlock(blockPos.below(), SpawnBlocks.ANTHILL.defaultBlockState(), 2);
            world.getBlockEntity(blockPos.below(), SpawnBlockEntityTypes.ANTHILL).ifPresent(anthillBlockEntity -> {
                int i = 1 + randomSource.nextInt(3);
                for (int j = 0; j < i; ++j) {
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(SpawnEntityType.SpawnLandCreature.ANT).toString());
                    anthillBlockEntity.storeAnt(compoundTag, randomSource.nextInt(599), false);
                }
            });
        }
        return true;
    }

}
