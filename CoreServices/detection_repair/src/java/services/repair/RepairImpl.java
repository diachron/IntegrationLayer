/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.repair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.diachron.repair.master.Repair;
import org.diachron.repair.master.Results;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class contains the service which is responsible for the repairing a specific dataset w.r.t. an ontology with integrity 
 * constraints. 
 * @author rousakis
 */
@Path("repair")
public class RepairImpl {

    private static String propFile = "/home/panos/NetBeansProjects/diachron/detection_repair/Config Files/config.properties";

    /** Creates a new instance of RepairImpl */
    public RepairImpl() {
    }

    /**
     * <b>POST</b> method which is responsible for the repairing of invalidities which are 
     * diagnosed, given an instance of a DIACHRON dataset and the provided integrity constraints.<br>
     * <b>URL:</b> /diachron/repair
     * @param <b>inputMessage</b> : A JSON-encoded string which has the following form: <br>
     * { <br>
     * "Dataset" : "Dataset1", <br>
     * "Ontology_w_constraints" : “Ontology1”, <br>
     * "GetDelta” : true, <br>
     * } <br>
     * <ul>
     * <li>Dataset - The URI of a DIACHRON entity to be repaired. <br>
     * <li>Ontology_w_constraints - The URI of an ontology describing the dataset, which should also contain the integrity 
     * constraints that will be checked over the dataset, in OWL syntax.<br>
     * <li>GetDelta - A flag which denotes if we want the method to return the delta which was applied on the dataset.
     * </ul>
     * @return A Response instance which has a JSON-encoded entity content depending on the input 
     * parameter of the method. We discriminate the following cases: <br>
     * <ul>
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message should have 
     * exactly 3 arguments." } if the input parameter has not three JSON parameters. 
     * <li> Error code: <b>200</b> and entity content: { "Success" : true, "RepairApplied" : true, "Delta" : [...]  } if 
     * the input parameter has the correct form where: 
     * <ul>
     * <li> RepairApplied is a flag which denotes whether the dataset was invalid, thus a repair was found and applied (true), false otherwise.
     * <li> Delta is an array of triples which had to be removed in order the dataset to become valid. However, whether these triples will be 
     * returned or not depends on the <b>GetDelta</b> flag which is given in the inputMessage parameter. 
     * </ul>
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message could not be parsed." } 
     * if the input parameter has not the correct form. 
     * </ul>
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response repairJSON(String inputMessage) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputMessage);
            if (jsonObject.size() != 3) {
                String message = "JSON input message should have exactly 3 arguments.";
                String json = "{ \"Success\" : false, "
                        + "\"Message\" : \"" + message + "\" }";
                return Response.status(400).entity(json).build();
            } else {
                String dataset = (String) jsonObject.get("Dataset");
                String constOntology = (String) jsonObject.get("Ontology_w_constraints");
                boolean getDelta = (Boolean) jsonObject.get("GetDelta");
                ///
                Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(propFile));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String virtuoso = properties.getProperty("Repository_IP");
                String username = properties.getProperty("Repository_Username");
                String password = properties.getProperty("Repository_Password");
                String port = properties.getProperty("Repository_Port");
                Repair repair = new Repair();
                String[] arguments = {virtuoso, port, username, password, dataset, constOntology, Boolean.toString(getDelta)};
                Results result = repair.run(arguments);
                ///
                String json = "{ \"Success\" = true, \"RepairApplied\" : " + result.getType() + ", \"Delta\" : [" + result.getStringBuilder() + "] }";
                return Response.status(200).entity(json).build();
            }
        } catch (ParseException ex) {
            String message = "JSON input message could not be parsed.";
            String json = "{ \"Success\" : false, "
                    + "\"Message\" : \"" + message + "\" }";
            return Response.status(400).entity(json).build();
        }
    }
}
