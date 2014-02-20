package br.ufpe.cin.groundhog.search;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Use this class when you need to create a github url, instead doing so by
 * string concatenation.
 * 
 * @author ghlp
 * @since 0.1.0
 */
public final class UrlBuilder {

	/**
	 * Root sources of the API
	 * 
	 * @author ghlp
	 */
	enum GithubAPI {
		LEGACY_V2 {
			@Override
			public String baseForm() {
				return String.format("%slegacy/repos/search/", ROOT.baseForm());
			}
		},
		ROOT {
			@Override
			public String baseForm() {
				return "https://api.github.com/";
			}
		}, 
		REPOSITORIES {
			@Override
			public String baseForm() {
				return String.format("%srepositories", ROOT.baseForm());
			}
		},
		USERS {
			@Override
			public String baseForm() {
				return "https://api.github.com/users/";
			}
		};

		public abstract String baseForm();
	}

	private final String oauthToken;
	private final StringBuilder builder;

	@Inject
	public UrlBuilder(@Named("githubOauthToken") String oauthToken) {
		this.oauthToken = oauthToken;
		this.builder = new StringBuilder();
	}

	/**
	 * Choose what will be your base API.
	 * Currently available: USERS, REPOSITORES, and LEGACY
	 * see {@link GithubAPI}

	 * @param api
	 * @return current url representation
	 */
	public UrlBuilder uses(GithubAPI api) {
		this.builder.append(api.baseForm());
		return this;
	}
	
	/**
	 * Add a parameter to the URL
	 * 
	 * @param value
	 * @return current url representation
	 */
	public UrlBuilder withParam(String value) {
		this.builder.append(value);
		return this;
	}
	
	/**
	 * Add a parameter and value to the URL. It will be concatenated with ? or
	 * &, depending on whether it is the first parameter or not.
	 * 
	 * @param key
	 * @param value
	 * @return current url representation
	 */
	public UrlBuilder withParam(String key, Object value) {
		String concat = isFirstParam() ? "?" : "&";
		
		String param = String.format("%s%s=%s", concat, key, value);
		this.builder.append(param);
		return this;
	}

	/**
	 * Add a parameter and value to the URL. Differently of {@link withparam},
	 * this method will not use ? or & to concatenate the url.
	 * 
	 * @param key
	 * @param value
	 * @return current url representation
	 */
	public UrlBuilder withSimpleParam(String key, Object value) {
		this.builder.append(key).append(value);
		return this;
	}

	private boolean isFirstParam() {
		return !builder.toString().contains("?");
	}

	public UrlBuilder withParam(Map<String, Object> params) {
		throw new UnsupportedOperationException("Sorry. Not implemented yet.");
	}

	/**
	 * Return the url as a string 
	 * 
	 * @return final url
	 */
	public String build() {
		if(this.builder == null) 
			throw new UnsupportedOperationException("Nenhum parametro de URL foi enviado!");
		
		String concat = isFirstParam() ? "?" : "&";
		String result = this.builder.append(concat).append(oauthToken).toString();
		this.builder.delete(0, result.length());
		return result;
	}
}