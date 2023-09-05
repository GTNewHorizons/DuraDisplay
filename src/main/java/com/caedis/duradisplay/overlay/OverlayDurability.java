package com.caedis.duradisplay.overlay;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;

import appeng.items.tools.powered.powersink.AEBasePoweredItem;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_RadioactiveCell_Item;
import ic2.api.item.ICustomDamageItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.armor.ItemArmorFluidTank;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.weaponry.AmmoItem;

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
                public @NotNull String category() {
                    return "durability";
                }
            });
        addHandler(GT_MetaBase_Item.class, OverlayDurability::handleGregTech);
        addHandler(GT_RadioactiveCell_Item.class, OverlayDurability::handleGregTechRadioactiveCell);
        addHandler(AmmoItem.class, i -> null);
        addHandler(AEBasePoweredItem.class, i -> null);
        addHandler(IElectricItem.class, i -> null);
        addHandler(ToolCore.class, OverlayDurability::handleToolCore);
        addHandler(ItemArmorFluidTank.class, OverlayDurability::handleItemArmorFluidTank);
        addHandler(ICustomDamageItem.class, OverlayDurability::handleICustomDamageItem);
        addHandler(Item.class, OverlayDurability::handleDefault);
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
            return handleDefault(stack);
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
}
