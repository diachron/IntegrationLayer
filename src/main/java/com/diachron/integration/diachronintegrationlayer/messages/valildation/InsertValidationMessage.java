package com.diachron.integration.diachronintegrationlayer.messages.valildation;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class InsertValidationMessage
{
    private String validationTriples;
    private String namedGraph;

    public InsertValidationMessage()
    {
    }

    public String getValidationTriples() {
        return validationTriples;
    }

    public void setValidationTriples(String validationTriples) {
        this.validationTriples = validationTriples;
    }

    public String getNamedGraph() {
        return namedGraph;
    }

    public void setNamedGraph(String namedGraph) {
        this.namedGraph = namedGraph;
    }
    
    
}
