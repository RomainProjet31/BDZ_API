package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import model.Tweet;

public class Factory {

    private GraphDatabaseFactory graphDbFactory;
    private GraphDatabaseService graphDb;
    private static String pathName = "C:\\Users\\MIAGE UT1\\Desktop\\NEO4JWorkSpace";
    private Transaction transactions;
    private Driver driver;
    private Session session;
    Connection con;

    public Factory() {
	createOrRetrieveFactory();
    }

    public void createOrRetrieveFactory() {
	try {
	    con = DriverManager.getConnection("jdbc:neo4j:bolt://localhost/?user=neo4j,password=111,scheme=basic");
	    clearAll();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /***
     * 
     * @param _user
     * @param _tweet
     */
    public void createLinkBetweenUserTweet(Node _user, Node _tweet) {
	_user.createRelationshipTo(_tweet, RelationshipType.withName("post"));
    }

    /***
     * 
     * @param _reTweet
     * @param _tweet
     */
    public void createLinkBetweenTweets(Node _reTweet, Node _tweet) {
	_reTweet.createRelationshipTo(_tweet, RelationshipType.withName("reTweet"));
    }

    /***
     * Ajoute un nouveau tweet dans le graph et appel le gestionnaire des
     * dependances
     * 
     * @param _twit
     * @throws SQLException
     */
    public void addNewTweet(Tweet _twit) throws SQLException {
	con.createStatement().execute(convertToStringCreation(_twit));
	checkDependencies(_twit);

    }

    private void checkDependencies(Tweet _twit) throws SQLException {
	if (_twit.isRetweet()) {
	    // Verifier que le tweet original soit dans le graph sinon on l'ajoute
	    ResultSet result = con.createStatement()
		    .executeQuery(convertToStringMatchTweet("" + _twit.getOriginalTweet().getIdTweet()));
	    if (!result.next()) {
		con.createStatement().execute(convertToStringCreation(_twit.getOriginalTweet()));
		checkDependencies(_twit.getOriginalTweet());
	    }
	    con.createStatement().execute(convertToStringReTweetRelation(_twit));
	}

	// Verifier que l'auteur du tweet soit dans le graph.
	ResultSet result = con.createStatement().executeQuery(convertToStringMatch(_twit.getAuthor().getIdUser() + ""));
	if (!result.next())
	    addNewUser(_twit.getAuthor());
    }

    /***
     * Ajoute un nouvel utilisateur dans le graph
     * 
     * @param _user
     */
    public void addNewUser(model.User _user) {
	try {
	    con.createStatement().execute(convertToStringCreation(_user));
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /***
     * 
     * @param _id
     * @return
     */
    public Node getTweetById(int _id) {
	Result result = graphDb.execute("MATCH (c:Tweet)" + "WHERE c.idTweet = '" + _id + "'" + "RETURN c");
	return (Node) result.next();
    }

    public void test() {
	ResultSet result = null;
	try {
	    result = con.createStatement().executeQuery("MATCH (c:Tweet) RETURN c");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println(result);
    }

    public GraphDatabaseFactory getGraphDbFactory() {
	return graphDbFactory;
    }

    public void setGraphDbFactory(GraphDatabaseFactory graphDbFactory) {
	this.graphDbFactory = graphDbFactory;
    }

    public GraphDatabaseService getGraphDb() {
	return graphDb;
    }

    public void setGraphDb(GraphDatabaseService graphDb) {
	this.graphDb = graphDb;
    }

    public static String getPathName() {
	return pathName;
    }

    public static void setPathName(String pathName) {
	Factory.pathName = pathName;
    }

    public Transaction getTransactions() {
	return transactions;
    }

    public void setTransactions(Transaction transactions) {
	this.transactions = transactions;
    }

    // Conver OBJETS INTO CYPHER STATEMENT STRING

    // USERS

    private String convertToStringCreation(model.User _user) {
	String cypherStatement = "Create (n:User{" + "idUser :\"" + _user.getIdUser() + "\"," + "screenName :\""
		+ _user.getScreenName() + "\"," + "name :\"" + _user.getName() + "\"," + "location :\""
		+ _user.getLocation() + "\"," + "description :\"" + _user.getDescription() + "\"," + "createdAt :\""
		+ _user.getCreatedAt() + "\"," + "favCount :\"" + _user.getFavCount() + "\"," + "friendCount :\""
		+ _user.getFriendCount() + "\"," + "timeZone :\"" + _user.getTimeZone() + "\"," + "lang :\""
		+ _user.getLang() + "\"," + "verified :\"" + _user.isVerified() + "\"," + "followersCount :\""
		+ _user.getFollowersCount() + "\"})";
	return cypherStatement;
    }

    private String convertToStringMatch(String _userId) {
	return "Match(u:User{idUser:'" + _userId + "'}) return u";
    }

    // TWEETS

    private String convertToStringCreation(Tweet _twit) {
	String content = _twit.getText().replaceAll("\"", "'");
	String cypherStatement = "CREATE (n:Tweet { " + "idTweet: \"" + _twit.getIdTweet() + "\"," + "text: \""
		+ content + "\"," + "createdAt: \"" + _twit.getCreatedAt() + "\"," + "inReplyToUserId: \""
		+ _twit.getInReplyToUserId() + "\"," + "inReplyToScreenName: \"" + _twit.getInReplyToScreenName()
		+ "\"," + "inReplyToStatusId: \"" + _twit.getInReplyToStatusId() + "\"," + "isRetweet: \""
		+ _twit.isRetweet() + "\"," + "isRetweeted: \"" + _twit.isRetweeted() + "\"," + "truncated: \""
		+ _twit.isTrucated() + "\"," + "author: \"" + _twit.getAuthor().getIdUser() + "\" })";
	return cypherStatement;
    }

    private String convertToStringReTweetRelation(Tweet _twit) {
	return "MATCH (t1:Tweet),(t2:Tweet)\r\n" + "WHERE t1.idTweet = '" + _twit.getIdTweet() + "' AND t2.idTweet = '"
		+ _twit.getOriginalTweet().getIdTweet() + "'\r\n" + "MERGE (t1)-[r:reTweeter]->(t2)";
    }

    private String convertToStringMatchTweet(String _idTwit) {
	return "Match(t:Tweet{idTweet : " + _idTwit + "}) return t";
    }

    // RESET THE GRAPH

    private void clearAll() {
	try {
	    con.createStatement().execute("Match(n:Tweet) DETACH DELETE n");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
