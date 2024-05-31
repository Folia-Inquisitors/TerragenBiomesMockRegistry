package com.tcoded.terragenbiomesmockregistry;

import org.bukkit.plugin.java.JavaPlugin;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.version.Version;

import java.io.File;
import java.lang.reflect.Method;

public final class TerragenBiomesMockRegistry extends JavaPlugin {

    @Override
    public void onEnable() {
        registerBiomes();
    }

    private void registerBiomes() {
        File pluginsFolder = getDataFolder().getParentFile();
        File terraFolder = new File(pluginsFolder, "TerraformGenerator");

        if (!terraFolder.exists()) {
            terraFolder.mkdirs();
        }

        TerraformGeneratorPlugin.logger = new TLogger();
        String version = "v1_20_R4";

        Class<?> biomeHandle;
        try {
            biomeHandle = Class.forName("org.terraform." + version + ".CustomBiomeHandler");
        } catch (Exception e) {
            throw new RuntimeException("Failed to find CustomBiomeHandler for version " + version);
        }

        Method initMethod;
        try {
            initMethod = biomeHandle.getDeclaredMethod("init");
        } catch (Exception e) {
            throw new RuntimeException("Failed to find init method for CustomBiomeHandler");
        }

        try {
            initMethod.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke init method for CustomBiomeHandler");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
