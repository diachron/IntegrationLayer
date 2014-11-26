package com.diachron.integration.diachronintegrationlayer.services.quality;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import management.configuration.PropertiesManager;

/**
 *
 * @author panos
 */
public class QualityAssessentModuleBase
{
    
    public ClientResponse retrieveQualityMetrics(String url, String qualityReportRequired, String metrics)
    {        
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();        
        MultivaluedMap<String,String> map = new MultivaluedMapImpl();
        map.add("Dataset", url);
        map.add("QualityReportRequired", qualityReportRequired);
        map.add("MetricsConfiguration", metrics);
        
        Client client = Client.create();
        WebResource webResource = client
                    .resource(propertiesManager.getPropertyValue("ComputeQualityAssementResourceAddress"));
	 
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED)
                                             .post(ClientResponse.class, map);

        return response;
    }     
    /*
    public ClientResponse retrieveQualityMetrics(String url)
    {        
        //String requestForm = "Dataset=$fileName&QualityReportRequired=false&MetricsConfiguration={\"@id\":\"_:f4216607408b1\",\"@type\":[\"http://github.com/EIS-Bonn/Luzzu#MetricConfiguration\"],\"http://github.com/EIS-Bonn/Luzzu#metric\":[{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.POBODefinitionUsage\"}]";
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        MultivaluedMap<String,String> map = new MultivaluedMapImpl();
        map.add("Dataset", "http://aksw.org/model/export/?m=http://aksw.org/&f=turtle");
        map.add("QualityReportRequired", "false");
        map.add("MetricsConfiguration", "{{\"@id\":\"_:f4216607408b1\",\"@type\":[\"http://github.com/EIS-Bonn/Luzzu#MetricConfiguration\"],\"http://github.com/EIS-Bonn/Luzzu#metric\":[{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.POBODefinitionUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.HomogeneousDatatypes\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.ObsoleteConceptsInOntology\"},{\"@value\":\"eu.diachron.qualitymetrics.representational.understandability.LowBlankNodeUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.representational.understandability.HumanReadableLabelling\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.SynonymUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.DefinedOntologyAuthor\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.conciseness.OntologyVersioningConciseness\"},{\"@value\":\"eu.diachron.qualitymetrics.accessibility.performance.DataSourceScalability\"},{\"@value\":\"eu.diachron.qualitymetrics.dynamicity.timeliness.TimelinessOfResource\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.EntitiesAsMembersOfDisjointClasses\"}]}");
        
        Client client = Client.create();
        WebResource webResource = client
            .resource(propertiesManager.getPropertyValue("ComputeQualityAssementResourceAddress"));
	 
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED)
                                             .post(ClientResponse.class, map);

        return response;
    }
    */
    
}
