package org.glycoinfo.batch.glyconvert;

import org.glycoinfo.convert.GlyConvert;

/**
 * 
 * This interface enforces the use of GlyConvert, a necessary class for all conversion batches.
 * 
 * @author aoki
 *
 */
public interface GlyConvertSparql {
    public static final String DoNotFilter = "DoNotFilter";
	public GlyConvert getGlyConvert();
}