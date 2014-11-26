package com.diachron.integration.diachronintegrationlayer.services.archiving;

/**
 *
 * @author panos
 */
public abstract class ExternalResourcesLoader
{
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
      
    public abstract void initConnection();    
    public abstract String loadData();
    public abstract void clearConnection();
}
