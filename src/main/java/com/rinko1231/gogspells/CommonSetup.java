package com.rinko1231.gogspells;

import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.entity.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import static com.rinko1231.gogspells.GoGSpells.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {

        event.put(EntityRegistry.SUMMONED_ENDER_DRAGON_GIRL.get(),
                SummonedEnderDragonGirl.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_NINE_TAILS.get(),
                SummonedNineTails.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_WERECAT.get(),
                SummonedWerecat.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_SLUDGE_GIRL.get(),
                SummonedSludgeGirl.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_YUKI_ONNA.get(),
                SummonedYukiOnna.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_MUMMY.get(),
                SummonedMummy.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_GRAVEMITE.get(),
                SummonedGraveMite.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_VALKYRIE.get(),
                SummonedValkyrie.createAttributes().build());

    }
}
