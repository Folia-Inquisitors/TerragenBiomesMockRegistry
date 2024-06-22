package com.tcoded.terragenbiomesmockregistry;

import org.bukkit.plugin.java.JavaPlugin;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.version.Version;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class TerragenBiomesMockRegistry extends JavaPlugin {

    @Override
    public void onEnable() {
        registerBiomes();
    }

    private double[] getVersionDouble(Version.SupportedVersion version) {
        Field versionDoubleField;
        try {
            versionDoubleField = version.getClass().getDeclaredField("versionDouble");
            versionDoubleField.setAccessible(true);
            return (double[]) versionDoubleField.get(version);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find versionDouble field for SupportedVersion");
        }
    }

    private String getPackageName(Version.SupportedVersion version) {
        Field versionPackageField;
        try {
            versionPackageField = version.getClass().getDeclaredField("packageName");
            versionPackageField.setAccessible(true);
            return (String) versionPackageField.get(version);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find packageName field for SupportedVersion");
        }
    }

    private void registerBiomes() {
        File pluginsFolder = getDataFolder().getParentFile();
        File terraFolder = new File(pluginsFolder, "TerraformGenerator");

        if (!terraFolder.exists()) {
            terraFolder.mkdirs();
        }

        TerraformGeneratorPlugin.logger = new TLogger();

        Class<?> biomeHandle = null;
        for (Version.SupportedVersion sv : Version.SupportedVersion.values()) {
            double[] versionDoubles = getVersionDouble(sv);
            String packageName = getPackageName(sv);
            for (double versionDouble : versionDoubles) {
                if (versionDouble != Version.DOUBLE) continue;

                try {
                    biomeHandle = Class.forName("org.terraform." + packageName + ".CustomBiomeHandler");
                } catch (Exception e) {
                    // IGNORED
                }
            }
        }

        if (biomeHandle == null) {
            throw new RuntimeException("Failed to find CustomBiomeHandler");
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
