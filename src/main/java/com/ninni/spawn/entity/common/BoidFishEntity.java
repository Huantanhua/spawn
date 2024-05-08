package com.ninni.spawn.entity.common;

import com.ninni.spawn.entity.ai.goal.BoidFishSchoolingGoal;
import com.ninni.spawn.entity.ai.goal.HeightBoundsGoal;
import com.ninni.spawn.entity.ai.goal.LimitSpeedAndLookInVelocityDirectionGoal;
import com.ninni.spawn.entity.ai.goal.OrganizeBoidSchoolingGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class BoidFishEntity extends AbstractFish {
    @Nullable
    public BoidFishEntity leader;
    public List<BoidFishEntity> ownSchool = new ArrayList<>();
    private int maxSchoolSize;
    public int cantFollowTimer;

    public BoidFishEntity(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new FishSwimGoal(this));
        this.goalSelector.addGoal(5, new BoidFishSchoolingGoal(this, 0.2f, 0.4f, 8 / 20f, 1 / 20f));
        this.goalSelector.addGoal(3, new HeightBoundsGoal(this));
        this.goalSelector.addGoal(2, new LimitSpeedAndLookInVelocityDirectionGoal(this, 0.65f));
        this.goalSelector.addGoal(5, new OrganizeBoidSchoolingGoal(this));
    }

    public int getMaxSchoolSize() {
        return maxSchoolSize;
    }
    public void SetMaxSchoolSize(int i) {
        this.maxSchoolSize = i;
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public void startFollowing(BoidFishEntity abstractSchoolingFish) {
        if (this.cantFollowTimer == 0) {
            this.leader = abstractSchoolingFish;
            abstractSchoolingFish.addToOwnSchoolFollower(this);
        }
    }

    public void stopFollowing() {
        if (this.leader != null) {
            this.leader.removeFollowerFromOwnSchool(this);
            this.leader = null;
        }
    }

    @Override
    protected SoundEvent getSwimSound() {
        return (this.isFollower() || this.hasFollowers()) ? this.random.nextInt(this.isFollower() ? this.leader.ownSchool.size() : this.ownSchool.size()) == 0 ? super.getSwimSound() : SoundEvents.EMPTY : super.getSwimSound();
    }

    private void addToOwnSchoolFollower(BoidFishEntity entity) {
        if (entity.cantFollowTimer == 0) this.ownSchool.add(entity);
    }

    private void removeFollowerFromOwnSchool(BoidFishEntity entity) {
        this.ownSchool.remove(entity);
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.ownSchool.size() < this.getMaxSchoolSize() && this.cantFollowTimer == 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.cantFollowTimer > 0) {
            this.cantFollowTimer--;
            this.stopFollowing();
        }
    }

    public boolean hasFollowers() {
        return this.ownSchool.size() > 1;
    }

    public void addFollowers(Stream<? extends BoidFishEntity> stream) {
        stream.limit(this.getMaxSchoolSize() - this.ownSchool.size()).filter(boidFish -> boidFish != this).forEach(boidFish -> boidFish.startFollowing(this));
    }

    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 200.0;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if (spawnGroupData == null) {
            spawnGroupData = new BoidFishEntity.SchoolSpawnGroupData(this);
        } else {
            this.startFollowing(((BoidFishEntity.SchoolSpawnGroupData)spawnGroupData).leader);
        }
        return spawnGroupData;
    }

    public static class SchoolSpawnGroupData
            implements SpawnGroupData {
        public final BoidFishEntity leader;

        public SchoolSpawnGroupData(BoidFishEntity boidFish) {
            this.leader = boidFish;
        }
    }

    static class FishSwimGoal extends RandomSwimmingGoal {
        private final BoidFishEntity fish;

        public FishSwimGoal(BoidFishEntity boidFish) {
            super(boidFish, 1.0, 40);
            this.fish = boidFish;
        }

        @Override
        public boolean canUse() {
            return !this.fish.isFollower() && !this.fish.hasFollowers() && super.canUse();
        }
    }
}

