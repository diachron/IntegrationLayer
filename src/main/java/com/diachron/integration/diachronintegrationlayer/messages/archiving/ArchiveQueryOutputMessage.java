package com.diachron.integration.diachronintegrationlayer.messages.archiving;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class ArchiveQueryOutputMessage
{
    //@XmlElement(name="query")    
    private String message;
    //@XmlElement(name="returnType")    
    private String data;
    private boolean success;

    public ArchiveQueryOutputMessage()
    {
        
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
