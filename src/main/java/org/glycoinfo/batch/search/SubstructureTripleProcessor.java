package org.glycoinfo.batch.search;
//package org.glycoinfo.batch.substructure;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.glycoinfo.conversion.GlyConvert;
//import org.glycoinfo.rdf.dao.SparqlDAO;
//import org.glycoinfo.rdf.dao.SparqlEntity;
//import org.glycoinfo.search.GlySearch;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.test.context.ContextConfiguration;
//
//public class SubstructureTripleProcessor implements
//		ItemProcessor<MotifSelectSparql, MotifSelectSparql> {
//	protected Log logger = LogFactory.getLog(getClass());
//
//	protected static Map<String, Object> data;
//	protected static HashMap<String, String> iris = new HashMap<String, String>();
//
//	@Autowired
//	private SparqlDAO schemaDAO;
//
//	@Autowired
//	GlySearch glySearch;
//
//	public GlySearch getGlySearch() {
//		return glySearch;
//	}
//
//	public void setGlySearch(GlySearch glySearch) {
//		this.glySearch = glySearch;
//	}
//
//	@Override
//	public MotifSelectSparql process(final MotifSelectSparql triple)
//			throws Exception {
//		MotifSelectSparql transformedTriple = new MotifSelectSparql();
//
//		if (data == null || data.size() == 0 || iris == null
//				|| iris.size() == 0) {
//			logger.debug("schemaDAO.query(triple.getProcessQuery())" + triple.getProcessQuery());
//			List<SparqlEntity> list = schemaDAO.query(triple.getProcessQuery());
//			data = new HashMap<String, Object>();
//			iris = new HashMap<String, String>();
//
//			for (SparqlEntity schemaEntity : list) {
//				data.put(schemaEntity.getValue("AccessionNumber"),
//						schemaEntity.getValue("Seq"));
//				iris.put(schemaEntity.getValue("AccessionNumber"),
//						schemaEntity.getValue("glycans"));
//			}
//		}
//
//		getGlySearch().setKeyword(triple.getKeyword());
//		getGlySearch().setData(data);
//
//		BeanUtils.copyProperties(triple, transformedTriple);
//
//		logger.debug("getGlySearch().search()>" + triple.getKeyword() + "<");
//		List<String> results = getGlySearch().search();
//		logger.debug("getGlySearch().search() completed");
//		transformedTriple.setSubIri(new HashSet<String>());
//		if (null != results) {
//			for (String string : results) {
//				if (!string.equals(triple.getIdent())) // logic
//					transformedTriple.getSubIri().add(iris.get(string));
//			}
//		}
//
////		logger.debug("Converting (" + triple + ") into (" + transformedTriple
////				+ ")");
//
//		return transformedTriple;
//	}
//}