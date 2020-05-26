package model;

import java.util.HashSet;

public class User {

    private int idUser;
    private String screenName;
    private String name;
    private String location;
    private String description;
    private String createdAt;
    private int favCount;
    private int friendCount;
    private String timeZone;
    private String lang;
    private boolean verified;
    private int followersCount;
    private Place place;
    private HashSet<Tweet> tweets;

    public User(int idUser, String screenName, String name, String location, String description, String createdAt,
	    int favCount, int friendCount, String timeZone, String lang, boolean verified, int followersCount,
	    Place place) {
	super();
	this.idUser = idUser;
	this.screenName = screenName;
	this.name = name;
	this.location = location;
	this.description = description;
	this.createdAt = createdAt;
	this.favCount = favCount;
	this.friendCount = friendCount;
	this.timeZone = timeZone;
	this.lang = lang;
	this.verified = verified;
	this.followersCount = followersCount;
	this.place = place;
	this.tweets = new HashSet<>();
    }

    public User(twitter4j.User u) {
	this((int) u.getId(), u.getScreenName(), u.getName(), u.getLocation(), u.getDescription(),
		u.getCreatedAt().toString(), u.getFavouritesCount(), u.getFriendsCount(), u.getTimeZone(), u.getLang(),
		u.isVerified(), u.getFollowersCount(), null);
    }

    public int getIdUser() {
	return idUser;
    }

    public void setIdUser(int idUser) {
	this.idUser = idUser;
    }

    public String getScreenName() {
	return screenName;
    }

    public void setScreenName(String screenName) {
	this.screenName = screenName;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
    }

    public int getFavCount() {
	return favCount;
    }

    public void setFavCount(int favCount) {
	this.favCount = favCount;
    }

    public int getFriendCount() {
	return friendCount;
    }

    public void setFriendCount(int friendCount) {
	this.friendCount = friendCount;
    }

    public String getTimeZone() {
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    public String getLang() {
	return lang;
    }

    public void setLang(String lang) {
	this.lang = lang;
    }

    public boolean isVerified() {
	return verified;
    }

    public void setVerified(boolean verified) {
	this.verified = verified;
    }

    public int getFollowersCount() {
	return followersCount;
    }

    public void setFollowersCount(int followersCount) {
	this.followersCount = followersCount;
    }

    public Place getPlace() {
	return place;
    }

    public void setPlace(Place place) {
	this.place = place;
    }

    public HashSet<Tweet> getTweets() {
	return tweets;
    }

    public void setTweets(HashSet<Tweet> tweets) {
	this.tweets = tweets;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + idUser;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	User other = (User) obj;
	if (idUser != other.idUser)
	    return false;
	return true;
    }

}
