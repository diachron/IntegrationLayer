/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.athena.imis.diachron.archive.datamapping.MultidimensionalConverter;
import org.athena.imis.diachron.archive.datamapping.OntologyConverter;
public class TestConversionFile {
/**
* @param args
*/
public static void main(String[] args)
{
    // TODO Auto-generated method stub
    String dir = "/home/panos/Downloads/test/";
    File[] files = new File(dir).listFiles();
    for(File file : files){
    System.out.println("Converting file " + file.getName());
    File inputFile = new File(dir+file.getName());
    FileInputStream fis = null;
    File outputFile = new File(dir+"_diachron_"+file.getName());

        try {
            fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OntologyConverter converter = new OntologyConverter();                                   
            converter.convert(fis, fos, "test");
            
            //MultidimensionalConverter converter = new MultidimensionalConverter();
            //converter.convert(fis, fos, file.getName().substring(file.getName().lastIndexOf(".")+1), "test_qb_data");
            
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
}
}
