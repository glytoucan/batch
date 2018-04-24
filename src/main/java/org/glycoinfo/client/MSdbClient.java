package org.glycoinfo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MSdbClient {
	
	@Value("${msDb.rdf}")
	String msdbRdf;
	
	protected Log logger = LogFactory.getLog(getClass());
	private final String USER_AGENT = "Mozilla/5.0";
	
	public String getTtl(String key) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		
		String url = msdbRdf + URLEncoder.encode(key, "UTF-8");
		logger.debug("url:>" + url);
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);

		logger.debug("Response Code : " 
	                + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			line = line.trim();
			if (StringUtils.isBlank(line) || line.startsWith("@prefix"))
				continue;
			logger.debug("line:>" + line + "<");
			result.append(line + "\n");
		}
		
//		SSLContextBuilder builder = new SSLContextBuilder();
//	    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
//	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//	            builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//	    CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(
//	            sslsf).build();
//		HttpPost post = new HttpPost(url);
//
//		// add header
//		post.setHeader("Content-Type", "application/json");

//		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		
//		Map<String,Object> data = new HashMap<String,Object>();
//		data.put("structure", sequence);
//		data.put("encoding", "glycoct");
//		ObjectMapper mapper = new ObjectMapper();
//		String output = mapper.writeValueAsString(data);
//		String output2 = "{\"encoding\":\"glycoct\",\"structure\":\"" + sequence + "\"}"; 
//		logger.debug("output:"+output);
//		logger.debug("output:"+output2);
		
//		post.setEntity(new StringEntity(output2));

//		HttpResponse response = client.execute(get);
//		int code = response.getStatusLine().getStatusCode();
//		logger.debug("status code:"+code);
//		if (code != 200) {
//			return null;
//		}
//
//		BufferedReader rd = new BufferedReader(
//		        new InputStreamReader(response.getEntity().getContent()));
//
//		StringBuilder result = new StringBuilder();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			logger.debug("line:>" + line);
//			result.append(line);
//		}
//        BufferedImage bufimage = ImageIO.read(response.getEntity().getContent());

//		String image = encodeToString(bufimage, "png"); 
		
		return result.toString();
	}
	
	
	
}