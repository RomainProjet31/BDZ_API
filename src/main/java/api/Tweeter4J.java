package api;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import model.Tweet;
import store.Store;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public final class Tweeter4J {
    private static String key = "ejRwCA2s2aNOj58SBr7XJfSiV";
    private static String consumer = "KrBaK0cqduSHyssCaJBBuTlowksSCAAb0wsiRTJGrMJOHbnKNF";
    private static String token = "1262389953859850242-TSTLXYkBAVlm022znkEY3nYMHNUWOf";
    private static String tokenSecret = "ccj69LiAOFx3NLZabwv9rPY1mhRRF9J3LgvCaAecsnUoO";
    private static Tweeter4J instance;
    private Twitter twitter;

    /*** SINGLETON ***/
    public static Tweeter4J getInstance() {
	if (instance == null) {
	    instance = new Tweeter4J();
	}
	return instance;
    }

    public static Twitter getTwitterInstance() {
	return getInstance().getTwitter();
    }

    /*** CONSTRUCTOR ***/

    public Tweeter4J() {
	instance = this;
	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setDebugEnabled(true).setOAuthConsumerKey(key).setOAuthConsumerSecret(consumer).setOAuthAccessToken(token)
		.setOAuthAccessTokenSecret(tokenSecret);
	TwitterFactory tf = new TwitterFactory(cb.build());
	instance.twitter = tf.getInstance();
    }

    /*** METHODS ***/

    public static List<String> getTimeLine() throws TwitterException {
	Twitter twitter = getTwitterInstance();
	return twitter.getHomeTimeline().stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public static void loadUsers() throws TwitterException {
	Twitter twitter = getTwitterInstance();
	HashSet<User> tempUsers = twitter.getHomeTimeline().stream().map(item -> item.getUser())
		.collect(Collectors.toCollection(HashSet::new));
	for (User u : tempUsers) {
	    model.User user = new model.User(u);
	    Store.listOfUsers.add(user);
	}
    }

    public static void loadTweetsFromUser(User user) throws TwitterException {
	Twitter twitter = getTwitterInstance();
	HashSet<Status> tempStatus = twitter.getHomeTimeline().stream().filter(item -> item.getUser().equals(user))
		.map(item -> item).collect(Collectors.toCollection(HashSet::new));
	for (Status status : tempStatus) {
	    // reprendre là
	    model.Tweet tweet = new Tweet(status);
	    Store.listOfTweets.add(tweet);
	}
    }

    /*** GETTERS AND SETTERS ***/

    public Twitter getTwitter() {
	return twitter;
    }

    public void setTwitter(Twitter twitter) {
	this.twitter = twitter;
    }

    public static void setInstance(Tweeter4J instance) {
	Tweeter4J.instance = instance;
    }

}
