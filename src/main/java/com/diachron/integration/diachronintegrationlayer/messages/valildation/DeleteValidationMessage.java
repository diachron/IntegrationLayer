package com.diachron.integration.diachronintegrationlayer.messages.valildation;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class DeleteValidationMessage
{
    private String namedGraph;

    public DeleteValidationMessage()
    {
    }

    public String getNamedGraph() {
        return namedGraph;
    }

    public void setNamedGraph(String namedGraph) {
        this.namedGraph = namedGraph;
    }
    
    
}
