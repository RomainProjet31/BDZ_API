package api;

import twitter4j.TwitterException;

public class Test {

    public static void main(String[] args) {
	try {
	    for (String str : Tweeter4J.getTimeLine()) {
		System.out.println(str);
	    }
	    System.out.println();
	} catch (TwitterException e) {
	    e.printStackTrace();
	}
    }

}
