package br.ufpe.cin.groundhog;

import java.util.Date;
import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.google.gson.annotations.SerializedName;

import org.mongodb.morphia.annotations.Reference;

/**
 * Represents a GitHub User in Groundhog
 * @author gustavopinto, Rodrigo Alves
 */
@Entity("users")
public class User extends GitHubEntity {
    @SerializedName("id")
    @Indexed(unique=true, dropDups=true)
    @Id private int id;
  
    @SerializedName("name")
    private String name;
  
    @SerializedName("login")
    private String login;
  
    @SerializedName("email")
    private String email;
  
    @SerializedName("company")
    private String company;
  
    @SerializedName("location")
    private String location;
  
    @SerializedName("blog")
    private String blog;
  
    @SerializedName("hireable")
    private boolean hireable;
  
    @SerializedName("followers")
    private int followers;
  
    @SerializedName("following")
    private int following;
  
    @SerializedName("public_repos")
    public int public_repos;
  
    @SerializedName("public_gists")
    public int public_gists;
  
    @SerializedName("created_at")
    private String created_at;
  
    @SerializedName("updated_at")
    private String updated_at;
    
	@Reference private List<Commit> commits;
    private List<String> emailAddresses;
  
    public User(String login) {
    	this.login = login;
    }
  
    public User(String login, String name) {
        this(login);
        this.name = name;
    }
  
    /**
     * Informs the GitHub ID for the {@link User} object in question
     * This ID is unique in GitHub, which means no two users can have the same ID on GitHub
     * @return the integer ID
     */
    public int getId() {
        return this.id;
    }
  
    public void setId(int id) {
        this.id = id;
    }
  
    /**
     * Informs the User name. Useful for measuring commit authorship
     * @return a {@link String} object
     */
    public String getName() {
        return this.name;
    }
  
    /**
     * Sets the name of the User
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
  
    /**
     * Informs the login (username) of the {@link User}
     * @return
     */
    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

   /**
    * Informs the {@link User}'s company name
    * @return
    */
    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

   /**
    * Informs the {@link User}'s blog URL
    * @return
    */
    public String getBlog() {
        return this.blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

   /**
    * Informs whether a {@link User} is available for hire (hireable) or not.
    * @return true if the {@link User} is hireable. Returns false otherwise.
    */
    public boolean isHireable() {
        return this.hireable;
    }

    public void setHireable(boolean hireable) {
        this.hireable = hireable;
    }

   /**
    * Informs the sum of followers the User has
    * @return
    */
    public int getFollowers() {
        return this.followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

   /**
    * Informs how many other GitHub Users the User follows
    * @return
    */
    public int getFollowing() {
        return this.following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

   /**
    * Informs the date when the {@link User} signed up for GitHub
    * @return a {@link String} object
    */
    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

   /**
    * Informs the last date when the {@link User} performed activity on GitHub
    * @return a {@link Date} object
    */
    public String getUpdatedAt() {
        return this.updated_at;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }
  
   /**
    * Returns the sum of public repositories that the User has
    * @return
    */
    public int getPublic_repos() {
        return this.public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }
  
   /**
    * Returns the sum of public gists that the User has
    * @return
    */
    public int getPublic_gists() {
        return this.public_gists;
    }

    public void setPublic_gists(int public_gists) {
        this.public_gists = public_gists;
    }

   /**
    * Informs the email address of the {@link User}
    * @return a String representing an email address
    */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   /**
    * The location String of the {@link User}
    * @return
    */
    public String getLocation() {
        return this.location;
    }

   public void setLocation(String location) {
       this.location = location;
   }
   
   public List<Commit> getCommits() {
		return this.commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}
  
   /**
    * Informs the list of email addresses that belongs to the user.
    * This is useful for matching critical {@link Commit} authorship data
    * @return
    */
    public List<String> getEmailAddresses() {
        return this.emailAddresses;
    }
  
    public void setEmailAddresses(List<String> emails) {
        this.emailAddresses = emails;
    }
  
    public String getURL() {
        return String.format("https://api.github.com/users/%s", this.getLogin());
    }
    
    /**
     * Two {@link User} objects X and Y are the same if both X and Y have the same login attribute
     * @param user
     * @return
     */
    public boolean equals(User user) {
    	return this.login == user.login;
    }

    @Override
    public String toString() {
        return "id=" + id + ", "
            + (name != null ? "Name = " + name + ", " : "")
            + (login != null ? "Login = " + login + ", " : "")
            + "Public repos = " + public_repos + ", "
            + (created_at != null ? "Created at = " + created_at : "") + ", Url: " + this.getURL();
    }
}