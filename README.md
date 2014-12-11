# DIACHRON Integration Layer #

All code of DIACHRON Integrated Platform can be found at (https://github.com/diachron/IntegrationLayer). Please use branching in order to do maintain the initial code.

For more information about the configuration and inner workings of each components, please advice the associated deliverables (D3.1, D3.2, D5.1, D4.1, D4.2, D6.3)

Please make sure that before you start running the layer, you always do a ```git pull``` to fetch the latest updates from the repository.
 
You can install the components in the following sections in the following order:

## a. Virtuoso installation ##

For installing Virtuoso 7 (building fom source), we recommend following these [instructions](http://virtuoso.openlinksw.com/dataspace/doc/dav/wiki/Main/VOSBuild)


## b. ActiveMQ Deployment ##

ActiveMQ can be found [here](http://activemq.apache.org/activemq-5100-release.html). Please use this version, as it is the one test from Integrated Layer version 1.0

There now follows instructions on how to run the ActiveMQ Message Broker out-of-the-box, as it is needed for DIACHRON.

1. From a command shell, change to the installation directory and run ActiveMQ:
	```cd [activemq_install_dir]```

where ```activemq_install_dir``` is the directory in which ActiveMQ was installed, e.g., /usr/local/activemq-4.x.

2. Then type:
  <code>bin/activemq  start [on unix-like systems]
  bin\activemq  start [on Windows]</code>
 
3. For shutting down, you can do:
  ```bin/activemq  stop```


## c. Platform Layer ##

###  Detection Module ###
At the time the IntegrationLayer was created, the Change Detection module was not in a Maven form so, it was uploaded as a Webapplication project with the correlated services. However, it is now converted in a Maven project and it can be found under this brach: https://github.com/diachron/detection_repair_maven. This branch contains the pom file along with the required libraries. 

Details on how it can be configured and deployded can be found in the [Readme](https://github.com/diachron/detection_repair_maven/blob/master/README.md) file which can be found in the branch as well. 


###  Archive Module ###

The archive module consists of two sub modules, the archive system and the archive web services. These two are structured are as maven modules under a single parent project. The archive web services module is dependent on archive system module.
Build instructions

1.Build first the archive system module using the following maven command on this module that will build it and install it to the local repository so as to be accessible for the other module

mvn clean install

There are two dependencies that might not be accessible in public online maven repositories. The virtuoso jdbc driver and the virtuoso provider for Jena. Currently they are accessible from here

http://maven.aksw.org/repository/internal/com/openlink/virtuoso/virtjdbc4/ http://maven.aksw.org/repository/internal/com/openlink/virtuoso/virt_jena2/

Alternatively you can download them from here http://virtuoso.openlinksw.com/dataspace/doc/dav/wiki/Main/VOSDownload/virtjdbc4.jar http://opldownload.s3.amazonaws.com/uda/virtuoso/rdfproviders/jena/210/virt_jena2.jar

and add them to your local repository following these instructions

http://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

2.Edit the “virt-connection.properties” under the “diachron-archive\archive-web-services\src\main\resources\” in order to provide your virtuoso connection properties More details about this in deliverable D6.3 (Section 3.1.5.2)

3.The build and package the archive web services module by executing the following maven command

mvn clean package

This will produce a war file including all the necessary dependencies. You can then deploy the war file as standard web application in Tomcat or JBoss.

    Status
    API
    Training
    Shop
    Blog
    About




###  Quality Assessment Module ###

In order to facilitate the building and execution of the quality assessment module, a script [assessment](https://raw.githubusercontent.com/diachron/IntegrationLayer/master/CoreServices/quality-luzzu-integration/assessment.sh) is available as part of the module. 

The module requires some dependencies before building the module. For the first execution of the quality assessment module the following is required: ```./assessment dependencies```. For subsequent builds this is optional, but it would ensure that the module runs on the latest dependencies.

To build the package the script is run with the build parameter ```./assessment build```. This will build the DIACHRON metrics, quality framework and set up the required resources.

To run the assessment framework, the run parameter is required - ```./assessment run```. This will start a local webservice on port 8080.
 
The module has one configuration file webservices.properties. This allows you to change the domain, port and application name for the web service. Changing the properties file would require a new build. 

For more information please check the module [README.MD](https://raw.githubusercontent.com/diachron/IntegrationLayer/master/CoreServices/quality-luzzu-integration/README.md) file

## d. Integration Layer Building ##

To build the DIACHRON Integration layer execute ```mvn clean install``` in the terminal.

The three main configuration files that need manipulation are:
* web.xml
* integration_platform.properties
* apache_shiro.properties

More information can be found at D6.3
