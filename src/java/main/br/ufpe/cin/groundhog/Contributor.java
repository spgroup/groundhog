package br.ufpe.cin.groundhog;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>Contributor</h1>
 * <p>
		This class represents a <i>Contributor</i> from a {@link Project}. 
	Sometimes a <i>Groundhog</i> user want to extract  metrics
	about the Contributors list from a project, with <i>Github</i>
	API we can get this list and store all individual
	data from a Contributor in this class.
 * </p>
 *@see
 * <p>This class have a list of attributes whom <b> should be set </b> after the instantiation because they are not being required by the constructor. These attributes represent the data of a Project <i>Contributor</i> on the <i>GitHub</i>, see they below. </p>   
 * <p>The attributes are at form bellow: </p>
 * <p>Class/type attributeName: What he represent </p>  
 * <ul>
 * 		 <li> <b>int</b> <i>id</i>: The unique id of the Contributor in question. 
 *		 <li> <b>int</b> <i>contributions</i>: Probably the most important data stored by this Class, the number of contributions in the <i>Project</i> by this Contributor.
 *		 <li> <b>boolean</b> <i>site_admin</i>: A boolean that represent if the Contributor is a site admin.
 *		 <li> <b>String</b> <i>avatar_url</i>: A String that represent the avatar_url.
 *		 <li> <b>String</b> <i>gravatar_id</i>: A String that represent the gravatar_id.
 *		 <li> <b>String</b> <i>html_url</i>: A String that represent the html_url.
 * 		 <li> <b>String</b> <i>followers_url</i>: A String that contains a link for all {@link User}'s that are following the <i>Contributor</i> in question.
 * 		 <li> <b>String</b> <i>following_url</i>: A String that contains a link for all {@link User}'s that the <i>Contributor</i> in question are following.
 *		 <li> <b>String</b> <i>gists_url</i>: A String that represent the gists_url.
 *		 <li> <b>String</b> <i>starred_url</i>: A String that represent the starred_url.
 *		 <li> <b>String</b> <i>subscriptions_url</i>: A String that represent the subscriptions_url.
 *		 <li> <b>String</b> <i>repos_url</i>: A String that represent the repos_url.
 *		 <li> <b>String</b> <i>events_url</i>: A String that represent the events_url.
 *		 <li> <b>String</b> <i>received_events_url</i>: A String that represent the received_events_url.
 *		 <li> <b>String</b> <i>type</i>: A String that represent the type.
 *		 
 * </ul> 
 * @author 
 * <p>Marlon Reghert (mras@cin.ufpe.br)</p>
 * <p>Tomer Simis (tls@cin.ufpe.br)</p>
 */
@Entity("contributor")
public class Contributor extends GitHubEntity {
    @SerializedName("id")
    @Indexed(unique=true, dropDups=true)
    @Id private int id;
   
    @SerializedName("login")
    private String login;
    
    @SerializedName("avatar_url")
    private String avatar_url;
    
    @SerializedName("gravatar_id")
    private String gravatar_id;
    
    @SerializedName("html_url")
    private String html_url;
    
    @SerializedName("followers_url")
    private String followers_url;
    
    @SerializedName("following_url")
    private String following_url;
    
    @SerializedName("gists_url")
    private String gists_url; 
    
    @SerializedName("starred_url")
    private String starred_url;
    
    @SerializedName("subscriptions_url")
    private String subscriptions_url;
    
    @SerializedName("repos_url")
    private String repos_url;
    
    @SerializedName("events_url") 
    private String events_url;
    
    @SerializedName("received_events_url")
    private String received_events_url;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("site_admin")
    private boolean site_admin;
    
    @SerializedName("contributions")
    private int contributions;
    
    
    /**<p>This constructor that doesn't require all fields, but they <b>should</b> be setted after instantiation for a good use like we have described in
	 {@link Contributor} Class annotation <p>*/
    public Contributor(String login) {
    	this.login = login;
    }
    
    /**
     * <p>Informs the GitHub ID for the {@link Contributor} object in question
     * This ID is unique in GitHub, which means no two contributors can have the same ID on GitHub</p>
     * @return the integer ID
     */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 *<p> The String returned by this method informs the {@link Contributor} <i>login</i>. </p> 
	 *@return
	 *<p>Return a String that represent the <i>login</i></p>
	 **/
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return
	 * <p>Return a String that represent the <i>avatar_url</i></p>
	 * */
	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	
	/**
	 * @return
	 * <p>Return a String that represent the <i>gravatar_id</i></p>
	 * */
	public String getGravatar_id() {
		return gravatar_id;
	}

