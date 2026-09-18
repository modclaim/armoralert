package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ArmorAlertHud {
    public static final ArmorAlertHud INSTANCE = new ArmorAlertHud();

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        WarningAnimator.INSTANCE.tick(tickCounter.getLastFrameDuration());
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.playerListKey.isPressed() || client.options.hudHidden || client.currentScreen != null) {
            return;
        }
        ArmorAlertConfig config = ArmorAlertConfig.get();
        if (!config.enabled) {
            return;
        }
        PlayerEntity player = client.player;
        int centerX = client.getWindow().getScaledWidth() / 2;
        int bottomY = client.getWindow().getScaledHeight() - 16;
        for (SlotLayout slot : SlotLayout.values()) {
            ArmorAlertConfig.SlotConfig slotConfig = slot.getConfig();
            if (!slotConfig.visible) {
                continue;
            }
            int slotX = centerX + slotConfig.offsetX;
            int slotY = bottomY + slotConfig.offsetY;
            ItemStack stack = slot.getStack(player);
            if (stack.isEmpty()) {
                context.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0x80000000);
            } else {
                context.drawItem(stack, slotX, slotY);
                if (stack.isDamageable()) {
                    int maxDurability = stack.getMaxDamage();
                    int currentDurability = maxDurability - stack.getDamage();
                    float durabilityPercent = (float) currentDurability / maxDurability * 100;
                    if (config.showDurabilityBar) {
                        int barColor = getBarColor(durabilityPercent);
                        context.fill(slotX, slotY + 14, slotX + 16, slotY + 16, 0xFF000000);
                        int barWidth = (int) (16 * (durabilityPercent / 100f));
                        context.fill(slotX, slotY + 14, slotX + barWidth, slotY + 15, barColor);
                    }
                    if (config.showPercentage) {
                        String text = String.format("%d%%", (int) durabilityPercent);
                        context.getMatrices().push();
                        context.getMatrices().translate(slotX, slotY + 18, 0);
                        context.getMatrices().scale(0.5f, 0.5f, 1f);
                        context.drawTextWithShadow(client.textRenderer, text, 0, 0, 0xFFFFFF);
                        context.getMatrices().pop();
                    }
                    String warningText = WarningAnimator.INSTANCE.getWarningText(durabilityPercent);
                    if (warningText != null) {
                        boolean isCritical = durabilityPercent <= config.criticalThreshold;
                        float bounceY = WarningAnimator.INSTANCE.getBounceOffset(slot, isCritical, tickCounter.getLastFrameDuration());
                        context.getMatrices().push();
                        context.getMatrices().translate(slotX + 8, slotY - 10 + bounceY, 0);
                        context.getMatrices().scale(0.8f, 0.8f, 1f);
                        int textWidth = client.textRenderer.getWidth(warningText);
                        context.drawTextWithShadow(client.textRenderer, warningText, -textWidth / 2, 0, 0xFFFFFF);
                        context.getMatrices().pop();
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
