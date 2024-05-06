package com.ninni.spawn.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import static com.ninni.spawn.Spawn.MOD_ID;

@SuppressWarnings("unused")
public interface SpawnTags {

    //Item tags
    TagKey<Item> CUSTOMIZABLE_MOB_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "customizable_mob_items"));
    TagKey<Item> ANGLER_FISH_TEMPTS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "angler_fish_tempts"));
    TagKey<Item> ANGLER_FISH_LIKES = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "angler_fish_likes"));
    TagKey<Item> TUNA_TEMPTS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "tuna_tempts"));
    TagKey<Item> TUNA_FEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "tuna_feeds"));
    TagKey<Item> SNAIL_TEMPTS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "snail_tempts"));
    TagKey<Item> SNAIL_FEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "snail_feeds"));
    TagKey<Item> HAMSTER_TEMPTS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "hamster_tempts"));
    TagKey<Item> HAMSTER_FEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "hamster_feeds"));
    TagKey<Item> ROTTEN_LOGS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "rotten_logs"));
    TagKey<Item> ANT_TEMPTS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "ant_tempts"));
    TagKey<Item> ANT_FEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "ant_feeds"));
    TagKey<Item> SEA_COW_LOVES = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "sea_cow_loves"));
    TagKey<Item> SEA_COW_LIKES = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "sea_cow_likes"));
    TagKey<Item> ADDITIONAL_CLAM_CASE_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "additional_clam_case_items"));
    TagKey<Item> CLAM_CASE_BLACKLIST = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "clam_case_blacklist"));
    TagKey<Item> PIGMENT_SHIFTER_BASE = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "pigment_shifter_base"));
    TagKey<Item> SUNFISH_TEMPTS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "sunfish_tempts"));
    TagKey<Item> SUNFISH_FEEDS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "sunfish_feeds"));

    //Block tags
    TagKey<Block> MUCUS_SOLIDIFIER = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "mucus_solidifier"));
    TagKey<Block> MUCUS_SOLIDIFICATION_SPREADER = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "mucus_solidification_spreader"));
    TagKey<Block> ROTTEN_LOGS_BLOCK = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "rotten_logs"));
    TagKey<Block> ANT_RESOURCE = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "ant_resource"));
    TagKey<Block> ANTHILLS = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "anthills"));

    //Biome tags
    TagKey<Biome> CLAM_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "clam_spawns"));
    TagKey<Biome> SNAIL_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "snail_spawns"));
    TagKey<Biome> ANGLER_FISH_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "angler_fish_spawns"));
    TagKey<Biome> TUNA_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "tuna_spawns"));
    TagKey<Biome> HERRING_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "herring_spawns"));
    TagKey<Biome> SEAHORSE_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "seahorse_spawns"));
    TagKey<Biome> HAMSTER_FREQUENTLY_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "hamster_frequently_spawns"));
    TagKey<Biome> HAMSTER_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "hamster_spawns"));
    TagKey<Biome> SEA_COW_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "sea_cow_spawns"));
    TagKey<Biome> SUNFISH_WARM_VARIANT = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "sunfish_warm_variant"));
    TagKey<Biome> SUNFISH_COLD_VARIANT = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "sunfish_cold_variant"));
    TagKey<Biome> SMALL_ANTHILL_GENERATES = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "small_anthill_generates"));

    //pointOfInterest tags
    TagKey<PoiType> ANT_HOME = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(MOD_ID, "ant_home"));
}