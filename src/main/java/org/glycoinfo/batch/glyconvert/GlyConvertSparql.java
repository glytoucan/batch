package org.glycoinfo.batch.glyconvert;

import org.glycoinfo.conversion.GlyConvert;

/**
 * 
 * This interface enforces the use of GlyConvert, a necessary class for all conversion batches.
 * 
 * @author aoki
 *
 */
public interface GlyConvertSparql {
	public GlyConvert getGlyConvert();
}