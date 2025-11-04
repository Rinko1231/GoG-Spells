package com.rinko1231.gogspells.init;

import com.rinko1231.gogspells.GoGSpells;
import com.rinko1231.gogspells.item.CurioAdvanceItem;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;

import static com.rinko1231.gogspells.GoGSpells.MOD_ID;

import static net.minecraft.world.item.Rarity.EPIC;
import static net.minecraft.world.item.Rarity.RARE;


public class itemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    //public static final DeferredHolder<Item, Item> DARK_CRYSTAL = ITEMS.register("dark_crystal", ()-> new Item(ItemPropertiesHelper.material().rarity(EPIC)));
    public static final DeferredHolder<Item, Item> ANCIENT_REGAL_FABRIC = ITEMS.register("ancient_regal_fabric", ()-> new Item(ItemPropertiesHelper.material().rarity(EPIC))
    {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
            super.appendHoverText(stack, context, tooltip, flag);
            String OriginalId = this.getDescriptionId();
            String TooltipKey = "tooltip." + OriginalId;
            tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
        }
    });
    public static final DeferredHolder<Item, Item> GAIA_BLESSING = ITEMS.register("gaia_blessing",
            () -> new CurioAdvanceItem(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC)) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    super.appendHoverText(stack, context, tooltip, flag);
                    String OriginalId = this.getDescriptionId();
                    String TooltipKey = "tooltip." + OriginalId;
                    tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
                }
            }.withAttributes(Curios.NECKLACE_SLOT,
                            new AttributeContainer[]{
                                    new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                    new AttributeContainer(AttributeRegistry.MAX_MANA, 50, AttributeModifier.Operation.ADD_VALUE),
                                    new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                            })
                    .withAttributes("charm",
                            new AttributeContainer[]{
                                    new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                    new AttributeContainer(AttributeRegistry.MAX_MANA, 50, AttributeModifier.Operation.ADD_VALUE),
                                    new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                            }));




/*
    public static final DeferredHolder<Item, Item> PONTIFICAL_KNIGHT_MEDAL = ITEMS.register("pontifical_knight_medal",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC)) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    super.appendHoverText(stack, context, tooltip, flag);
                    String OriginalId = this.getDescriptionId();
                    String TooltipKey = "tooltip." + OriginalId;
                    tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
                }
            }.withAttributes("charm",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
                    );

    */

/*
    public static final DeferredHolder<Item, Item> CLAIR_DE_LUNE_DISC = ITEMS.register("clair_de_lune_disc",
            () -> new Item(ItemPropertiesHelper.material(1)
                    .rarity(Rarity.RARE)
                    .jukeboxPlayable(
                            ResourceKey.create(
                                    Registries.JUKEBOX_SONG,
                                    GoGSpells.id("clair_de_lune"))))
            {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    super.appendHoverText(stack, context, tooltip, flag);
                    tooltip.add(Component.translatable("item.gogspells.clair_de_lune_disc.tooltip")
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }
            );

    */



}
