detection_repair
================

This repository contains the services which correspond on the Change Detection and Validation/Repair Modules.

Configurations Notes
=====================
Folder Config Files contains two sample configuration files
 - change detection parameters for multidimensional model (md_config.properties)
 - change detection parameters for ontological model (config.properties)

The current war file which contained in folder dist (DiachronForthServices.war) has been created w.r.t. the configuration file config.properties. If you want to deploy the existing war file in your own application server, note that the existing one was tested successfully on a Tomcat Server 7.0.54 (Windows 7) and the configuration was place in folder C:/. Alternatively, you can make changes in the source code in order to read the properties file from another directory and recreate your own war file.
