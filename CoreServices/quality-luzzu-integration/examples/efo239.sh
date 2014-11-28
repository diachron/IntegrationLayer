#!/bin/bash
var=$(pwd)
fileName="$var/efo-2.39.rdf"
curl --data "Dataset=$fileName&QualityReportRequired=false&MetricsConfiguration={\"@id\":\"_:f4216607408b1\",\"@type\":[\"http://github.com/EIS-Bonn/Luzzu#MetricConfiguration\"],\"http://github.com/EIS-Bonn/Luzzu#metric\":[{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.POBODefinitionUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.HomogeneousDatatypes\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.ObsoleteConceptsInOntology\"},{\"@value\":\"eu.diachron.qualitymetrics.representational.understandability.LowBlankNodeUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.representational.understandability.HumanReadableLabelling\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.SynonymUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.DefinedOntologyAuthor\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.conciseness.OntologyVersioningConciseness\"},{\"@value\":\"eu.diachron.qualitymetrics.accessibility.performance.DataSourceScalability\"},{\"@value\":\"eu.diachron.qualitymetrics.dynamicity.timeliness.TimelinessOfResource\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.EntitiesAsMembersOfDisjointClasses\"}]}" http://localhost:8080/luzzu/compute_quality ;
