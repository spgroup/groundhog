package br.ufpe.cin.groundhog.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;

public class Requests {
	private AsyncHttpClient asyncClient;
	
	public Requests() {
		this.asyncClient = new AsyncHttpClient();
	}
	
	/**
	 * Gets the response body of the given URL
	 * @param urlStr
	 * @return
	 * @throws IOException
	 */
	public String get(String urlStr) throws IOException {
		try {
			return this.asyncClient.prepareGet(urlStr).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new HttpException(e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	/**
	 * Downloads the response body of the given URL
	 * @param urlStr an URL of a page whose body will be downloaded
	 * @return
	 * @throws IOException
	 */
	public InputStream download(String urlStr) throws IOException {
		try {
			return this.asyncClient.prepareGet(urlStr).setFollowRedirects(true).execute().get().getResponseBodyAsStream();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new HttpException(e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	public <T> ListenableFuture<T> getAsync(String urlStr, AsyncCompletionHandler<T> callback) throws IOException {
		return asyncClient.prepareGet(urlStr).execute(callback);
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
	
	/**
	 * Closes the connection
	 */
	public void close() {
		this.asyncClient.close();
	}
}