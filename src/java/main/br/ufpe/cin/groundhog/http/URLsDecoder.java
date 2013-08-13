package br.ufpe.cin.groundhog.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
/**
 * 
 * @author ghlp
 * @since 0.1.0
 */
public class URLsDecoder {

	/**
	 * 
	 * @param s a String representing an URL
	 * @return encoded URL
	 */
	public static String encodeURL(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	/**
	 * 
	 * @param s a String representing an URL
	 * @return an UTF-8 decoded URL
	 */
	public static String decodeURL(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
}
