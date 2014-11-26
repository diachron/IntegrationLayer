package com.diachron.integration.publication;

import com.hp.hpl.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author panos
 */
public class PublicationManager
{
    private String data;
    private Lang inputMIMEType;    
    private String returnMIMEType;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
   
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Lang getInputMIMEType() {
        return inputMIMEType;
    }

    public void setInputMIMEType(Lang inputMIMEType) {
        this.inputMIMEType = inputMIMEType;
    }

    public String getReturnMIMEType() {
        return returnMIMEType;
    }

    public void setReturnMIMEType(String returnMIMEType) {
        this.returnMIMEType = returnMIMEType;
    }
    
    public void convert()
    {
        Model model = RDFDataMgr.loadModel(this.url, this.inputMIMEType);
        model.write(System.out, this.returnMIMEType);
    }

    public static void main(String[] args)
    {
        PublicationManager pm = new PublicationManager();
        pm.setUrl("http://aksw.org/model/export/?m=http://aksw.org/&f=N3");
        pm.setInputMIMEType(Lang.N3);
        pm.setReturnMIMEType("Turtle");
        pm.convert();
    }
}
