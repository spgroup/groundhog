package br.ufpe.cin.groundhog.search;

import java.util.Map;

import com.google.inject.name.Named;

/**
 * @author ghlp
 * @since 0.1.0
 */
public final class UrlBuilder {

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

	public UrlBuilder(@Named("githubOauthToken") String oauthToken) {
		this.oauthToken = oauthToken;
		this.builder = new StringBuilder();
	}

	public UrlBuilder uses(GithubAPI api) {
		this.builder.append(api.baseForm());
		return this;
	}
	
	public UrlBuilder withParam(String value) {
		this.builder.append(value);
		return this;
	}
	
	public UrlBuilder withParam(String key, Object value) {
		String concat = isFirstParam() ? "?" : "&";
		
		String param = String.format("%s%s=%s", concat, key, value);
		this.builder.append(param);
		return this;
	}
	
	public UrlBuilder withSimpleParam(String key, Object value) {
		this.builder.append(key).append(value);
		return this;
	}

	private boolean isFirstParam() {
		return !builder.toString().contains("?");
	}

	public UrlBuilder withParam(Map<String, Object> params) {
		throw new UnsupportedOperationException("not implemented yet.");
	}

	public String build() {
		String result = this.builder.append(oauthToken).toString();
		this.builder.delete(0, result.length());
		return result;
	}
}