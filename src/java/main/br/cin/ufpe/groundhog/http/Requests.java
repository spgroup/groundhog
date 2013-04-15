package br.cin.ufpe.groundhog.http;

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
	private static Requests instance;
	
	public static Requests getInstance() {
		if (instance == null) {
			instance = new Requests();
		}
		return instance;
	}
	
	private AsyncHttpClient asyncClient;
	
	private Requests() {
		asyncClient = new AsyncHttpClient();
	}
	
	public String get(String urlStr) throws IOException {
		try {
			return asyncClient.prepareGet(urlStr).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public InputStream download(String urlStr) throws IOException {
		try {
			return asyncClient.prepareGet(urlStr).setFollowRedirects(true).execute().get().getResponseBodyAsStream();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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
			new RuntimeException(e);
		}
		return null;
	}
	
	public String decodeURL(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
		return null;
	}

	public void close() {
		asyncClient.close();
		instance = null;
	}
	
}