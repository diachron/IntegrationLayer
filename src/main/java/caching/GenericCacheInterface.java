package caching;

/**
 *
 * @author panos
 */
public abstract class GenericCacheInterface
{
    public abstract Object get(String key);
    public abstract void put(String key, Object value); 
}
