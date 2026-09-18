package dev.armoralert.hud;

import dev.armoralert.config.ArmorAlertConfig;

import java.util.EnumMap;
import java.util.Map;

public class WarningAnimator {
    public static final WarningAnimator INSTANCE = new WarningAnimator();
    private final Map<SlotLayout, Float> ticks = new EnumMap<>(SlotLayout.class);

    public WarningAnimator() {
        for (SlotLayout slot : SlotLayout.values()) {
            ticks.put(slot, 0f);
        }
    }

    public void tick(float deltaTime) {
        for (SlotLayout slot : SlotLayout.values()) {
            ticks.put(slot, ticks.get(slot) + deltaTime);
        }
    }

    public float getBounceOffset(SlotLayout slot, boolean isCritical, float deltaTime) {
        float time = ticks.get(slot) + deltaTime;
        float speed = (isCritical ? 4.0f : 2.0f) * ArmorAlertConfig.get().animationSpeed;
        float amplitude = isCritical ? 3.5f : 2.5f;
        return (float) Math.sin(time * speed) * amplitude;
    }

    public String getWarningText(float durabilityPercent) {
        ArmorAlertConfig config = ArmorAlertConfig.get();
        if (durabilityPercent <= config.criticalThreshold) {
            return "‼️";
        }
        if (durabilityPercent <= config.warningThreshold) {
            return "❗";
        }
        return null;
    }
}
