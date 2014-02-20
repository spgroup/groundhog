package br.ufpe.cin.groundhog.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * The URL parameter builder class
 * @author fjsj
 * @since 0.0.1
 * @deprecated
 */
public class ParamBuilder {
	private List<NameValuePair> params;

	public ParamBuilder() {
		this.params = new ArrayList<NameValuePair>();
	}

	public ParamBuilder add(String name, String value) {
		this.params.add(new BasicNameValuePair(name, value));
		return this;
	}

	public String build() {
		return URLEncodedUtils.format(this.params, "UTF-8");
	}
}