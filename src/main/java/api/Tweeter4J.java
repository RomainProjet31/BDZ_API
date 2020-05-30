package api;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import factory.Factory;
import model.Tweet;
import store.Store;
import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public final class Tweeter4J {
    private static String key = "ejRwCA2s2aNOj58SBr7XJfSiV";
    private static String consumer = "KrBaK0cqduSHyssCaJBBuTlowksSCAAb0wsiRTJGrMJOHbnKNF";
    private static String token = "1262389953859850242-TSTLXYkBAVlm022znkEY3nYMHNUWOf";
    private static String tokenSecret = "ccj69LiAOFx3NLZabwv9rPY1mhRRF9J3LgvCaAecsnUoO";
    private static Tweeter4J instance;
    private Twitter twitter;
    private TwitterStream twitterStream;
    private Factory factory;

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
	Configuration conf = cb.build();
	TwitterFactory tf = new TwitterFactory(conf);
	instance.twitter = tf.getInstance();
	instance.factory = new Factory();
	instance.twitterStream = null;

    }

    /*** METHODS ***/

    public static void connect() {
	Configuration conf = instance.twitter.getConfiguration();
	instance.twitterStream = new TwitterStreamFactory(conf).getInstance();
	StatusListener listener = new StatusListener() {
	    @Override
	    public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
		model.Tweet twit = new Tweet(status);
		try {
		    instance.factory.addNewTweet(twit);
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	    @Override
	    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	    }

	    @Override
	    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
	    }

	    @Override
	    public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	    }

	    @Override
	    public void onException(Exception ex) {
		ex.printStackTrace();
	    }

	    @Override
	    public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
	    }
	};

	FilterQuery filtre = new FilterQuery();
	String[] keywordsArray = { " RTL ", "RTL France", "RTLFrance", "#RTL", "#RTLFrance", "#rtlfrance", "#rtl" };
	filtre.track(keywordsArray);
	filtre.language("fr");
	instance.twitterStream.addListener(listener);
	instance.twitterStream.filter(filtre);
    }

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

    public static void loadTweetsFromUser(model.User user) throws TwitterException {
	Twitter twitter = getTwitterInstance();
	HashSet<Status> tempStatus = twitter.getHomeTimeline().stream()
		.filter(item -> (item.getUser().getId() + "").equals(user.getIdUser() + "")).map(item -> item)
		.collect(Collectors.toCollection(HashSet::new));
	for (Status status : tempStatus) {
	    // reprendre là
	    model.Tweet tweet = new Tweet(status);
	    user.getTweets().add(tweet);
	    Store.listOfTweets.add(tweet);
	}
    }

    public static void insertTweetsFromStore() {
	for (Tweet tweet : Store.listOfTweets) {
	    try {
		instance.factory.addNewTweet(tweet);
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	System.out.println("\n NOMBRE DE TWEETS : " + Store.listOfTweets.size() + " \n");
	instance.factory.test();

    }

    public static QueryResult query(Query query) throws TwitterException {
	Twitter twitter = getTwitterInstance();
	QueryResult result = twitter.search(query);
	return result;
    }

    /*** GETTERS AND SETTERS ***/

    public Twitter getTwitter() {
	return twitter;
    }

    public TwitterStream getTwitterStream() {
	return twitterStream;
    }

    public void setTwitterStream(TwitterStream twitterStream) {
	this.twitterStream = twitterStream;
    }

    public void setTwitter(Twitter twitter) {
	this.twitter = twitter;
    }

    public static void setInstance(Tweeter4J instance) {
	Tweeter4J.instance = instance;
    }

    public static String getKey() {
	return key;
    }

    public static void setKey(String key) {
	Tweeter4J.key = key;
    }

    public static String getConsumer() {
	return consumer;
    }

    public static void setConsumer(String consumer) {
	Tweeter4J.consumer = consumer;
    }

    public static String getToken() {
	return token;
    }

    public static void setToken(String token) {
	Tweeter4J.token = token;
    }

    public static String getTokenSecret() {
	return tokenSecret;
    }

    public static void setTokenSecret(String tokenSecret) {
	Tweeter4J.tokenSecret = tokenSecret;
    }

    public Factory getFactory() {
	return factory;
    }

    public void setFactory(Factory factory) {
	this.factory = factory;
    }

}
