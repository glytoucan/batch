package org.glycoinfo.rdf.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.codec.digest.Sha2Crypt;

public class NumberGenerator {

	/** Random Number Generator.
     * 
     */
    public final static Random generator = new Random();

    
    /**
     * Generates a random alphanumeric string.
     * 
     * @param len The length of the string to be generated.
     * @return A random alphanumeric string.
     */
    public static String generateRandomString(int len) {
    	// Math.min avoids out of heap space errors
        int maxLen=Math.min(len, 1000);
    	
        StringBuffer sb = null;
        
        sb = new StringBuffer(maxLen);
        for (int i=0; i<maxLen; i++) {
            if (i >= maxLen-2) {   // last two digits are characters
                //Generate Character
                sb.append((char) (generator.nextInt(26)+65));               
            } else {
                //Generate Digit
                sb.append(generator.nextInt(10));
            }
        }

        return sb.toString();
    }
    
    /**
     * Generates a random alphanumeric string.
     * 
     * @param upperLimit the upperLimit of the integer part to be generated
     * @param len The length of the string to be generated.
     * @return A random alphanumeric string.
     */
    public static String generateRandomString(int upperLimit, int len) {
        
    	StringBuffer sb = new StringBuffer(len);
    	Integer randomInteger = generator.nextInt(upperLimit + 1);
    	StringBuffer integerString = new StringBuffer(randomInteger.toString());
    	while (integerString.length() < len) {
    		// pad with zeros
    		integerString.append("0");
    	}
    	// randomly throw away 2 digits
    	Integer index1 = generator.nextInt(len);
    	Integer index2 = generator.nextInt(len);
    	String integerS = new String(randomInteger.toString());
    	for (int i=0; i < integerS.length(); i++) {
    		if (index1 != i && index2 != i) {
    			sb.append(integerS.charAt(i));
    		}
    	}
    	for (int i=0; i<2; i++) {
    		sb.append((char) (generator.nextInt(26)+65));
    	}

    	return sb.toString(); 
    }

    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

    public static String generateHash(String input, Date salt) {
    	return generateHash(input, sdf.format(salt));
    }

    public static String generateHash(String input, String salt) {
    	String hash = Sha2Crypt.sha256Crypt(input.getBytes(), "$5$" + salt);
    	return org.apache.commons.codec.digest.DigestUtils.sha256Hex(Base64.encodeBase64String(hash.getBytes()));
    }
    
    public static String generateSHA256Hash(String input) {
      return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
    }    
}
