package com.caedis.duradisplay.overlay;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;
import com.caedis.duradisplay.utils.ModSelfDrawnBar;
import com.caedis.duradisplay.utils.Mods;

import gregtech.api.items.ItemRadioactiveCell;
import ic2.api.item.ICustomDamageItem;
import ic2.core.item.armor.ItemArmorFluidTank;

public class OverlayDurability extends OverlayDurabilityLike {

    public OverlayDurability() {
        super(
            new ConfigDurabilityLike(
                true,
                OverlayDurabilityLike.Style.Text,
                DurabilityFormatter.Format.percent,
                2,
                false,
                true,
                0x00FF00,
                ColorType.RYGDurability,
                new double[] { 30, 70 },
                new int[] { 0xFF0000, 0x55FF00, 0x00FF00 },
                true,
                0,
                true) {

                @Override
                public void postLoadConfig() {
                    if (enabled && DuraDisplayConfig.Enable) ModSelfDrawnBar.changeDurabilitybar(false);
                    else ModSelfDrawnBar.restoreDurabilitybar();
                    configCategory.setComment("""
                        Durability is the default module that shows durability of items
                                                                        """);
                }

                @Override
                public @NotNull String category() {
                    return "durability";
                }
            });

        if (Mods.GregTech.isLoaded()) {
            addHandler("gregtech.api.items.MetaBaseItem", OverlayDurability::handleGregTech);
            addHandler("gregtech.api.items.ItemRadioactiveCell", OverlayDurability::handleGregTechRadioactiveCell);
        }

        if (Mods.TinkersConstruct.isLoaded()) {
            addHandler("tconstruct.library.weaponry.AmmoItem", i -> null);
            addHandler("tconstruct.library.tools.ToolCore", OverlayDurability::handleToolCore);
        }

        if (Mods.AE2.isLoaded()) addHandler("appeng.items.tools.powered.powersink.AEBasePoweredItem", i -> null);

        if (Mods.IC2.isLoaded()) {
            addHandler("ic2.api.item.IElectricItem", i -> null);
            addHandler("ic2.core.item.armor.ItemArmorFluidTank", OverlayDurability::handleItemArmorFluidTank);
            addHandler("ic2.api.item.ICustomDamageItem", OverlayDurability::handleICustomDamageItem);
            addHandler("ic2.core.item.tool.ItemToolPainter", i -> null);
        }

        if (Mods.Botania.isLoaded()) addHandler("vazkii.botania.common.item.brew.ItemBrewBase", i -> null);

        if (Mods.AlchemicalWizardry.isLoaded()) {
            addHandler("WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask", i -> null);
            addHandler("WayofTime.alchemicalWizardry.common.items.ScribeTool", i -> null);
        }

        if (Mods.BuildCraftCore.isLoaded()) addHandler("buildcraft.core.ItemPaintbrush", i -> null);

        if (Mods.Thaumcraft.isLoaded()) addHandler("thaumcraft.api.IScribeTools", i -> null);

        addHandler("net.minecraft.item.Item", OverlayDurability::handleDefault);
    }

    @Override
    public @NotNull ConfigDurabilityLike config() {
        return config;
    }

    public static DurabilityLikeInfo handleDefault(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        assert item != null;

        if (!item.isDamageable()) return null;

        // handled by OverlayGadgets
        if (OverlayGadgets.AllowListUnLocalized.contains(stack.getUnlocalizedName())) return null;

        double max = item.getMaxDamage(stack);
        double current = max - item.getDamage(stack);
        return new DurabilityLikeInfo(current, max);
    }

    public static DurabilityLikeInfo handleGregTech(@NotNull ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        NBTTagCompound nbt = stack.getTagCompound();

        if (!nbt.hasKey("GT.ToolStats")) {
            return null;
        } else {

            NBTTagCompound ts = nbt.getCompoundTag("GT.ToolStats");
            double damage = ts.getLong("Damage");
            double max = ts.getLong("MaxDamage");
            double current = max - damage;
            return new DurabilityLikeInfo(current, max);
        }

    }

    public static DurabilityLikeInfo handleToolCore(@NotNull ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound()
            .hasKey("InfiTool")) return null;
        NBTTagCompound tags = stack.getTagCompound()
            .getCompoundTag("InfiTool");

        if (tags.getInteger("Unbreaking") < 10) {
            int damage = tags.getInteger("Damage");
            int max = tags.getInteger("TotalDurability");
            int current = max - damage;
            return new DurabilityLikeInfo(current, max);
        }

        return null;
    }

    public static DurabilityLikeInfo handleGregTechRadioactiveCell(@NotNull ItemStack stack) {
        ItemRadioactiveCell bei = ((ItemRadioactiveCell) stack.getItem());

        assert bei != null;

        double damage = bei.getDamageOfStack(stack);
        double max = bei.getMaxDamageEx();
        double current = max - damage;
        return new DurabilityLikeInfo(current, max);
    }

    public static DurabilityLikeInfo handleItemArmorFluidTank(@NotNull ItemStack stack) {
        ItemArmorFluidTank bei = ((ItemArmorFluidTank) stack.getItem());
        assert bei != null;

        return new DurabilityLikeInfo(bei.getCharge(stack), bei.getCapacity(stack));
    }

    public static DurabilityLikeInfo handleICustomDamageItem(@NotNull ItemStack stack) {
        ICustomDamageItem bei = ((ICustomDamageItem) stack.getItem());
        assert bei != null;

        double damage = bei.getCustomDamage(stack);
        double max = bei.getMaxCustomDamage(stack);
        double current = max - damage;
        return new DurabilityLikeInfo(current, max);
    }

}
