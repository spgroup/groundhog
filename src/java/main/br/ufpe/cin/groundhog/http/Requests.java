package br.ufpe.cin.groundhog.http;

import java.io.IOException;
import java.io.InputStream;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

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
	 * Gets the response body of the given URL using the preview flag in the request header
	 * This is for new API's that are still in preview mode
	 * @param urlStr
	 * @return the entire html content
	 */
	public String getWithPreviewHeader(String urlStr) {
		try {
			RequestBuilder builder = new RequestBuilder("GET");
		    Request request = builder.setUrl(urlStr)
		     .addHeader("Accept", "application/vnd.github.manifold-preview")
		     .build();
			return this.httpClient.prepareRequest(request).execute().get().getResponseBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpException(e);
		}
	}
	
	/**
	 * Downloads the response body of the given URL
	 * 
	 * @param urlStr an URL of a page whose body will be downloaded
	 * @return the entire html content as an InputStream
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
}
