package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public enum SlotLayout {
    HELMET("Helmet", 3, false),
    CHESTPLATE("Chestplate", 2, false),
    LEGGINGS("Leggings", 1, false),
    BOOTS("Boots", 0, false),
    OFFHAND("Offhand", -1, true);

    public final String translationKey;
    public final int armorIndex;
    public final boolean isOffhand;

    SlotLayout(String translationKey, int armorIndex, boolean isOffhand) {
        this.translationKey = translationKey;
        this.armorIndex = armorIndex;
        this.isOffhand = isOffhand;
    }

    public ItemStack getStack(PlayerEntity player) {
        if (isOffhand) {
            return player.getOffHandStack();
        }
        return player.getInventory().armor.get(armorIndex);
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
