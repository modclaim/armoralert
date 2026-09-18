package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArmorAlertHud {
    public static final ArmorAlertHud INSTANCE = new ArmorAlertHud();

    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        WarningAnimator.INSTANCE.tick(deltaTracker.getRealtimeDeltaTicks());
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.player == null || client.font == null || client.options == null) {
            return;
        }
        if (client.options.keyPlayerList.isDown()) {
            return;
        }
        if (client.gui != null && client.gui.screen() != null) {
            return;
        }
        if (client.gui != null && client.gui.hud != null && client.gui.hud.isHidden()) {
            return;
        }
        ArmorAlertConfig config = ArmorAlertConfig.get();
        if (!config.enabled) {
            return;
        }
        Player player = client.player;
        int centerX = graphics.guiWidth() / 2;
        int bottomY = graphics.guiHeight() - 16;
        for (SlotLayout slot : SlotLayout.values()) {
            ArmorAlertConfig.SlotConfig slotConfig = slot.getConfig();
            if (!slotConfig.visible) {
                continue;
            }
            int slotX = centerX + slotConfig.offsetX;
            int slotY = bottomY + slotConfig.offsetY;
            ItemStack stack = slot.getStack(player);
            if (stack.isEmpty()) {
                graphics.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0x80000000);
            } else {
                graphics.item(stack, slotX, slotY);
                if (stack.isDamageableItem()) {
                    int maxDurability = stack.getMaxDamage();
                    int currentDurability = maxDurability - stack.getDamageValue();
                    float durabilityPercent = (float) currentDurability / maxDurability * 100f;
                    if (config.showDurabilityBar) {
                        int barColor = getBarColor(durabilityPercent);
                        graphics.fill(slotX, slotY + 14, slotX + 16, slotY + 16, 0xFF000000);
                        int barWidth = (int) (16 * (durabilityPercent / 100f));
                        graphics.fill(slotX, slotY + 14, slotX + barWidth, slotY + 15, barColor);
                    }
                    if (config.showPercentage) {
                        String text = String.format("%d%%", (int) durabilityPercent);
                        graphics.pose().pushMatrix();
                        graphics.pose().translate((float) slotX, (float) (slotY + 18));
                        graphics.pose().scale(0.5f, 0.5f);
                        graphics.text(client.font, text, 0, 0, 0xFFFFFF, true);
                        graphics.pose().popMatrix();
                    }
                    String warningText = WarningAnimator.INSTANCE.getWarningText(durabilityPercent);
                    if (warningText != null) {
                        boolean isCritical = durabilityPercent <= config.criticalThreshold;
                        float bounceY = WarningAnimator.INSTANCE.getBounceOffset(slot, isCritical, deltaTracker.getRealtimeDeltaTicks());
                        graphics.pose().pushMatrix();
                        graphics.pose().translate((float) (slotX + 8), (float) (slotY - 10 + bounceY));
                        graphics.pose().scale(0.8f, 0.8f);
                        int textWidth = client.font.width(warningText);
                        graphics.text(client.font, warningText, -textWidth / 2, 0, 0xFFFFFF, true);
                        graphics.pose().popMatrix();
                    }
                }
            }
        }
    }

    private int getBarColor(float percent) {
        if (percent > 50) return 0xFF00FF00;
        if (percent > 20) return 0xFFFFFF00;
        return 0xFFFF0000;
    }
}
