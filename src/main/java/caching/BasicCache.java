package caching;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import management.configuration.PropertiesManager;

/**
 *
 * @author panos
 */
public class BasicCache
extends GenericCacheInterface
{
    private static BasicCache basicCache = null;
    private static Hashtable<String, Object> hashtable = null;

    public static BasicCache getBasicCache()
    {
        if(basicCache==null)
        {
            System.out.println(" ------------ Initializing BasicCache");
            basicCache = new BasicCache();
            hashtable = new Hashtable<String, Object>();
        }
        
        return basicCache;
    }
     
    public Object get(String key)
    {
        return this.hashtable.get(key);
    }
    
    public void put(String key, Object value)
    {
        this.hashtable.put(key, value);
    }    
}
