package xyz.brettb.ac.plugin;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

@Data
public class ConfigurationFile {
    private final String filename;
    private final JavaPlugin plugin;

    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigurationFile(@NonNull JavaPlugin plugin, @NonNull String fileName) {
        this(plugin, new File(plugin.getDataFolder(), fileName));
    }

    public ConfigurationFile(@NonNull JavaPlugin plugin, File file) {
        if (!plugin.isEnabled())
            throw new IllegalArgumentException("plugin has to be enabled");
        this.plugin = plugin;
        this.filename = file.getName();
        this.configFile = file;
    }

    public void reloadConfiguration() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        InputStream inputStream = plugin.getResource(filename);
        if (inputStream != null) {
            YamlConfiguration defaultConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            fileConfiguration.setDefaults(defaultConfiguration);
        }
    }

    public FileConfiguration getConfiguration() {
        if (fileConfiguration == null) {
            this.reloadConfiguration();
        }
        return fileConfiguration;
    }

    public void saveConfiguration() {
        if (fileConfiguration == null || configFile == null) {
            return;
        }
        try {
            getConfiguration().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save configuration to " + configFile, ex);
        }
    }

    public void saveDefaultConfiguration() {
        if (!configFile.exists()) {
            this.plugin.saveResource(filename, false);
        }
    }

}
