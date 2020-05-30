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
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import model.Tweet;

public class Factory {

    private GraphDatabaseFactory graphDbFactory;
    private GraphDatabaseService graphDb;
    private static String pathName = "C:\\Users\\MIAGE UT1\\Desktop\\NEO4JWorkSpace";
    private Transaction transactions;
    @SuppressWarnings("unused")
    private Driver driver;
    @SuppressWarnings("unused")
    private Session session;
    Connection con;

    public Factory() {
	createOrRetrieveFactory();
    }

    public void createOrRetrieveFactory() {
	try {
	    con = DriverManager.getConnection("jdbc:neo4j:bolt://localhost/?user=neo4j,password=111,scheme=basic");
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
	ResultSet resultat = getTweetById(_twit.getIdTweet());

	if (!resultat.next()) { // S'il n'est pas dans la bd pour éviter les doublons
	    con.createStatement().execute(convertToStringCreation(_twit));
	    checkDependencies(_twit);
	} else {
	    System.out.println("\n IL N'Y EST PAS \n");
	}
    }

    private void checkDependencies(Tweet _twit) throws SQLException {

	// Verifier que l'auteur du tweet soit dans le graph.
	ResultSet result = con.createStatement().executeQuery(convertToStringMatch(_twit.getAuthor().getIdUser() + ""));
	if (!result.next())
	    addNewUser(_twit.getAuthor());
	con.createStatement().execute(convertToStringRelationPost(_twit.getAuthor(), _twit));

	if (_twit.isRetweet()) {
	    // Verifier que le tweet original soit dans le graph sinon on l'ajoute
	    result = con.createStatement()
		    .executeQuery(convertToStringMatchTweet("" + _twit.getOriginalTweet().getIdTweet()));
	    if (!result.next()) {
		con.createStatement().execute(convertToStringCreation(_twit.getOriginalTweet()));
		checkDependencies(_twit.getOriginalTweet());
	    }
	    con.createStatement().execute(convertToStringReTweetRelation(_twit));
	}
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
    public ResultSet getTweetById(int _id) {
	ResultSet result = null;
	try {
	    result = con.createStatement()
		    .executeQuery("MATCH (c:Tweet)" + "WHERE c.idTweet = '" + _id + "'" + "RETURN c");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return result;
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

    public ResultSet getTweet(Tweet _twit) {
	ResultSet resultat = null;
	try {
	    resultat = con.createStatement().executeQuery(convertToStringMatchTweet(_twit.getIdTweet() + ""));
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return resultat;
    }

    public ResultSet getUser(model.User _user) {
	ResultSet resultat = null;
	try {
	    resultat = con.createStatement().executeQuery(convertToStringMatch(_user.getIdUser() + ""));
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return resultat;
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

    private String convertToStringRelationPost(model.User _user, model.Tweet _twit) {
	String inReply = "";
	if (_twit.getInReplyToScreenName() != null)
	    inReply = _twit.getInReplyToScreenName();
	String cypherStatement = "Match(u:User),(t:Tweet{idTweet:'" + _twit.getIdTweet() + "'})" + " WHERE u.idUser = '"
		+ _user.getIdUser() + "' OR t.inReplyToScreenName = '" + inReply + "'" + " MERGE (u)-[r:Tweeter]->(t) ";
	return cypherStatement;
    }

    private String convertToStringCreation(model.User _user) {
	String description = "";
	if (_user.getDescription() != null)
	    description = _user.getDescription().toLowerCase().trim();
	description = description.replaceAll("\"", "'");
	String cypherStatement = "Create (n:User{" + "idUser :\"" + _user.getIdUser() + "\"," + "screenName :\""
		+ _user.getScreenName() + "\"," + "name :\"" + _user.getName() + "\"," + "location :\""
		+ _user.getLocation() + "\"," + "description :\"" + description + "\"," + "createdAt :\""
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

    public void clearAll() {
	try {
	    con.createStatement().execute("Match(n) DETACH DELETE n");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
