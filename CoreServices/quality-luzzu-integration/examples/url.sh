#!/bin/bash
var=$(pwd)
fileName="http://aksw.org/model/export/?m=http%3A%2F%2Faksw.org%2F%26f=turtle"
curl -v --data "Dataset=$fileName&QualityReportRequired=false&MetricsConfiguration={\"@id\":\"_:f4216607408b1\",\"@type\":[\"http://github.com/EIS-Bonn/Luzzu#MetricConfiguration\"],\"http://github.com/EIS-Bonn/Luzzu#metric\":[{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.POBODefinitionUsage\"}]}" http://localhost:5677/Luzzu/compute_quality ;
