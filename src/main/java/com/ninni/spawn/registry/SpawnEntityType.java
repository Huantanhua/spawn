package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnEntityType {

    public class SpawnLandCreature{
        public static final EntityType<Snail> SNAIL = register(
                "snail",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Snail::new)
                        .defaultAttributes(Snail::createAttributes)
                        .spawnGroup(MobCategory.CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snail::canSpawn)
                        .dimensions(EntityDimensions.scalable(0.8F, 0.8F))
                        .trackRangeChunks(10)
        );

        public static final EntityType<Hamster> HAMSTER = register(
                "hamster",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Hamster::new)
                        .defaultAttributes(Hamster::createAttributes)
                        .spawnGroup(MobCategory.CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hamster::canSpawn)
                        .dimensions(EntityDimensions.scalable(0.6F, 0.5F))
                        .trackRangeChunks(10)
        );

        public static final EntityType<Ant> ANT = register(
                "ant",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Ant::new)
                        .defaultAttributes(Ant::createAttributes)
                        .spawnGroup(MobCategory.CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Ant::canSpawn)
                        .dimensions(EntityDimensions.scalable(0.6F, 0.5F))
                        .trackRangeChunks(10)
        );
    }
    public class SpawnFish{
        public static final EntityType<Seahorse> SEAHORSE = register(
                "seahorse",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Seahorse::new)
                        .defaultAttributes(Seahorse::createAttributes)
                        .spawnGroup(MobCategory.WATER_AMBIENT)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(0.3F, 0.6F))
                        .trackRangeChunks(10)
        );
        public static final EntityType<Tuna> TUNA = register(
                "tuna",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Tuna::new)
                        .defaultAttributes(Tuna::createAttributes)
                        .spawnGroup(MobCategory.WATER_CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Tuna::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(1.2F, 0.8F))
        );

        public static final EntityType<TunaEgg> TUNA_EGG = register(
                "tuna_egg",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(TunaEgg::new)
                        .defaultAttributes(TunaEgg::createAttributes)
                        .spawnGroup(MobCategory.MISC)
                        .dimensions(EntityDimensions.fixed(0.15f, 0.15f))
                        .trackRangeChunks(10)
        );

        public static final EntityType<Herring> HERRING = register(
                "herring",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Herring::new)
                        .defaultAttributes(Herring::createAttributes)
                        .spawnGroup(MobCategory.WATER_AMBIENT)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Herring::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(0.4F, 0.25F))
                        .trackRangeChunks(30)
        );

        public static final EntityType<Sunfish> SUNFISH = register(
                "sunfish",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Sunfish::new)
                        .defaultAttributes(Sunfish::createAttributes)
                        .spawnGroup(MobCategory.WATER_CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Sunfish::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(1.5F, 2.2F))
        );
        public static final EntityType<AnglerFish> ANGLER_FISH = register(
                "angler_fish",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(AnglerFish::new)
                        .defaultAttributes(AnglerFish::createAttributes)
                        .spawnGroup(MobCategory.WATER_AMBIENT)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(0.6F, 0.6F))
                        .trackRangeChunks(10)
        );
    }
    public class SpawnAquaticCreature{
        public static final EntityType<SeaCow> SEA_COW = register(
                "sea_cow",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(SeaCow::new)
                        .defaultAttributes(SeaCow::createAttributes)
                        .spawnGroup(MobCategory.WATER_CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaCow::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(1.3F, 1F))
                        .trackRangeChunks(30)
        );
        public static final EntityType<SeaLion> SEA_LION = register(
                "sea_lion",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(SeaLion::new)
                        .defaultAttributes(SeaLion::createAttributes)
                        .spawnGroup(MobCategory.CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaLion::checkAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(0.8F, 1.3F))
                        .trackRangeChunks(30)

        );
        public static final EntityType<Whale> WHALE = register(
                "whale",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Whale::new)
                        .defaultAttributes(Whale::createAttributes)
                        .spawnGroup(MobCategory.WATER_CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Whale::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(2.2F, 2.2F))
                        .trackRangeChunks(30)
        );
        public static final EntityType<Clam> CLAM = register(
                "clam",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Clam::new)
                        .defaultAttributes(Clam::createAttributes)
                        .spawnGroup(MobCategory.WATER_AMBIENT)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Clam::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(0.4F, 0.2F))
                        .trackRangeChunks(30)

        );
        public static final EntityType<KrillSwarm> KRILL_SWARM = register(
                "krill_swarm",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(KrillSwarm::new)
                        .defaultAttributes(KrillSwarm::createAttributes)
                        .spawnGroup(MobCategory.WATER_CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, KrillSwarm::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(1.8F, 1.8F))
                        .trackRangeChunks(30)
        );

    }
    public class SpawnCephalopod {
        public static final EntityType<Octopus> OCTOPUS = register(
                "octopus",
                FabricEntityTypeBuilder.createMob()
                        .entityFactory(Octopus::new)
                        .defaultAttributes(Octopus::createAttributes)
                        .spawnGroup(MobCategory.WATER_CREATURE)
                        .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Octopus::checkSurfaceWaterAnimalSpawnRules)
                        .dimensions(EntityDimensions.scalable(0.8F, 0.6F))
                        .trackRangeChunks(30)

        );
    }
    static {
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.CLAM_SPAWNS), MobCategory.WATER_AMBIENT, SpawnAquaticCreature.CLAM, 150, 4, 12);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.ANGLER_FISH_SPAWNS), MobCategory.WATER_AMBIENT, SpawnEntityType.SpawnFish.ANGLER_FISH, 5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.TUNA_SPAWNS), MobCategory.WATER_CREATURE, SpawnEntityType.SpawnFish.TUNA, 15, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.HERRING_SPAWNS), MobCategory.WATER_AMBIENT, SpawnEntityType.SpawnFish.HERRING, 15, 4, 12);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.SEA_COW_SPAWNS), MobCategory.WATER_CREATURE, SpawnEntityType.SpawnAquaticCreature.SEA_COW, 20, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.SEAHORSE_SPAWNS), MobCategory.WATER_AMBIENT, SpawnEntityType.SpawnFish.SEAHORSE, 20, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.SNAIL_SPAWNS), MobCategory.CREATURE, SpawnEntityType.SpawnLandCreature.SNAIL, 12, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.HAMSTER_SPAWNS), MobCategory.CREATURE, SpawnEntityType.SpawnLandCreature.HAMSTER, 25, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.HAMSTER_FREQUENTLY_SPAWNS), MobCategory.CREATURE, SpawnEntityType.SpawnLandCreature.HAMSTER, 50, 1, 4);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Spawn.MOD_ID, id), entityType.build());
    }

}
