package br.cin.ufpe.epona.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class ParamBuilder {
	private List<NameValuePair> params;

	public ParamBuilder() {
		params = new ArrayList<NameValuePair>();
	}

	public ParamBuilder add(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
		return this;
	}

	public String build() {
		return URLEncodedUtils.format(params, "UTF-8");
	}
}
