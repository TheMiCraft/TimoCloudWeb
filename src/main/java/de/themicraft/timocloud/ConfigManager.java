package de.themicraft.timocloud;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager{

    private File configFile;
    private Map<String, Object> config;

    public ConfigManager() {
        load();
    }
    
    public String getString(String key) {
        return (String) getConfig().get(key);
    }

    
    public int getInteger(String key) {
        return (int) getConfig().get(key);
    }

    
    public double getDouble(String key) {
        return (double) getConfig().get(key);
    }

    
    public float getFloat(String key) {
        return (float) getConfig().get(key);
    }

    
    public void set(String key, Object value) {
        getConfig().put(key, value);
    }

    
    public void reload() {
        config.clear();
        load();
    }

    private void load() {
        try {
            this.configFile = new File("./TimoCloudWeb/config.yml");
            if(!configFile.exists()) configFile.createNewFile();
            config = (Map<String, Object>) loadYaml(configFile);
            if (config == null) {
                config = new LinkedHashMap<>();
            }
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            saveYaml(getConfig(), getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object loadYaml(File file) throws IOException {
        FileReader reader = new FileReader(file);
        Object data = new Yaml().load(reader);
        reader.close();
        return data;
    }

    private void saveYaml(Object data, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        new Yaml(dumperOptions).dump(data, writer);
        writer.close();
    }

    private File getConfigFile() {
        return configFile;
    }

    private Map getConfig() {
        return config;
    }
}