	public void setGravatar_id(String gravatar_id) {
		this.gravatar_id = gravatar_id;
	}
	
	/**
	 * @return
	 * <p>Return a String that represent the <i>html_url</i></p>
	 * */
	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	
	/**
	 * <p>The String returned by this method contains a URL for a list of all {@link User}'s that are
	 * following this {@link Contributor}</p>
	 * @return
	 * <p>Reeturn a String that represent the <i>followers_url</i></p>
	 * */
	public String getFollowers_url() {
		return followers_url;
	}

	public void setFollowers_url(String followers_url) {
		this.followers_url = followers_url;
	}

	/**
	 * <p>The String returned by this method contains a URL for a list of {@link User}'s
	 *  that this {@link Contributor} are following</p>
	 * @return
	 * <p>Reeturn a String that represent the <i>following_url</i></p>
	 * */
	public String getFollowing_url() {
		return following_url;
	}

	public void setFollowing_url(String following_url) {
		this.following_url = following_url;
	}
	
	/**
	 * @return
	 * <p>Return a String that represent the <i>gists_url</i></p>
	 * */
	public String getGists_url() {
		return gists_url;
	}

	public void setGists_url(String gists_url) {
		this.gists_url = gists_url;
	}

	/**
	 * @return
	 * <p>Return a String that represent the <i>starred_url</i></p>
	 * */
	public String getStarred_url() {
		return starred_url;
	}

	public void setStarred_url(String starred_url) {
		this.starred_url = starred_url;
	}
	
	
	/**
	 * @return
	 * <p>Return a String that represent the <i>subscriptions_url</i></p>
	 * */
	public String getSubscriptions_url() {
		return subscriptions_url;
	}

	public void setSubscriptions_url(String subscriptions_url) {
		this.subscriptions_url = subscriptions_url;
	}
	
	/**
	 * @return
	 * <p>Return a String that represent the <i>repos_url</i></p>
	 * */
	public String getRepos_url() {
		return repos_url;
	}

	public void setRepos_url(String repos_url) {
		this.repos_url = repos_url;
	}

	/**
	 * @return
	 * <p>Return a String that represent the <i>events_url</i></p>
	 * */
	public String getEvents_url() {
		return events_url;
	}

	public void setEvents_url(String events_url) {
		this.events_url = events_url;
	}

	/**
	 * @return
	 * <p>Returns a String that represents the <i>receveid_events_url</i> </p>
	 * */
	public String getReceived_events_url() {
		return received_events_url;
	}
	
	
	public void setReceived_events_url(String received_events_url) {
		this.received_events_url = received_events_url;
	}
	
	/**
	 * @return
	 * <p>Return a String that represents the {@link Contributor} type</p>
	 * */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Informs is this {@link Contributor} is the site admin
	 * @return
	 * <p>A boolean that represents if the user is the site admin</p>
	 * */
	public boolean isSite_admin() {
		return site_admin;
	}

	public void setSite_admin(boolean site_admin) {
		this.site_admin = site_admin;
	}

	/**
	 * Informs the <b> number of contributions </b> of this {@link Contributor} in the {@link Project}
	 * @return
	 * <p>A integer that represents the number of contributions</p>
	 * */
	public int getContributions() {
		return contributions;
	}

	public void setContributions(int contributions) {
		this.contributions = contributions;
	}
	
    
    /**
     * <p>Two {@link Contributor} objects X and Y are the same <i>if and only</i> if both X and Y have the <b>same login</b> attribute </p>
     * @param 
     <p>A <i>Contributor</i> object that will be compared with the <i>Contributor</i> object in question: </p>
     * @return
     * 	<p><b>true</b> <i>if and only if</i> they have the same loging.</p>
     *  <p><b>else</b> otherwise.</p> 
     */
    public boolean equals(Contributor contributor) {
    	return this.login.equalsIgnoreCase(contributor.login);
    }
	
	/**<p>A {@link Contributor} does not have a API URL so we redirect to his {@link User} URL defined by his <i>login</i>.</p>
	 * 
	 * @return
	 * <p>A String to the URL that contains all information about the <i>User</i> that represent this <i>Contributor</i></p>
	 * */
	public String getURL() {
		return String.format("https://api.github.com/users/%s", this.getLogin());
	}
 
}