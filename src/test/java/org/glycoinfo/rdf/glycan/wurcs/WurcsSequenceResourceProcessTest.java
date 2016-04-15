/**
 * 
 */
package org.glycoinfo.rdf.glycan.wurcs;

import org.glycoinfo.rdf.ResourceProcessException;
import org.glycoinfo.rdf.ResourceProcessResult;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.glycan.GlycoSequenceResourceProcess;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import jp.bluetree.log.LevelType;

/**
 * @author aoki
 *
 * This work is licensed under the Creative Commons Attribution 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { WurcsSequenceResourceProcessConfig.class, VirtSesameDAOTestConfig.class })
public class WurcsSequenceResourceProcessTest {

	@Autowired
	GlycoSequenceResourceProcess wurcsSequenceResourceProcess;
	
	/**
	 * Base case test of a blank sequence.  Should return exception as the sequence format cannot be detected.
	 * 
	 * Test method for {@link org.glycoinfo.rdf.glycan.wurcs.WurcsSequenceResourceProcess#processGlycoSequence(java.lang.String)}.
	 */
	@Test(expected=ResourceProcessException.class)
	public void testProcessGlycoSequenceBlank() throws Exception {
		// from org.glycoinfo.convert.ConvertTest
		ResourceProcessResult result = wurcsSequenceResourceProcess.processGlycoSequence("", "0");
		
		Assert.assertTrue(result.getLogMessage().getLevel().equals(LevelType.ERROR));
	}
	
	/**
	 * Already registered and Conversion test case.  Already registered as G15021LG.
	 * Test method for {@link org.glycoinfo.rdf.glycan.wurcs.WurcsSequenceResourceProcess#processGlycoSequence(java.lang.String)}.
	 */
	@Test
	@Transactional
	public void testProcessGlycoSequenceGlycoctExisting() throws Exception {
		// from org.glycoinfo.convert.ConvertTest
		String glycoct = "RES\n" + "1b:x-dglc-HEX-1:5";
		
		ResourceProcessResult result = wurcsSequenceResourceProcess.processGlycoSequence(glycoct, "0");
		
		Assert.assertTrue(result.getLogMessage().getLevel().equals(LevelType.WARN));
		Assert.assertTrue(result.getLogMessage().getMessage().contains("G15021LG"));
	}
	
	/**
	 * New Sequence and Conversion test case.  Already registered as G15021LG.
	 * Test method for {@link org.glycoinfo.rdf.glycan.wurcs.WurcsSequenceResourceProcess#processGlycoSequence(java.lang.String)}.
	 */
	@Test
	@Transactional
	public void testProcessGlycoSequenceGlycoctNew() throws Exception {
		String glycoct = "RES\n"
				+ "1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n"
				+ "2b:x-dgal-HEX-1:5\n"
				+ "3b:x-llyx-PEN-1:5\n"
				+ "4s:n-acetyl\n"
				+ "5s:n-acetyl\n"
				+ "LIN\n"
				+ "1:1o(-1+1)2d\n"
				+ "2:2o(-1+1)3d\n"
				+ "3:2d(2+1)4n\n"
				+ "4:1d(5+1)5n";
		
		ResourceProcessResult result = wurcsSequenceResourceProcess.processGlycoSequence(glycoct, "0");
		
		Assert.assertTrue(result.getLogMessage().getLevel().equals(LevelType.INFO));
	}
}
