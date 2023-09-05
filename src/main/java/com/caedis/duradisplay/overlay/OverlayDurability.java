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

import gregtech.api.items.GT_RadioactiveCell_Item;
import ic2.api.item.ICustomDamageItem;
import ic2.core.item.armor.ItemArmorFluidTank;
import vazkii.botania.common.item.brew.ItemBrewBase;

public class OverlayDurability extends OverlayDurabilityLike {

    public OverlayDurability() {
        super(
            new ConfigDurabilityLike(
                true,
                OverlayDurabilityLike.Style.NumPad,
                DurabilityFormatter.Format.percent,
                2,
                false,
                true,
                0x00FF00,
                ColorType.Vanilla,
                new double[] { 30, 70 },
                true) {

                @Override
                public void postLoadConfig() {
                    if (enabled && DuraDisplayConfig.Enable) ModSelfDrawnBar.changeDurabilitybar(false);
                    else ModSelfDrawnBar.restoreDurabilitybar();
                }

                @Override
                public @NotNull String category() {
                    return "durability";
                }
            });
        addHandler("gregtech.api.items.GT_MetaBase_Item", OverlayDurability::handleGregTech);
        addHandler("gregtech.api.items.GT_RadioactiveCell_Item", OverlayDurability::handleGregTechRadioactiveCell);
        addHandler("tconstruct.library.weaponry.AmmoItem", i -> null);
        addHandler("appeng.items.tools.powered.powersink.AEBasePoweredItem", i -> null);
        addHandler("ic2.api.item.IElectricItem", i -> null);
        addHandler("tconstruct.library.tools.ToolCore", OverlayDurability::handleToolCore);
        addHandler("ic2.core.item.armor.ItemArmorFluidTank", OverlayDurability::handleItemArmorFluidTank);
        addHandler("ic2.api.item.ICustomDamageItem", OverlayDurability::handleICustomDamageItem);
        addHandler("vazkii.botania.common.item.brew.ItemBrewBase", OverlayDurability::handleBotaniaBrew);
        addHandler("net.minecraft.item.Item", OverlayDurability::handleDefault);
    }

    @Override
    public @NotNull ConfigDurabilityLike config() {
        return config;
    }

    private static DurabilityLikeInfo handleDefault(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        assert item != null;

        if (!item.isDamageable()) return null;

        double damagePercent = item.getDurabilityForDisplay(stack);
        double max = item.getMaxDamage();
        double current = max * (1 - damagePercent);
        return new DurabilityLikeInfo(current, max);
    }

    private static DurabilityLikeInfo handleGregTech(@NotNull ItemStack stack) {
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

    private static DurabilityLikeInfo handleToolCore(@NotNull ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound()
            .hasKey("InfiTool")) return null;
        NBTTagCompound tags = stack.getTagCompound()
            .getCompoundTag("InfiTool");

        if (tags.getInteger("Unbreaking") < 10) {
            int damage = tags.getInteger("Damage");;
            int max = tags.getInteger("TotalDurability");
            int current = max - damage;
            return new DurabilityLikeInfo(current, max);
        }

        return null;
    }

    private static DurabilityLikeInfo handleGregTechRadioactiveCell(@NotNull ItemStack stack) {
        GT_RadioactiveCell_Item bei = ((GT_RadioactiveCell_Item) stack.getItem());

        assert bei != null;

        double damage = bei.getDamageOfStack(stack);
        double max = bei.getMaxDamageEx();
        double current = max - damage;
        return new DurabilityLikeInfo(current, max);
    }

    private static DurabilityLikeInfo handleItemArmorFluidTank(@NotNull ItemStack stack) {
        ItemArmorFluidTank bei = ((ItemArmorFluidTank) stack.getItem());
        assert bei != null;

        return new DurabilityLikeInfo(bei.getCharge(stack), bei.getCapacity(stack));
    }

    private static DurabilityLikeInfo handleICustomDamageItem(@NotNull ItemStack stack) {
        ICustomDamageItem bei = ((ICustomDamageItem) stack.getItem());
        assert bei != null;

        double damage = bei.getCustomDamage(stack);
        double max = bei.getMaxCustomDamage(stack);
        double current = max - damage;
        return new DurabilityLikeInfo(current, max);
    }

    private static DurabilityLikeInfo handleBotaniaBrew(@NotNull ItemStack stack) {
        ItemBrewBase brew = ((ItemBrewBase) stack.getItem());
        assert brew != null;

        double current = brew.getSwigsLeft(stack);
        double max = brew.getMaxDamage();
        return new DurabilityLikeInfo(current, max);
    }
}
