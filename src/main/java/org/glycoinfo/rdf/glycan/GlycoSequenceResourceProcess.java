package org.glycoinfo.rdf.glycan;

import org.glycoinfo.rdf.ResourceProcessException;
import org.glycoinfo.rdf.ResourceProcessResult;

public interface GlycoSequenceResourceProcess {
	public ResourceProcessResult processGlycoSequence(String sequence, String contributorId) throws ResourceProcessException;
}