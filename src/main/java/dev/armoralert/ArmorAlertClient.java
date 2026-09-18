package dev.armoralert;

import com.mojang.blaze3d.platform.InputConstants;
import dev.armoralert.config.ArmorAlertConfig;
import dev.armoralert.config.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class ArmorAlertClient implements ClientModInitializer {
    public static KeyMapping configKeyMapping;

    @Override
    public void onInitializeClient() {
        ArmorAlertConfig.load();

        configKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.armoralert.open_settings",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_K,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeyMapping != null && configKeyMapping.consumeClick()) {
                client.setScreenAndShow(new ConfigScreen(client.gui != null ? client.gui.screen() : null));
            }
        });
    }
}
