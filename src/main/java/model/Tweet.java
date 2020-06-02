package model;

import java.util.ArrayList;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Status;

public class Tweet {

    private int idTweet;
    private String text;
    private String createdAt;
    private String inReplyToUserId;
    private String inReplyToScreenName;
    private String inReplyToStatusId;
    private boolean isRetweet;
    private boolean isRetweeted;
    private boolean truncated;
    public int replyCount;
    public List<String> hashtags;
    public int favCount;
    public int retweetCount;
    /***
     * Si cet attribut n'est pas nul, alors il s'agit d'un reTweet
     */
    private Tweet originalTweet;
    private User author;

    /***
     * Constructeur d'un reTweet
     * 
     * @param idTweet
     * @param text
     * @param createdAt
     * @param inReplyToUserId
     * @param inReplyToScreenName
     * @param inReplyToStatusId
     * @param trucated
     * @param tweet
     * @param user
     */
    public Tweet(int idTweet, String text, String createdAt, String inReplyToUserId, String inReplyToScreenName,
	    String inReplyToStatusId, boolean trucated, User user, boolean isRetweet, Status status,
	    boolean isRetweeted, int replyCount, int favCount, HashtagEntity[] hashtags, int countRetweet) {
	this(idTweet, text, createdAt, inReplyToUserId, inReplyToScreenName, inReplyToStatusId, trucated, user);
	this.isRetweet = isRetweet;
	this.isRetweeted = isRetweeted;
	this.replyCount = replyCount;
	this.favCount = favCount;
	if (isRetweet) {
	    Tweet originalTweet = new Tweet(status);
	    this.originalTweet = originalTweet;
	}
	if (this.inReplyToScreenName == null) {
	    this.inReplyToScreenName = "";
	    this.inReplyToStatusId = "";
	    this.inReplyToUserId = "";
	}
	this.hashtags = new ArrayList<>();
	for (HashtagEntity hash : hashtags) {
	    this.hashtags.add(hash.getText());
	}
	this.retweetCount = countRetweet;
    }

    public Tweet(twitter4j.Status status) {
	this((int) status.getId(), status.getText(), status.getCreatedAt().toString(), status.getInReplyToUserId() + "",
		status.getInReplyToScreenName(), "" + status.getInReplyToStatusId(), status.isTruncated(),
		new model.User(status.getUser()), status.isRetweet(), status.getRetweetedStatus(), status.isRetweeted(),
		status.getRetweetCount(), status.getFavoriteCount(), status.getHashtagEntities(),
		status.getRetweetCount());
    }

    /***
     * Constructeur d'un tweet
     * 
     * @param idTweet
     * @param text
     * @param createdAt
     * @param inReplyToUserId
     * @param inReplyToScreenName
     * @param inReplyToStatusId
     * @param trucated
     * @param user
     */
    public Tweet(int idTweet, String text, String createdAt, String inReplyToUserId, String inReplyToScreenName,
	    String inReplyToStatusId, boolean trucated, User user) {
	super();
	createdAt = createdAt.substring(3, createdAt.indexOf(":")) + "h";

	this.idTweet = idTweet;
	this.text = text;
	this.createdAt = createdAt;
	this.inReplyToUserId = inReplyToUserId;
	this.inReplyToScreenName = inReplyToScreenName;
	this.inReplyToStatusId = inReplyToStatusId;
	this.truncated = trucated;
	this.originalTweet = null;
	this.author = user;
    }

    /*** GETTERS AND SETTERS ***/

    public boolean isRetweet() {
	return isRetweet;
    }

    public void setRetweet(boolean isRetweet) {
	this.isRetweet = isRetweet;
    }

    public boolean isRetweeted() {
	return isRetweeted;
    }

    public void setRetweeted(boolean isRetweeted) {
	this.isRetweeted = isRetweeted;
    }

    public boolean isTruncated() {
	return truncated;
    }

    public void setTruncated(boolean truncated) {
	this.truncated = truncated;
    }

    public Tweet getOriginalTweet() {
	return originalTweet;
    }

    public void setOriginalTweet(Tweet originalTweet) {
	this.originalTweet = originalTweet;
    }

    public User getAuthor() {
	return author;
    }

    public void setAuthor(User author) {
	this.author = author;
    }

    public int getIdTweet() {
	return idTweet;
    }

    public void setIdTweet(int idTweet) {
	this.idTweet = idTweet;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
    }

    public String getInReplyToUserId() {
	return inReplyToUserId;
    }

    public void setInReplyToUserId(String inReplyToUserId) {
	this.inReplyToUserId = inReplyToUserId;
    }

    public String getInReplyToScreenName() {
	return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
	this.inReplyToScreenName = inReplyToScreenName;
    }

    public String getInReplyToStatusId() {
	return inReplyToStatusId;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
	this.inReplyToStatusId = inReplyToStatusId;
    }

    public boolean isTrucated() {
	return truncated;
    }

    public void setTrucated(boolean trucated) {
	this.truncated = trucated;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + idTweet;
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
	Tweet other = (Tweet) obj;
	if (idTweet != other.idTweet)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Tweet [idTweet=" + idTweet + ", text=" + text + ", createdAt=" + createdAt + ", inReplyToUserId="
		+ inReplyToUserId + ", inReplyToScreenName=" + inReplyToScreenName + ", inReplyToStatusId="
		+ inReplyToStatusId + ", isRetweet=" + isRetweet + ", isRetweeted=" + isRetweeted + ", truncated="
		+ truncated + ", OriginalTweet=" + originalTweet + ", author=" + author + "]";
    }

}
