package com.rinko1231.gogspells;

import com.rinko1231.gogspells.init.EntityRegistry;

import com.rinko1231.gogspells.renderer.summonedEnderDragonGirl.SummonedEnderDragonGirlRenderer;
import com.rinko1231.gogspells.renderer.summonedMummyGravemite.SummonedGraveMiteRenderer;
import com.rinko1231.gogspells.renderer.summonedMummyGravemite.SummonedMummyRenderer;
import com.rinko1231.gogspells.renderer.summonedNineTails.SummonedNineTailsRenderer;
import com.rinko1231.gogspells.renderer.summonedSludgeGirl.SummonedSludgeGirlRenderer;
import com.rinko1231.gogspells.renderer.summonedValkyrie.SummonedValkyrieRenderer;
import com.rinko1231.gogspells.renderer.summonedWerecat.SummonedWerecatRenderer;
import com.rinko1231.gogspells.renderer.summonedWitch.SummonedWitchRenderer;
import com.rinko1231.gogspells.renderer.summonedYukiOnna.SummonedYukiOnnaRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


import static com.rinko1231.gogspells.GoGSpells.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SUMMONED_ENDER_DRAGON_GIRL.get(), SummonedEnderDragonGirlRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_NINE_TAILS.get(), SummonedNineTailsRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_WERECAT.get(), SummonedWerecatRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_SLUDGE_GIRL.get(), SummonedSludgeGirlRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_YUKI_ONNA.get(), SummonedYukiOnnaRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_MUMMY.get(), SummonedMummyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_GRAVEMITE.get(), SummonedGraveMiteRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_VALKYRIE.get(), SummonedValkyrieRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_WITCH.get(), SummonedWitchRenderer::new);
    }
    /*
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
 }


    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
 }*/

}
