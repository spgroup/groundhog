package br.ufpe.cin.groundhog.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;

/**
 * Utility class to perform asynchronous http requests
 * 
 * @author ghlp, fjsj
 * @since 0.0.1
 */
public class Requests {
	private final AsyncHttpClient httpClient;
	
	public Requests() {
		this.httpClient = new AsyncHttpClient();
	}
	
	/**
	 * Gets the response body of the given URL
	 * @param urlStr
	 * @return the entire html content
	 * @throws HttpException
	 */
	public String get(String urlStr) {
		try {
			return this.httpClient.prepareGet(urlStr).execute().get().getResponseBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	/**
	 * Downloads the response body of the given URL
	 * @param urlStr an URL of a page whose body will be downloaded
	 * @return the entire html content as an InputStream
	 * @throws HttpException
	 */
	public InputStream download(String urlStr) {
		try {
			return this.httpClient.prepareGet(urlStr).setFollowRedirects(true).execute().get().getResponseBodyAsStream();
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	public <T> ListenableFuture<T> getAsync(String urlStr, AsyncCompletionHandler<T> callback) throws IOException {
		return httpClient.prepareGet(urlStr).execute(callback);
	}
	
	public String encodeURL(String s) {
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
	 * @return an UTF-8 decoded String derived from the given URL
	 */
	public String decodeURL(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
}