package com.rinko1231.gogspells.init;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;


import java.util.List;
import java.util.UUID;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;

import static com.rinko1231.gogspells.GoGSpells.MOD_ID;

import static net.minecraft.world.item.Rarity.EPIC;


public class itemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    //public static final DeferredHolder<Item, Item> DARK_CRYSTAL = ITEMS.register("dark_crystal", ()-> new Item(ItemPropertiesHelper.material().rarity(EPIC)));
    public static final RegistryObject<Item> SIREN_PEARL = ITEMS.register("siren_pearl", ()-> new Item(ItemPropertiesHelper.material().rarity(EPIC))
    {
        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            String OriginalId = this.getDescriptionId();
            String TooltipKey = "tooltip." + OriginalId;
            tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
        }
    });
    public static final RegistryObject<Item> ANCIENT_REGAL_FABRIC = ITEMS.register("ancient_regal_fabric", ()-> new Item(ItemPropertiesHelper.material().rarity(EPIC))
    {
        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            String OriginalId = this.getDescriptionId();
            String TooltipKey = "tooltip." + OriginalId;
            tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
        }
    });
    public static final RegistryObject<Item> GRIEF_SEED = ITEMS.register("grief_seed", ()-> new Item(ItemPropertiesHelper.material().rarity(EPIC))
    {
        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            String OriginalId = this.getDescriptionId();
            String TooltipKey = "tooltip." + OriginalId;
            tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
        }
    });
    public static final RegistryObject<Item> GAIA_BLESSING = ITEMS.register("gaia_blessing",
            () -> new CurioBaseItem(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC)) {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

                    builder.put(AttributeRegistry.SUMMON_DAMAGE.get(),
                            new AttributeModifier(uuid, "gaia_blessing_summon_power_bonus", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));
                    builder.put(AttributeRegistry.MAX_MANA.get(),
                            new AttributeModifier(uuid, "gaia_blessing_max_mana_bonus", 50, AttributeModifier.Operation.ADDITION));
                    builder.put(AttributeRegistry.MANA_REGEN.get(),
                            new AttributeModifier(uuid, "gaia_blessing_mana_regen_bonus", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));

                    return builder.build();
                }
                @OnlyIn(Dist.CLIENT)
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
                    super.appendHoverText(stack, worldIn, tooltip, flagIn);
                    String OriginalId = this.getDescriptionId();
                    String TooltipKey = "tooltip." + OriginalId;
                    tooltip.add(Component.translatable(ChatFormatting.BLUE + "" + I18n.get(TooltipKey)));
                }
           });


}
