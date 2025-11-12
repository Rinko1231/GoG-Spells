package com.rinko1231.gogspells;


import com.rinko1231.gogspells.compat.traveloptics.init.EntityInit;
import com.rinko1231.gogspells.compat.traveloptics.init.SpellInit;
import com.rinko1231.gogspells.config.GoGSpellsConfig;

import com.rinko1231.gogspells.init.*;

import com.rinko1231.gogspells.utils.MyUtils;
import gaia.entity.*;
import gaia.registry.GaiaRegistry;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Mod(GoGSpells.MOD_ID)
public class GoGSpells {
    public static final String MOD_ID = "gogspells";
    public static final String MODID = "gogspells"; //下划线很烦

    public GoGSpells() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GoGSpellsConfig.setup();
        itemRegistry.ITEMS.register(modEventBus);

        TabInit.TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        EntityRegistry.register(modEventBus);

        NewSpellRegistry.register(modEventBus);

        MobEffectRegistry.MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);

        if (ModList.get().isLoaded("traveloptics")) {
            SpellInit.TO_SPELLS.register(modEventBus);
            EntityInit.TO_ENTITIES.register(modEventBus);
        }

    }

    //好用的ResourceLocation
    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(MOD_ID, path);
    }


    //木乃伊掉落
    @SubscribeEvent
    public void MummyDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (!(entity instanceof Mummy)) return;

        Entity killer = source.getEntity();
        if (killer instanceof Player player) {
            if(!MyUtils.isEquipGaiaBlessing(player)) return;
            Random random = new Random();
            float roll = random.nextFloat();
            if (roll<= GoGSpellsConfig.gaiaBlessingExtraDropRateMummy.get()) {
                ItemStack dropStack = new ItemStack(itemRegistry.ANCIENT_REGAL_FABRIC.get(), 1);
                event.getDrops().add(new ItemEntity(
                        entity.level(),
                        entity.getX(), entity.getY(), entity.getZ(),
                        dropStack
                ));
            }
        }
    }
    //塞壬掉落
    @SubscribeEvent
    public void SirenDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!ModList.get().isLoaded("traveloptics")) return;
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (!(entity instanceof Siren)) return;
        Entity killer = source.getEntity();
        if (killer instanceof Player player) {
            if(!MyUtils.isEquipGaiaBlessing(player)) return;
            Random random = new Random();
            float roll = random.nextFloat();
            if (roll<= GoGSpellsConfig.gaiaBlessingExtraDropRateWitch.get()) {
                ItemStack dropStack = new ItemStack(itemRegistry.SIREN_PEARL.get(), 1);
                event.getDrops().add(new ItemEntity(
                        entity.level(),
                        entity.getX(), entity.getY(), entity.getZ(),
                        dropStack
                ));
            }
        }
    }
    //魔女掉落
    @SubscribeEvent
    public void WitchDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (!(entity instanceof Witch)) return;

        Entity killer = source.getEntity();
        if (killer instanceof Player player) {
            if(!MyUtils.isEquipGaiaBlessing(player)) return;
            Random random = new Random();
            float roll = random.nextFloat();
            if (roll<= GoGSpellsConfig.gaiaBlessingExtraDropRateWitch.get()) {
                ItemStack dropStack = new ItemStack(itemRegistry.GRIEF_SEED.get(), 1);
                event.getDrops().add(new ItemEntity(
                        entity.level(),
                        entity.getX(), entity.getY(), entity.getZ(),
                        dropStack
                ));
            }
        }
    }
    //末影龙女掉落
    @SubscribeEvent
    public void EnderDragonGirlDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!(entity instanceof EnderDragonGirl)) return;

        // 检查是否已经掉落
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.is(GaiaRegistry.DOLL_ENDER_GIRL_ITEM.get())) {
                return; // 已经掉落娃娃，直接返回
            }
        }
        // 获取击杀者
        Entity killer = source.getEntity();
        if (!(killer instanceof Player player)) return;

        if (!MyUtils.isEquipGaiaBlessing(player)) return;


        if (player.getRandom().nextFloat() <= GoGSpellsConfig.gaiaBlessingExtraDropRateEnderDragonGirl.get()) {
            ItemStack dropStack = new ItemStack(GaiaRegistry.DOLL_ENDER_GIRL_ITEM.get());
            event.getDrops().add(new ItemEntity(
                    entity.level(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    dropStack
            ));
        }
    }
    //九尾妖狐掉落
    @SubscribeEvent
    public void NineTailsDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!(entity instanceof NineTails)) return;

        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.is(GaiaRegistry.DOLL_NINE_TAILS_ITEM.get())) {
                return; // 已经掉落娃娃，直接返回
            }
        }

        // 获取击杀者
        Entity killer = source.getEntity();
        if (!(killer instanceof Player player)) return;

        if (!MyUtils.isEquipGaiaBlessing(player)) return;

        if (player.getRandom().nextFloat() <= GoGSpellsConfig.gaiaBlessingExtraDropRateNineTails.get()) {
            ItemStack dropStack = new ItemStack(GaiaRegistry.DOLL_NINE_TAILS_ITEM.get());
            event.getDrops().add(new ItemEntity(
                    entity.level(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    dropStack
            ));
        }
    }
    //瓦尔基里掉落
    @SubscribeEvent
    public void ValkyrieDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!(entity instanceof Valkyrie)) return;

        // 检查是否已经掉落
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.is(GaiaRegistry.BUST_VALKYRIE_ITEM.get())) {
                return; // 已经掉落娃娃，直接返回
            }
        }

        // 获取击杀者
        Entity killer = source.getEntity();
        if (!(killer instanceof Player player)) return;

        if (!MyUtils.isEquipGaiaBlessing(player)) return;

        if (player.getRandom().nextFloat() <= GoGSpellsConfig.gaiaBlessingExtraDropRateValkyrie.get()) {
            ItemStack dropStack = new ItemStack(GaiaRegistry.BUST_VALKYRIE_ITEM.get());
            event.getDrops().add(new ItemEntity(
                    entity.level(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    dropStack
            ));
        }
    }
    //雪女掉落
    @SubscribeEvent
    public void YukiOnnaDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!(entity instanceof YukiOnna)) return;

        // 检查是否已经掉落
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.is(GaiaRegistry.FAN_ICE.get())) {
                return; // 已经掉落
            }
        }

        // 获取击杀者
        Entity killer = source.getEntity();
        if (!(killer instanceof Player player)) return;

        if (!MyUtils.isEquipGaiaBlessing(player)) return;

        if (player.getRandom().nextFloat() <= GoGSpellsConfig.gaiaBlessingExtraDropRateYukiOnna.get()) {
            ItemStack dropStack = new ItemStack(GaiaRegistry.FAN_ICE.get());
            event.getDrops().add(new ItemEntity(
                    entity.level(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    dropStack
            ));
        }
    }

    //污泥掉落
    @SubscribeEvent
    public void SludgeGirlDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (!(entity instanceof SludgeGirl)) return;

        // 检查是否已经掉落
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.is(GaiaRegistry.DOLL_SLIME_GIRL_ITEM.get())) {
                return; // 已经掉落
            }
        }

        // 获取击杀者
        Entity killer = source.getEntity();
        if (!(killer instanceof Player player)) return;

        if (!MyUtils.isEquipGaiaBlessing(player)) return;

        if (player.getRandom().nextFloat() <= GoGSpellsConfig.gaiaBlessingExtraDropRateSludgeGirl.get()) {
            ItemStack dropStack = new ItemStack(GaiaRegistry.DOLL_SLIME_GIRL_ITEM.get());
            event.getDrops().add(new ItemEntity(
                    entity.level(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    dropStack
            ));
        }
    }

}
