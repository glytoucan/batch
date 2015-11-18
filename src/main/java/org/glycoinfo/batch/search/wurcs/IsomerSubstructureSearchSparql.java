package org.glycoinfo.batch.search.wurcs;

import org.glycoinfo.WURCSFramework.util.rdf.WURCSSequence2ExporterSPARQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IsomerSubstructureSearchSparql extends SubstructureSearchSparql {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(IsomerSubstructureSearchSparql.class);

	public IsomerSubstructureSearchSparql() {
		super();
	}

	@Override
	WURCSSequence2ExporterSPARQL getExporter() {		
		WURCSSequence2ExporterSPARQL t_oExport = super.getExporter();
		t_oExport.setSearchIsomer(true);
		t_oExport.setIgnoreSelf(true);
		return t_oExport;
	}
}