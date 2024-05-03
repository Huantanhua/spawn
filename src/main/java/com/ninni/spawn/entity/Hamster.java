package com.ninni.spawn.entity;

import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.variant.HamsterVariant;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Hamster extends TamableAnimal implements InventoryCarrier, ContainerListener, HasCustomInventoryScreen {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> PUFF_TICKS = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.BYTE);
    static final Predicate<ItemEntity> ALLOWED_ITEMS = itemEntity -> !itemEntity.hasPickUpDelay() && itemEntity.isAlive();
    public final SimpleContainer inventory = new SimpleContainer(12);
    private int standingTicks;

    public Hamster(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new Hamster.HamsterMoveControl();
        this.setCanPickUpLoot(true);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        HamsterVariant[] variants = HamsterVariant.values();
        HamsterVariant variant = Util.getRandom(variants, serverLevelAccessor.getRandom());
        this.setVariant(variant);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Hamster.class, false, this::getTerritorialTarget));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 24.0f, 1.1, 1.3));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 24.0f, 1.1, 1.3));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 7f, 2.0f, false));
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.2, Ingredient.of(SpawnTags.HAMSTER_TEMPTS), false));
        this.goalSelector.addGoal(9, new HamsterSearchForItemsGoal());
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(13, new StandGoal());
    }

    private boolean getTerritorialTarget(LivingEntity livingEntity) {
        return livingEntity instanceof Hamster hamster && hamster.isTame() && !hamster.isInSittingPose() && !this.isInSittingPose() && this.isTame() && hamster.getOwnerUUID() == this.getOwnerUUID();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(SpawnTags.HAMSTER_TEMPTS);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!this.level().isClientSide && player.isSecondaryUseActive() && player instanceof HamsterOpenContainer && this.isOwnedBy(player) && !this.isBaby()) {
            if (player instanceof ServerPlayer serverPlayer) SpawnCriteriaTriggers.OPEN_HAMSTER_INVENTORY.trigger(serverPlayer);
            this.openCustomInventoryScreen(player);
            return InteractionResult.SUCCESS;
        }
        if (this.level().isClientSide) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || itemStack.is(SpawnTags.HAMSTER_FEEDS) && !this.isTame();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) itemStack.shrink(1);
                this.heal(4f);
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = super.mobInteract(player, interactionHand);
            if (interactionResult.consumesAction() && !this.isBaby() || !this.isOwnedBy(player)) return interactionResult;
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }
        if (!itemStack.is(SpawnTags.HAMSTER_FEEDS)) return super.mobInteract(player, interactionHand);
        if (!player.getAbilities().instabuild) {
            this.playSound(SpawnSoundEvents.HAMSTER_EAT, 1.0f, 1.0f);
            itemStack.shrink(1);
        }
        if (this.random.nextInt(3) == 0) {
            this.playSound(SpawnSoundEvents.HAMSTER_EAT, 1.0f, 1.0f);
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
            return InteractionResult.SUCCESS;
        } else this.level().broadcastEntityEvent(this, (byte)6);
        return InteractionResult.SUCCESS;
    }

    public float getPuffTicks() {
        return this.entityData.get(PUFF_TICKS);
    }

    @Override
    public void tick() {

        if (!this.level().isClientSide) {
            if (standingTicks > 0) standingTicks--;
            if (standingTicks == 0 && this.isStanding()) setStanding(false);
            float amount = 0;
            for (ItemStack itemStack : this.getInventory().items) {
                if (itemStack.isEmpty()) amount = amount - 0.1f;
                amount = amount + 0.1f;
            }
            if (amount > 0.8f) amount = 0.8f;
            this.entityData.set(PUFF_TICKS, amount);
        }

        super.tick();
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        if (this.getInventory().canAddItem(itemEntity.getItem())) this.playSound(SpawnSoundEvents.HAMSTER_EAT, 1.0f, 1.0f);
        InventoryCarrier.pickUpItem(this, this, itemEntity);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
            this.spawnAtLocation(itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    protected int calculateFallDamage(float f, float g) {
        return super.calculateFallDamage(f, g) - 10;
    }
    
    boolean canMove() {
        return !this.isStanding() && !this.isInSittingPose() && !this.isImmobile();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, HamsterVariant.RUSSIAN.id());
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(PUFF_TICKS, 0.0F);
    }

    @Override
    public void setTame(boolean bl) {
        super.setTame(bl);
        if (bl) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0);
            this.setHealth(20.0f);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6.0);
        }
    }

    public boolean isStanding() {
        return this.getFlag(1);
    }

    public void setStanding(boolean bl) {
        this.setFlag(1, bl);
    }

    private void setFlag(int i, boolean bl) {
        if (bl) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | i));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~i));
        }
    }
    private boolean getFlag(int i) {
        return (this.entityData.get(DATA_FLAGS_ID) & i) != 0;
    }

    public HamsterVariant getVariant() {
        return HamsterVariant.byId(this.entityData.get(VARIANT));
    }
    public void setVariant(HamsterVariant variant) {
        this.entityData.set(VARIANT, variant.id());
    }

    @Override
    public void containerChanged(Container container) {}

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ((HamsterOpenContainer)serverPlayer).openHamsterInventory(this, this.getInventory());
        }
    }

    class HamsterMoveControl extends MoveControl {
        public HamsterMoveControl() {
            super(Hamster.this);
        }
        @Override
        public void tick() {
            if (Hamster.this.canMove()) super.tick();
        }
    }

    class HamsterSearchForItemsGoal extends Goal {
        public HamsterSearchForItemsGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!Hamster.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return false;
            if (Hamster.this.getTarget() != null || Hamster.this.getLastHurtByMob() != null) return false;
            if (!Hamster.this.canMove()) return false;
            if (Hamster.this.getRandom().nextInt(Hamster.HamsterSearchForItemsGoal.reducedTickDelay(10)) != 0) return false;
            List<ItemEntity> list = Hamster.this.level().getEntitiesOfClass(ItemEntity.class, Hamster.this.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            return !list.isEmpty() && Hamster.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }

        @Override
        public void tick() {
            List<ItemEntity> list = Hamster.this.level().getEntitiesOfClass(ItemEntity.class, Hamster.this.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            ItemStack itemStack = Hamster.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack.isEmpty() && !list.isEmpty()) {
                Hamster.this.getNavigation().moveTo(list.get(0), 1f);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = Hamster.this.level().getEntitiesOfClass(ItemEntity.class, Hamster.this.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            if (!list.isEmpty()) {
                Hamster.this.getNavigation().moveTo(list.get(0), 1.2f);
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SpawnSoundEvents.HAMSTER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isStanding() ? SpawnSoundEvents.HAMSTER_AMBIENT_CALL : SpawnSoundEvents.HAMSTER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SpawnSoundEvents.HAMSTER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SpawnSoundEvents.HAMSTER_STEP, 0.15f, 1.0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.writeInventoryToTag(compoundTag);
        compoundTag.putInt("Variant", this.getVariant().id());
        compoundTag.putBoolean("Standing", this.isStanding());
        ListTag listTag = new ListTag();
        for (int i = 2; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (itemStack.isEmpty()) continue;
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putByte("Slot", (byte)i);
            itemStack.save(compoundTag2);
            listTag.add(compoundTag2);
        }
        compoundTag.put("Items", listTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readInventoryFromTag(compoundTag);
        this.setVariant(HamsterVariant.byId(compoundTag.getInt("Variant")));
        this.setStanding(compoundTag.getBoolean("Standing"));
        ListTag listTag = compoundTag.getList("Items", 10);
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag2 = listTag.getCompound(i);
            int j = compoundTag2.getByte("Slot") & 0xFF;
            if (j < 2 || j >= this.inventory.getContainerSize()) continue;
            this.inventory.setItem(j, ItemStack.of(compoundTag2));
        }
    }

    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType<Hamster> hamsterEntityType, ServerLevelAccessor world, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(world, pos);
    }

    @Override
    @Nullable
    public Hamster getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        Hamster hamster = SpawnEntityType.SpawnLandCreature.HAMSTER.create(serverLevel);
        if (hamster != null && ageableMob instanceof Hamster hamster2) {
            if (this.random.nextBoolean()) {
                hamster.setVariant(this.getVariant());
            } else {
                hamster.setVariant(hamster2.getVariant());
            }
            if (this.isTame()) {
                hamster.setOwnerUUID(this.getOwnerUUID());
                hamster.setTame(true);
            }
        }
        return hamster;
    }

    class StandGoal extends Goal {

        public StandGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return Hamster.this.getLastHurtByMob() == null
                    && Hamster.this.getRandom().nextFloat() < 0.02f
                    && Hamster.this.standingTicks == 0
                    && Hamster.this.getTarget() == null
                    && Hamster.this.getNavigation().isDone() ;
        }

        @Override
        public boolean canContinueToUse() {
            return Hamster.this.standingTicks > 0;
        }

        @Override
        public void start() {
            Hamster.this.standingTicks = 20 * (10 + Hamster.this.getRandom().nextInt(5));
            Hamster.this.setStanding(true);
            Hamster.this.getNavigation().stop();
        }

        @Override
        public void stop() {
            Hamster.this.setStanding(false);
        }
    }
}
