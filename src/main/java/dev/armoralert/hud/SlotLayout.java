package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum SlotLayout {
    HELMET("Helmet", EquipmentSlot.HEAD, false),
    CHESTPLATE("Chestplate", EquipmentSlot.CHEST, false),
    LEGGINGS("Leggings", EquipmentSlot.LEGS, false),
    BOOTS("Boots", EquipmentSlot.FEET, false),
    OFFHAND("Offhand", EquipmentSlot.OFFHAND, true);

    public final String translationKey;
    public final EquipmentSlot slot;
    public final boolean isOffhand;

    SlotLayout(String translationKey, EquipmentSlot slot, boolean isOffhand) {
        this.translationKey = translationKey;
        this.slot = slot;
        this.isOffhand = isOffhand;
    }

    public ItemStack getStack(Player player) {
        if (isOffhand) {
            return player.getOffhandItem();
        }
        return player.getItemBySlot(slot);
    }

    public ArmorAlertConfig.SlotConfig getConfig() {
        ArmorAlertConfig config = ArmorAlertConfig.get();
        return switch (this) {
            case HELMET -> config.helmet;
            case CHESTPLATE -> config.chestplate;
            case LEGGINGS -> config.leggings;
            case BOOTS -> config.boots;
            case OFFHAND -> config.offhand;
        };
    }
}
