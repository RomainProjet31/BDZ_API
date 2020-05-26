package model;

public class Tweet {

    private int idTweet;
    private String text;
    private String createdAt;
    private String inReplyToUserId;
    private String inReplyToScreenName;
    private String inReplyToStatusId;
    private boolean truncated;
    /***
     * Si cet attribut n'est pas nul, alors il s'agit d'un reTweet
     */
    private Tweet OriginalTweet;
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
	    String inReplyToStatusId, boolean trucated, Tweet tweet, User user) {
	this(idTweet, text, createdAt, inReplyToUserId, inReplyToScreenName, inReplyToStatusId, trucated, user);
	this.OriginalTweet = tweet;
    }

    public Tweet(twitter4j.Status status) {
	this((int) status.getId(), status.getText(), status.getCreatedAt().toString(), status.getInReplyToUserId() + "",
		status.getInReplyToScreenName(), "" + status.getInReplyToStatusId(), status.isTruncated(),
		new model.User(status.getUser()));
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
	this.idTweet = idTweet;
	this.text = text;
	this.createdAt = createdAt;
	this.inReplyToUserId = inReplyToUserId;
	this.inReplyToScreenName = inReplyToScreenName;
	this.inReplyToStatusId = inReplyToStatusId;
	this.truncated = trucated;
	this.OriginalTweet = null;
	this.author = user;
    }

    public boolean isTruncated() {
	return truncated;
    }

    public void setTruncated(boolean truncated) {
	this.truncated = truncated;
    }

    public Tweet getOriginalTweet() {
	return OriginalTweet;
    }

    public void setOriginalTweet(Tweet originalTweet) {
	OriginalTweet = originalTweet;
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
}