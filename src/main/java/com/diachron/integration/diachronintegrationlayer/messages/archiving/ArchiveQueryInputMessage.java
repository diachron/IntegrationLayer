package com.diachron.integration.diachronintegrationlayer.messages.archiving;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class ArchiveQueryInputMessage
{
    //@XmlElement(name="query")    
    private String query;
    //@XmlElement(name="returnType")    
    private String returnType;

    public ArchiveQueryInputMessage()
    {
        
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

}
