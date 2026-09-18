package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum SlotLayout {
    HELMET("Helmet", EquipmentSlot.HEAD, false, Identifier.withDefaultNamespace("container/slot/helmet")),
    CHESTPLATE("Chestplate", EquipmentSlot.CHEST, false, Identifier.withDefaultNamespace("container/slot/chestplate")),
    LEGGINGS("Leggings", EquipmentSlot.LEGS, false, Identifier.withDefaultNamespace("container/slot/leggings")),
    BOOTS("Boots", EquipmentSlot.FEET, false, Identifier.withDefaultNamespace("container/slot/boots")),
    OFFHAND("Offhand", EquipmentSlot.OFFHAND, true, Identifier.withDefaultNamespace("container/slot/shield"));

    public final String translationKey;
    public final EquipmentSlot slot;
    public final boolean isOffhand;
    public final Identifier emptyIcon;

    SlotLayout(String translationKey, EquipmentSlot slot, boolean isOffhand, Identifier emptyIcon) {
        this.translationKey = translationKey;
        this.slot = slot;
        this.isOffhand = isOffhand;
        this.emptyIcon = emptyIcon;
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
