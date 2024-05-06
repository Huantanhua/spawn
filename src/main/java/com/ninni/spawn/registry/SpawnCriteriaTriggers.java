package com.ninni.spawn.registry;

import com.ninni.spawn.advancements.SpawnCriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;


public class SpawnCriteriaTriggers {

    public static SpawnCriterionTrigger INTERACT_WITH_ANGLER_FISH = CriteriaTriggers.register(new SpawnCriterionTrigger("interact_with_angler_fish"));
    public static SpawnCriterionTrigger HATCH_ANT = CriteriaTriggers.register(new SpawnCriterionTrigger("hatch_ant"));
    public static SpawnCriterionTrigger OPEN_HAMSTER_INVENTORY = CriteriaTriggers.register(new SpawnCriterionTrigger("open_hamster_inventory"));
    public static SpawnCriterionTrigger GOT_STUCK_IN_MUCUS = CriteriaTriggers.register(new SpawnCriterionTrigger("got_stuck_in_mucus"));
    public static SpawnCriterionTrigger WENT_THROUGH_GHOSTLY_MUCUS = CriteriaTriggers.register(new SpawnCriterionTrigger("went_through_ghostly_mucus"));
    public static SpawnCriterionTrigger BREED_SUNFISH = CriteriaTriggers.register(new SpawnCriterionTrigger("breed_sunfish"));

}