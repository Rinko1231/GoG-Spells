package com.rinko1231.gogspells;

import com.rinko1231.gogspells.compat.traveloptics.init.EntityInit;
import com.rinko1231.gogspells.compat.traveloptics.renderer.SummonedSirenRenderer;
import com.rinko1231.gogspells.init.EntityRegistry;

import com.rinko1231.gogspells.renderer.summonedArachne.SummonedArachneRenderer;
import com.rinko1231.gogspells.renderer.summonedEnderDragonGirl.SummonedEnderDragonGirlRenderer;
import com.rinko1231.gogspells.renderer.summonedMummyGravemite.SummonedGraveMiteRenderer;
import com.rinko1231.gogspells.renderer.summonedMummyGravemite.SummonedMummyRenderer;
import com.rinko1231.gogspells.renderer.summonedNineTails.SummonedNineTailsRenderer;
import com.rinko1231.gogspells.renderer.summonedSludgeGirl.SummonedSludgeGirlRenderer;
import com.rinko1231.gogspells.renderer.summonedValkyrie.SummonedValkyrieRenderer;
import com.rinko1231.gogspells.renderer.summonedWerecat.SummonedWerecatRenderer;
import com.rinko1231.gogspells.renderer.summonedWitch.SummonedWitchRenderer;
import com.rinko1231.gogspells.renderer.summonedYukiOnna.SummonedYukiOnnaRenderer;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrbRenderer;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrowRenderer;
import net.minecraft.client.renderer.entity.CaveSpiderRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;


import static com.rinko1231.gogspells.GoGSpells.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SUMMONED_ENDER_DRAGON_GIRL.get(), SummonedEnderDragonGirlRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_NINE_TAILS.get(), SummonedNineTailsRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_WERECAT.get(), SummonedWerecatRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_SLUDGE_GIRL.get(), SummonedSludgeGirlRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_YUKI_ONNA.get(), SummonedYukiOnnaRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_MUMMY.get(), SummonedMummyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_VALKYRIE.get(), SummonedValkyrieRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_WITCH.get(), SummonedWitchRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_ARACHNE.get(), SummonedArachneRenderer::new);

        if (ModList.get().isLoaded("traveloptics")) {
        event.registerEntityRenderer(EntityInit.SUMMONED_SIREN.get(), SummonedSirenRenderer::new);}

        //Sub Summon
        event.registerEntityRenderer(EntityRegistry.SUMMONED_GRAVEMITE.get(), SummonedGraveMiteRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_CAVE_SPIDER.get(), CaveSpiderRenderer::new);
        //Projectile
        event.registerEntityRenderer(EntityRegistry.MAGIC_WEB_PROJECTILE.get(), (context) -> new ThrownItemRenderer(context, 0.75F, true));
        event.registerEntityRenderer(EntityRegistry.ARACHNE_POISON_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ARACHNE_POISON_ARROW.get(), PoisonArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ARACHNE_ACID_ORB.get(), AcidOrbRenderer::new);
    }
    /*
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
 }


    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
 }*/

}
