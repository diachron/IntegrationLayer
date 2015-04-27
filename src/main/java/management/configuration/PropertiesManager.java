package management.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author panos
 */
public final class PropertiesManager
{
    private static PropertiesManager propManager = null;
    private static String initFilePath = "integration_layer_config.properties";
    private static Properties prop;
    
    public static PropertiesManager getPropertiesManager()
    {
        if(propManager==null)
        {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream input = classLoader.getResourceAsStream(initFilePath);                                
                prop = new Properties();
                prop.load(input);
                propManager = new PropertiesManager();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return propManager;
    }
    
    public String getPropertyValue(String key)
    {
        return prop.getProperty(key);
    }
}
