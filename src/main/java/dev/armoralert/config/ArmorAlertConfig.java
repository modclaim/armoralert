package dev.armoralert.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArmorAlertConfig {
    public static ArmorAlertConfig INSTANCE = new ArmorAlertConfig();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean enabled = true;
    public int warningThreshold = 30;
    public int criticalThreshold = 10;
    public boolean showPercentage = true;
    public boolean showDurabilityBar = true;
    public float animationSpeed = 1.0f;
    
    public SlotConfig helmet = new SlotConfig(-82, -2, true);
    public SlotConfig chestplate = new SlotConfig(-62, -2, true);
    public SlotConfig leggings = new SlotConfig(-42, -2, true);
    public SlotConfig boots = new SlotConfig(-22, -2, true);
    public SlotConfig offhand = new SlotConfig(22, -2, true);

    public static class SlotConfig {
        public int offsetX;
        public int offsetY;
        public boolean visible;

        public SlotConfig(int offsetX, int offsetY, boolean visible) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.visible = visible;
        }
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("armoralert.json");
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                INSTANCE = GSON.fromJson(reader, ArmorAlertConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ArmorAlertConfig();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("armoralert.json");
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(INSTANCE, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArmorAlertConfig get() {
        return INSTANCE;
    }
}
