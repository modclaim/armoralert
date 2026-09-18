package dev.armoralert.config;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final ArmorAlertConfig config;

    public ConfigScreen(Screen parent) {
        super(Component.literal("ArmorAlert Settings"));
        this.parent = parent;
        this.config = ArmorAlertConfig.get();
    }

    @Override
    protected void init() {
        int leftCol = this.width / 2 - 155;
        int rightCol = this.width / 2 + 5;
        int y = 30;
        int rowHeight = 24;

        this.addRenderableWidget(CycleButton.onOffBuilder(config.enabled)
                .create(leftCol, y, 150, 20, Component.literal("HUD: "), (button, value) -> {
                    config.enabled = value;
                    ArmorAlertConfig.save();
                }));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.showPercentage)
                .create(rightCol, y, 150, 20, Component.literal("Percentage: "), (button, value) -> {
                    config.showPercentage = value;
                    ArmorAlertConfig.save();
                }));

        y += rowHeight;

        this.addRenderableWidget(CycleButton.onOffBuilder(config.showDurabilityBar)
                .create(leftCol, y, 150, 20, Component.literal("Durability Bar: "), (button, value) -> {
                    config.showDurabilityBar = value;
                    ArmorAlertConfig.save();
                }));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.helmet.visible)
                .create(rightCol, y, 150, 20, Component.literal("Helmet: "), (button, value) -> {
                    config.helmet.visible = value;
                    ArmorAlertConfig.save();
                }));

        y += rowHeight;

        this.addRenderableWidget(CycleButton.onOffBuilder(config.chestplate.visible)
                .create(leftCol, y, 150, 20, Component.literal("Chestplate: "), (button, value) -> {
                    config.chestplate.visible = value;
                    ArmorAlertConfig.save();
                }));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.leggings.visible)
                .create(rightCol, y, 150, 20, Component.literal("Leggings: "), (button, value) -> {
                    config.leggings.visible = value;
                    ArmorAlertConfig.save();
                }));

        y += rowHeight;

        this.addRenderableWidget(CycleButton.onOffBuilder(config.boots.visible)
                .create(leftCol, y, 150, 20, Component.literal("Boots: "), (button, value) -> {
                    config.boots.visible = value;
                    ArmorAlertConfig.save();
                }));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.offhand.visible)
                .create(rightCol, y, 150, 20, Component.literal("Offhand: "), (button, value) -> {
                    config.offhand.visible = value;
                    ArmorAlertConfig.save();
                }));

        y += rowHeight + 8;

        this.addRenderableWidget(Button.builder(Component.literal("Reset to Defaults"), button -> {
            ArmorAlertConfig.INSTANCE = new ArmorAlertConfig();
            ArmorAlertConfig.save();
            if (this.minecraft != null) {
                this.minecraft.setScreenAndShow(new ConfigScreen(parent));
            }
        }).bounds(leftCol, y, 150, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(rightCol, y, 150, 20)
                .build());
    }

    @Override
    public void onClose() {
        ArmorAlertConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreenAndShow(this.parent);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        this.extractBackground(graphics, mouseX, mouseY, delta);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
    }
}
