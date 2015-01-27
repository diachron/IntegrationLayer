/* CVS $Id: $ */
package de.unibonn.iai.eis.luzzu.semantics.vocabularies; 
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from src/main/resources/vocabularies/lmi/lmi.trig 
 * @author Auto-generated by schemagen on 11 Aug 2014 18:41 
 */
public class LMI {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://github.com/EIS-Bonn/Luzzu#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final Property javaPackageName = m_model.createProperty( "http://github.com/EIS-Bonn/Luzzu#javaPackageName" );
    
    public static final Property metric = m_model.createProperty( "http://github.com/EIS-Bonn/Luzzu#metric" );
    
    public static final Resource LuzzuMetricJavaImplementation = m_model.createResource( "http://github.com/EIS-Bonn/Luzzu#LuzzuMetricJavaImplementation" );
    
    public static final Resource MetricConfiguration = m_model.createResource( "http://github.com/EIS-Bonn/Luzzu#MetricConfiguration" );
    
}