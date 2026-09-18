package dev.armoralert.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final ArmorAlertConfig config;
    private double scrollOffset = 0;
    private double maxScroll = 0;
    private final List<ClickableWidget> scrollableWidgets = new ArrayList<>();
    private final Map<ClickableWidget, Integer> originalY = new HashMap<>();

    public ConfigScreen(Screen parent) {
        super(Text.literal("ArmorAlert Settings"));
        this.parent = parent;
        this.config = ArmorAlertConfig.get();
    }

    @Override
    protected void init() {
        scrollableWidgets.clear();
        originalY.clear();
        int y = 30;

        addToggleButton(width / 2 - 100, y, "Enabled: ", () -> config.enabled = !config.enabled, () -> config.enabled);
        y += 24;
        addSlider(width / 2 - 100, y, "Warning Threshold", 10, 90, config.warningThreshold, val -> config.warningThreshold = val.intValue());
        y += 24;
        addSlider(width / 2 - 100, y, "Critical Threshold", 1, 50, config.criticalThreshold, val -> config.criticalThreshold = val.intValue());
        y += 24;
        addToggleButton(width / 2 - 100, y, "Show Percentage: ", () -> config.showPercentage = !config.showPercentage, () -> config.showPercentage);
        y += 24;
        addToggleButton(width / 2 - 100, y, "Show Durability Bar: ", () -> config.showDurabilityBar = !config.showDurabilityBar, () -> config.showDurabilityBar);
        y += 24;
        addSlider(width / 2 - 100, y, "Animation Speed", 0.1, 3.0, config.animationSpeed, val -> config.animationSpeed = val.floatValue());
        y += 34;

        y = addSlotConfigControls("Helmet", config.helmet, y);
        y = addSlotConfigControls("Chestplate", config.chestplate, y);
        y = addSlotConfigControls("Leggings", config.leggings, y);
        y = addSlotConfigControls("Boots", config.boots, y);
        y = addSlotConfigControls("Offhand", config.offhand, y);

        maxScroll = Math.max(0, y - height + 40);

        ButtonWidget resetButton = ButtonWidget.builder(Text.literal("Reset to Defaults"), button -> {
            ArmorAlertConfig.INSTANCE = new ArmorAlertConfig();
            if (this.client != null) {
                this.client.setScreen(new ConfigScreen(parent));
            }
        }).dimensions(width / 2 - 105, height - 30, 100, 20).build();
        addDrawableChild(resetButton);

        ButtonWidget doneButton = ButtonWidget.builder(Text.literal("Done"), button -> {
            ArmorAlertConfig.save();
            if (this.client != null) {
                this.client.setScreen(parent);
            }
        }).dimensions(width / 2 + 5, height - 30, 100, 20).build();
        addDrawableChild(doneButton);

        updateWidgetPositions();
    }

    private int addSlotConfigControls(String name, ArmorAlertConfig.SlotConfig slotConfig, int y) {
        addToggleButton(width / 2 - 100, y, name + " Visible: ", () -> slotConfig.visible = !slotConfig.visible, () -> slotConfig.visible);
        y += 24;
        addSlider(width / 2 - 100, y, name + " X Offset", -200, 200, slotConfig.offsetX, val -> slotConfig.offsetX = val.intValue());
        y += 24;
        addSlider(width / 2 - 100, y, name + " Y Offset", -50, 50, slotConfig.offsetY, val -> slotConfig.offsetY = val.intValue());
        return y + 24;
    }

    private void addToggleButton(int x, int y, String prefix, Runnable toggle, Supplier<Boolean> state) {
        ButtonWidget button = ButtonWidget.builder(Text.literal(prefix + (state.get() ? "On" : "Off")), b -> {
            toggle.run();
            b.setMessage(Text.literal(prefix + (state.get() ? "On" : "Off")));
        }).dimensions(x, y, 200, 20).build();
        scrollableWidgets.add(button);
        originalY.put(button, y);
        addDrawableChild(button);
    }

    private void addSlider(int x, int y, String prefix, double min, double max, double current, Consumer<Double> onUpdate) {
        SliderWidget slider = new SliderWidget(x, y, 200, 20, Text.literal(prefix + ": " + String.format("%.1f", current)), (current - min) / (max - min)) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal(prefix + ": " + String.format("%.1f", min + value * (max - min))));
            }
            @Override
            protected void applyValue() {
                onUpdate.accept(min + value * (max - min));
            }
        };
        scrollableWidgets.add(slider);
        originalY.put(slider, y);
        addDrawableChild(slider);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset -= verticalAmount * 20;
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        updateWidgetPositions();
        return true;
    }

    private void updateWidgetPositions() {
        for (ClickableWidget w : scrollableWidgets) {
            w.setY(originalY.get(w) - (int) scrollOffset);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void close() {
        ArmorAlertConfig.save();
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
