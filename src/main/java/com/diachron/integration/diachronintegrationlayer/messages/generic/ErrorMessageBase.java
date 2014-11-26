package com.diachron.integration.diachronintegrationlayer.messages.generic;

import java.util.Date;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author panos
 */
public class ErrorMessageBase
{
    private JSONObject jsonMessage = null;
    private String datasetInformation = null;
    private String errorMessage = null;    
    private String originProblematicModule = null;
    
    public ErrorMessageBase()
    {
        this.jsonMessage = new JSONObject();
    }

    public String getDatasetInformation() {
        return datasetInformation;
    }

    public void setDatasetInformation(String datasetInformation) {
        this.datasetInformation = datasetInformation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOriginProblematicModule() {
        return originProblematicModule;
    }

    public void setOriginProblematicModule(String originProblematicModule) {
        this.originProblematicModule = originProblematicModule;
    }

    public JSONObject serializeMessageToJSON()
    throws JSONException
    {
        this.jsonMessage.put("DatasetInformation", this.datasetInformation);
        this.jsonMessage.put("ErrorMessage", this.errorMessage);
        this.jsonMessage.put("OriginProblematicModule", this.originProblematicModule);
        this.jsonMessage.put("DateError", (new Date()).toString());
        
        return this.jsonMessage;
    }
}
