package dev.armoralert;

import dev.armoralert.config.ArmorAlertConfig;
import dev.armoralert.config.ConfigScreen;
import dev.armoralert.hud.ArmorAlertHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ArmorAlertClient implements ClientModInitializer {
    public static KeyBinding configKeyBinding;

    @Override
    public void onInitializeClient() {
        ArmorAlertConfig.load();

        HudRenderCallback.EVENT.register(ArmorAlertHud.INSTANCE::render);

        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.armoralert.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeyBinding.wasPressed()) {
                client.setScreen(new ConfigScreen(client.currentScreen));
            }
        });
    }
}
