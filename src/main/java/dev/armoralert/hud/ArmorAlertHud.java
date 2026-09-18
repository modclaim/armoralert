package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArmorAlertHud {
    public static final ArmorAlertHud INSTANCE = new ArmorAlertHud();
    private static final Identifier SLOT_SPRITE = Identifier.fromNamespaceAndPath("armoralert", "hud/slot");

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
        int bottomY = graphics.guiHeight() - 22;

        for (SlotLayout slot : SlotLayout.values()) {
            ArmorAlertConfig.SlotConfig slotConfig = slot.getConfig();
            if (!slotConfig.visible) {
                continue;
            }
            int slotX = centerX + slotConfig.offsetX;
            int slotY = bottomY + slotConfig.offsetY;

            try {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE, slotX, slotY, 22, 22);
            } catch (Exception e) {
                drawFallbackSlot(graphics, slotX, slotY);
            }

            int itemX = slotX + 3;
            int itemY = slotY + 3;
            ItemStack stack = slot.getStack(player);

            if (stack.isEmpty()) {
                if (slot.emptyIcon != null) {
                    try {
                        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slot.emptyIcon, itemX, itemY, 16, 16);
                    } catch (Exception ignored) {
                    }
                }
            } else {
                graphics.item(stack, itemX, itemY);

                if (stack.isDamageableItem()) {
                    int maxDurability = stack.getMaxDamage();
                    int currentDurability = maxDurability - stack.getDamageValue();
                    float durabilityPercent = (float) currentDurability / maxDurability * 100f;

                    if (config.showDurabilityBar) {
                        int barColor = getBarColor(durabilityPercent);
                        graphics.fill(itemX + 1, itemY + 13, itemX + 15, itemY + 15, 0xFF000000);
                        int barWidth = Math.max(1, (int) (13 * (durabilityPercent / 100f)));
                        graphics.fill(itemX + 2, itemY + 13, itemX + 1 + barWidth, itemY + 14, barColor);
                    }

                    if (config.showPercentage) {
                        String text = String.format("%d%%", (int) durabilityPercent);
                        graphics.pose().pushMatrix();
                        int textW = client.font.width(text);
                        graphics.pose().translate((float) (slotX + 11), (float) (slotY - 7));
                        graphics.pose().scale(0.6f, 0.6f);
                        graphics.text(client.font, text, -textW / 2, 0, 0xFFFFFF, true);
                        graphics.pose().popMatrix();
                    }

                    String warningText = WarningAnimator.INSTANCE.getWarningText(durabilityPercent);
                    if (warningText != null) {
                        boolean isCritical = durabilityPercent <= config.criticalThreshold;
                        float bounceY = WarningAnimator.INSTANCE.getBounceOffset(slot, isCritical, deltaTracker.getRealtimeDeltaTicks());
                        graphics.pose().pushMatrix();
                        graphics.pose().translate((float) (slotX + 11), (float) (slotY - 18 + bounceY));
                        graphics.pose().scale(0.85f, 0.85f);
                        int textWidth = client.font.width(warningText);
                        graphics.text(client.font, warningText, -textWidth / 2, 0, 0xFFFFFF, true);
                        graphics.pose().popMatrix();
                    }
                }
            }
        }
    }

    private void drawFallbackSlot(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x, y, x + 22, y + 1, 0xFF000000);
        graphics.fill(x, y + 21, x + 22, y + 22, 0xFF000000);
        graphics.fill(x, y, x + 1, y + 22, 0xFF000000);
        graphics.fill(x + 21, y, x + 22, y + 22, 0xFF000000);

        graphics.fill(x + 1, y + 1, x + 21, y + 2, 0xFFCECECE);
        graphics.fill(x + 1, y + 1, x + 2, y + 21, 0xFFCECECE);

        graphics.fill(x + 2, y + 2, x + 20, y + 3, 0xFF5D5D5D);
        graphics.fill(x + 2, y + 2, x + 3, y + 20, 0xFF5D5D5D);

        graphics.fill(x + 1, y + 20, x + 21, y + 21, 0xFF7E7E7E);
        graphics.fill(x + 20, y + 1, x + 21, y + 21, 0xFF7E7E7E);

        graphics.fill(x + 3, y + 3, x + 19, y + 19, 0xBA151818);
    }

    private int getBarColor(float percent) {
        if (percent > 50) return 0xFF00FF00;
        if (percent > 20) return 0xFFFFFF00;
        return 0xFFFF0000;
    }
}
