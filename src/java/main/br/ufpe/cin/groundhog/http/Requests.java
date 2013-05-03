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
		asyncClient = new AsyncHttpClient();
	}
	
	public String get(String urlStr) throws IOException {
		try {
			return asyncClient.prepareGet(urlStr).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new HttpException(e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	public InputStream download(String urlStr) throws IOException {
		try {
			return asyncClient.prepareGet(urlStr).setFollowRedirects(true).execute().get().getResponseBodyAsStream();
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
	 * @return an UTF-8 decoded String derivated from the given URL
	 */
	public String decodeURL(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}

	public void close() {
		asyncClient.close();
	}
	
}