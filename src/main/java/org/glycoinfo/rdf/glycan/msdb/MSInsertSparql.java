package org.glycoinfo.rdf.glycan.msdb;

import java.io.IOException;

import org.glycoinfo.client.MSdbClient;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.Monosaccharide;
import org.springframework.beans.factory.annotation.Autowired;

public class MSInsertSparql extends InsertSparqlBean {

	// NONE,URI,URL,STRING,INT,DOUBLE,BOOL
	String[][] props= {
			{"http://purl.jp/bio/12/glyco/glycan#has_msdb_id","INT"},
			{"http://purl.jp/bio/12/glyco/glycan#has_basetype","URL"},
			{"http://purl.jp/bio/12/glyco/glycan#has_alias_name","STRING"},
			{"http://purl.jp/bio/12/glyco/glycan#has_linkage_position","INT"},
			{"http://purl.jp/bio/12/glyco/glycan#has_linkage_type","URI"},
			{"http://purl.jp/bio/12/glyco/glycan#has_modification_position","UNKNOWN"},
			{"http://purl.jp/bio/12/glyco/glycan#has_monoisotopic_molecular_weight","DOUBLE"},
			{"http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme","URI"},
			{"http://purl.jp/bio/12/glyco/glycan#has_substitution","UNKNOWN"},
			{"http://purl.jp/bio/12/glyco/glycan#has_external_substituent","UNKNOWN"},
			{"http://purl.jp/bio/12/glyco/glycan#has_substituent_type","URI"},
			{"http://purl.jp/bio/12/glyco/glycan#has_substitution_name","STRING"},
			{"http://purl.jp/bio/12/glyco/glycan#is_primary_name","BOOL"},
			{"http://purl.jp/bio/12/glyco/glycan#is_trivial_name","BOOL"},
			{"http://www.w3.org/1999/02/22-rdf-syntax-ns#type","UNKNOWN"},
			{"http://www.w3.org/2000/01/rdf-schema#seeAlso","URL"},
			{"http://www.w3.org/2002/07/owl#sameAs","URL"}
	};
	
	@Autowired
	MSdbClient msdbClient;
	
	public MSInsertSparql() {
		super();
//		this.prefix="PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n";
	}
	
	@Override
	public String getInsert() throws SparqlException  {
        String rdf = null;

        String res = getSparqlEntity().getValue(Monosaccharide.Residue);
		
//		try {
//			res = URLEncoder.encode(res, "UTF-8");
//		} catch (UnsupportedEncodingException e2) {
//			throw new SparqlException(e2);
//		}
//
//        String url = "http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=" + res;
//        logger.debug("url:>" + url);
//        Model model = ModelFactory.createDefaultModel();
//        try{
//        	model.read(url, null, "TTL");
//        }catch(Throwable th){
//        }
//        
//        String dec_url = url;
//        try {
//			dec_url = java.net.URLDecoder.decode(dec_url,"UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//        
//        Model model_x = ModelFactory.createDefaultModel();
//        model_x.setNsPrefix("glycan", "http://purl.jp/bio/12/glyco/glycan#");
////        model_x.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
////        model_x.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
//        model.add(model.createResource(url), model.createProperty("http://www.w3.org/2002/07/owl#sameAs"), model.createResource(dec_url));
//		StmtIterator sit = model.listStatements();
//		List<Triple> triples = new ArrayList<Triple>();
//		while(sit.hasNext()){
//			Statement st = sit.next();
//			Resource s = st.getSubject();
//			Property p = st.getPredicate();
//			RDFNode o = st.getObject();
//			String prop_str = p.getURI();
//			//属性の種類を調べて型を合わせる
//			for( int i=0; i<props.length; i++ ){
//				if( props[i][0].equals(prop_str) ){
//					String o_str = o.toString();
//					if( "URL".equals(props[i][1]) ){
//						o = model.createResource(o_str);
//					}else if( "URI".equals(props[i][1]) ){
//						String[] strs = o_str.split(":");
//						String ns_url = model.getNsPrefixURI(strs[0]);
//						o_str = ns_url+strs[1];
//						o = model.createResource(o_str);
//					}else if( "INT".equals(props[i][1]) ){
//						if( !MSInsertSparql.numCheck(o_str) ){
//							o_str = "0";
//						}
//						o = model.createTypedLiteral(o_str, XSDDatatype.XSDinteger);
//					}else if( "BOOL".equals(props[i][1]) ){
////						if( "true".equals(o_str) ){
////							o = model.createTypedLiteral(true);
////						}else{
////							o = model.createTypedLiteral(false);
////						}
////						o = model.createLiteral(o_str);
//						o = model.createTypedLiteral(o_str, XSDDatatype.XSDboolean);
//					}else if( "DOUBLE".equals(props[i][1]) ){
////						o = model.createTypedLiteral(Double.valueOf(o_str));
////						o = model.createLiteral(o_str);
//						o = model.createTypedLiteral(o_str, XSDDatatype.XSDdouble);
//					}else if( "STRING".equals(props[i][1]) ){
//					}else if( "UNKNOWN".equals(props[i][1]) ){
//					}
//				}
//			}
//			if( s != null && p != null && o != null ){
//				triples.add(new Triple(s.asNode(), p.asNode(), o.asNode()));
//				model_x.add(s, p, o);
//			}
//		}
//		if( triples.size() > 0 ){
////			triples.add(new Triple(model_x.createResource(url).asNode(), model_x.createProperty(model_x.getNsPrefixURI("owl")+"sameAs").asNode(), model_x.createResource(dec_url).asNode()));
////			model_x.add(model_x.createResource(url), model_x.createProperty(model_x.getNsPrefixURI("owl")+"sameAs"), model_x.createResource(dec_url));
////			virt_opt = new VirtGraph(virt_ms_graph, virt_host, virt_user, virt_pass);
////			GraphUtil.add(virt_opt, triples);
////			virt_opt.close();
//			try {
////				String file_name = url.substring(url.lastIndexOf("/"));
////				file_name = file_name.replace("/monosaccharide.action?", "");
////				FileOutputStream ttl_out = new FileOutputStream(file_name+".ttl");
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				model_x.write(baos, "TURTLE");
//				baos.close();
//				String output = baos.toString();
//				logger.debug("output:>" + output);
//				rdf = output;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

		try {
			rdf = msdbClient.getTtl(res);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SparqlException(e);
		}

		return rdf;
	}
	/**
	 * 文字列が数値か否かチェックする
	 * @param str
	 * @return
	 */
	public static boolean numCheck(String str) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	@Override
	public String getFormat() {
		return InsertSparql.Turtle;
	}
}