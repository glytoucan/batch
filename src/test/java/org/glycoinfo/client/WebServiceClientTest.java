package org.glycoinfo.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WebServiceClientTest.class})
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
	public void image() throws Exception {
		GlyspaceClient gsClient = new GlyspaceClient();
		String image = gsClient.getImage("https://test.glytoucan.org", "RES\\n"
				+ "1b:x-dglc-HEX-x:x\\n"
				+ "2b:b-dgal-HEX-1:5\\n"
				+ "3b:a-dgal-HEX-1:5\\n"
				+ "4b:b-dgal-HEX-1:5\\n"
				+ "5s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(4+1)2d\\n"
				+ "2:2o(3+1)3d\\n"
				+ "3:3o(3+1)4d\\n"
				+ "4:4d(2+1)5n");
		Assert.assertNotNull(image);
		logger.debug(image);
	}

	@Test
	public void imageTest() throws Exception {
		GlyspaceClient gsClient = new GlyspaceClient();
		String image = gsClient.getImage("http://localhost:8081", "RES\\n"
				+ "1b:x-dgal-HEX-1:5\\n"
				+ "2b:x-dman-HEX-1:5\\n"
				+ "3s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n"
				+ "2:1d(2+1)3n");
		Assert.assertNotNull(image);
		logger.debug(image);
	}

	@Test
	public void imageTestmanglc() throws Exception {
		GlyspaceClient gsClient = new GlyspaceClient();
		String image = gsClient.getImage("http://beta.glytoucan.org", "RES\\n"
				+ "1b:x-dman-HEX-1:5\\n"
				+ "2b:x-dglc-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n");
		Assert.assertNotNull(image);
		logger.debug(image);
	}
	
	@Test
	public void testRead() throws IOException {
	    /* Test image to string and string to image start */
        BufferedImage img = ImageIO.read(new File("/home/aoki/workspace/rdf.glytoucan/src/test/resources/image.png"));
        BufferedImage newImg;
        String imgstr;
		GlyspaceClient gsClient = new GlyspaceClient();
        imgstr = gsClient.encodeToString(img, "png");
        System.out.println(imgstr);
        newImg = gsClient.decodeToImage(imgstr);
        ImageIO.write(newImg, "png", new File("/tmp/CopyOfTestImage.png"));
        /* Test image to string and string to image finish */
	}
	
	@Test
	public void testReadMs() throws IOException {
		String ttl = msdbClient.getTtl("a-dall-HEX-1:5|2,3:anhydro");
		
		logger.debug(ttl);
	}
}