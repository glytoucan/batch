package org.glycoinfo.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.glycoinfo.convert.GlyConvertConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WebServiceClientTest.class})
@EnableAutoConfiguration
public class WebServiceClientTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(WebServiceClientTest.class);

	@Bean
	MSdbClient msdbClient() {
		return new MSdbClient();
	}
	
	@Autowired
	MSdbClient msdbClient;
	
	@Test
	public void testReadMs() throws IOException {
		String ttl = msdbClient.getTtl("a-dall-HEX-1:5|2,3:anhydro");
		
		logger.debug(ttl);
	}
}