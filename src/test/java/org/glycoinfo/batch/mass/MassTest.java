package org.glycoinfo.batch.mass;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.array.RES;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.rdf.SparqlException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MassTest.class)
@Configuration
@EnableAutoConfiguration
public class MassTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(MassTest.class);

	@Test
	public void testMassCalculator() throws SparqlException, WURCSFormatException {
		String input = "WURCS=2.0/7,10,9/[x2122h-1x_1-5_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5][12112h-1b_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/"
				+ "1-2-3-4-2-5-4-2-6-7/" +
				// "a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3\\c6_g1-c3\\c6";
				"a4-b1_a6-j1_b4-c1_d2-e1_e4-f1_g2-h1_h4-i1_d1-c3_g1-c6*S*~10:100";

//		WURCSArray t_objWURCS = new WURCSArray("2.0", 0, 0, 0);
		WURCSArray t_objWURCS = new WURCSArray("2.0", 0, 0 , 0 , false);
		WURCSImporter t_objImporter = new WURCSImporter();

		t_objWURCS = t_objImporter.extractWURCSArray(input);

		LinkedList<RES> testRESs = t_objWURCS.getRESs();
		double testMass = 0;
		double mass  = 0;
		try {
			testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		System.out.println(t_objWURCS + " : " + testMass);

		testMass = WURCSMassCalculator.getMassSkeletonCode("u2122h");
		System.out.println(testMass);
		System.out.println();

		// Calcurate mass from WURCS
		mass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		} catch (WURCSMassException e) {
			e.printStackTrace();
			Assert.assertTrue(e.getMessage().contains("repeating unit"));
		}
		System.out.println(mass);

	}

	@Test
	public void testMassCalculator2() throws SparqlException, WURCSMassException {
		String input = "WURCS=2.0/3,4,3/[x2122h-1x_1-5_2*NCC/3=O_6*OSO/3=O/3=O][12112h-1b_1-5][12122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2/a4-b1_b3-c1_c4-d1";

		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input);
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		double testMass = 0;
		testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		System.out.println(t_objWURCS + " : " + testMass);

		testMass = WURCSMassCalculator.getMassSkeletonCode("u2122h");
		System.out.println(testMass);
		System.out.println();

		// Calcurate mass from WURCS
		double mass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		System.out.println(mass);

	}

	@Test
	public void testMassCalculator3() throws SparqlException, WURCSMassException {
		String input = "WURCS=2.0/4,5,4/[11122h-1b_1-5][12122a-1b_1-5][12122h-1b_1-5][12112h-1b_1-5_3-4*OC^XO*/3CO/6=O/3C]/1-1-2-3-4/a4-b1_b3-c1_b4-d1_b6-e1";

		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input);
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		double testMass = 0;
		testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		System.out.println(t_objWURCS + " : " + testMass);
		System.out.println();
	}

	@Test
	public void testMassCalculator4() throws SparqlException {
		String input = "WURCS=2.0/2,2,2/[<0>-?b][a6d21121m-2a_2-6_5*NCC/3=O_7*NCC/3=O]/1-2/a3-b2_a1-b4~n";

		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input);
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		double testMass = 0;
		try {
			testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		} catch (WURCSMassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(t_objWURCS + " : " + testMass);
		System.out.println();
	}
	

	@Test
	public void testMassCalculatorUnknownCarbonLength() throws SparqlException {
		String input = "WURCS=2.0/2,2,2/[<0>-?b][a6d21121m-2a_2-6_5*NCC/3=O_7*NCC/3=O]/1-2/a3-b2_a1-b4~n";

		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input);
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		double testMass = 0;
		try {
			testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		} catch (WURCSMassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(e.getMessage().contains("unknown carbon length"));
		}
		System.out.println(t_objWURCS + " : " + testMass);
		System.out.println();
	}
	
	@Test
	public void testNotMatchAsLip() throws SparqlException {
		String input = "WURCS=2.0/5,5,4/[x1122h-1x_1-5][21122h-1a_1-5][22112h-1a_1-5][22122h-1a_1-5][12112h-1b_1-5_4,6*OC^XO*/3CO/6=O/3C]/1-2-3-4-5/a2-b1_b2-c1_b3-d1_d4-e1";

		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(input);
		} catch (WURCSFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		double testMass = 0;
		try {
			testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
		} catch (WURCSMassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(t_objWURCS + " : " + testMass);
		System.out.println();
	}
	
}