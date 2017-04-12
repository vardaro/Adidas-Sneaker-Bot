import twitter4j.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by justa on 4/11/2017.
 */
public class Main {
  private static String adidasalerts = "C:\\Users\\justa\\IdeaProjects\\SneakerBot\\Tweets\\adidasalerts.txt";

  private static long lastIDUsed;
  public static void main(String[] args) {
    Twitter twitter = getTwitter();
    try {
      User user = twitter.showUser("adidasalerts");
      Status latestTweet = user.getStatus();
      printTweetInfo(latestTweet,user);
      recordTweet(latestTweet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Stores Twitter API access keys into Twitter object
   *
   * @return Twitter object containing API keys
   */
  private static Twitter getTwitter() {
    System.out.println("Getting Twitter properties...");
    Twitter twitter = new TwitterFactory().getSingleton();
    System.out.println("Twitter received");
    return twitter;
  }

  /**
   * Takes a status and prints relevant info about the tweet
   * to the console for users to see.
   *
   * @param s Tweet found
   * @param u User who posted tweet
   */
  private static void printTweetInfo(Status s, User u) {
    System.out.println("\nNew @" + u.getScreenName() + " tweet found!");
    System.out.println("Date Tweeted: " + s.getCreatedAt());
    System.out.println("Tweet ID: " + s.getId());
    System.out.println("Body: {\n" + s.getText() + "\n}");

  }

  /**
   * Listens for new tweets from specified twitter user
   *
   * If the user tweets a restock or release of new shoe,
   * it will travel to the provided link in the tweet to
   * purchase the shoe
   *
   * TODO: Finish tweet listener and add support for shoe value logic
   *
   * @param username Twitter users @
   * @param twitter Twitter object
   */
  private static void findNewTweet(String username, Twitter twitter) {
    try {
      User user = twitter.showUser(username);
      Status recentTweet = user.getStatus();

      // Declaring Status listener
      StatusListener listener = new StatusListener() {
        //Declaring each method

        public void onStatus(Status status) {
          //do your action here
        }
        public void onDeletionNotice(StatusDeletionNotice sdn){}
        public void onTrackLimitationNotice(int numberOfLimitedStatuses){}
        public void onException(Exception e) {
          e.printStackTrace();
        }
        public void onScrubGeo(long userID,long upToStatusID) {}

      };
/*
      TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
      twitterStream.addListener(listener);

      FilterQuery query = new FilterQuery();
      query.follow(user.getId());
      twitterStream.filter(query);*/
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Records tweet info into designated .txt file
   * of the corresponding twitter account.
   *
   * This prevents a tweet from being found twice and
   * accidentally purchasing the same shoe twice by
   * reassigning the value of lastIDUsed long to the tweets ID.
   *
   * This method will only be called after a purchase has been made.
   *
   * @param s Latest status from specified twitter account
   */
  private static void recordTweet(Status s) {
    // Try to create PrintWriter object
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(adidasalerts,true)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Write info of the tweet to the file
    out.println("tweedID=" + s.getId());
    out.println("tweetDate=" + s.getCreatedAt());
    out.println("Body={\n"+s.getText()+"\n}");
    out.println();
    lastIDUsed = s.getId();
    out.close();
  }
}
