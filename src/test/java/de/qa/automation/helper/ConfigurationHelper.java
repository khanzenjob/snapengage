package de.qa.automation.helper;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationHelper {
    /**
     * Singleton instance.
     */
    private final static ConfigurationHelper instance = new ConfigurationHelper();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static ConfigurationHelper getInstance() {
        return ConfigurationHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private ConfigurationHelper() {
        try {
            loadConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Properties instance
     */
    private Properties configuration = null;

    /**
     * Loads config.properties an instantiate the Properties object
     *
     * @throws Exception If instance already set.
     */
    private void loadConfiguration() throws Exception {
        if (configuration == null) {
            try (FileInputStream in = new FileInputStream("config.properties")) {
                configuration = new Properties();
                configuration.load(in);
            }
        } else {
            throw new Exception("Properties instance already available.");
        }
    }

    /**
     * Returns a configuration property for a specific key.
     *
     * @param key Configuration key.
     * @return Configuration property for key.
     */
    public String getProperty(String key) {
        return configuration.getProperty(key);
    }

    /**
     * Sets a configuration property with a value.
     *
     * @param key   Configuration key.
     * @param value Value.
     */
    public void setProperty(String key, String value) {
        configuration.setProperty(key, value);
    }

    /**
     * Removes a configuration property.
     *
     * @param key Configuration key.
     */
    public void removeProperty(String key) {
        configuration.remove(key);
    }

    /**
     * Returns enviroment.
     *
     * @return enviroment as String.
     */
    public String getEnviroment() {
        return getProperty("enviroment");
    }

    /**
     * Returns a configuration property as boolean.
     *
     * @param key Configuration key.
     * @return Configuration property.
     */
    public boolean getPropertyBoolean(String key) {
        String booleanValue = getProperty(key);
        if (booleanValue == null || booleanValue.trim().equals("")) {
            return false;
        }

        return Boolean.valueOf(getProperty(key));
    }

    /**
     * Returns a configuration property as integer.
     *
     * @param key      Configuration key.
     * @param fallback Fallback value.
     * @return Configuration property for key with fallback.
     */
    public int getPropertyInteger(String key, int fallback) {
        String intNumber = getProperty(key);
        if (intNumber == null || intNumber.trim().equals("")) {
            return fallback;
        }
        return Integer.parseInt(intNumber);
    }

    /**
     * Returns an array of strings.
     *
     * @param key       Configuration key
     * @param separator Separator
     * @return Array of strings
     */
    public String[] getPropertyArray(String key, String separator) {
        String values = getProperty(key);
        return values.split(separator);
    }

    /**
     * Returns an array of strings.
     *
     * @param key Configuration key
     * @return Array of strings
     */
    public String[] getPropertyArray(String key) {
        return getPropertyArray(key, "\\,");
    }
}
