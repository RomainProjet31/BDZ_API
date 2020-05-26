package api;

import model.Tweet;
import store.Store;
import twitter4j.TwitterException;

public class Test {

    public static void main(String[] args) {
	try {
	    Tweeter4J.loadUsers();
	    for (model.User user : Store.listOfUsers)
		Tweeter4J.loadTweetsFromUser(user);
	    for (Tweet tweet : Store.listOfTweets)
		System.out.println(tweet);
	    Tweeter4J.connect();
	} catch (TwitterException e) {
	    e.printStackTrace();
	}
    }

}
