import twitter4j.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by justa on 4/20/2017.
 *
 * With the users consent, this class will listen to tweets from
 * specified twitter accounts for restock and release announcement
 */
public class TweetListener {
  private static final String adidasalerts = "Tweets/adidasalerts.txt";
  PropertiesReader user = new PropertiesReader("user.properties");

  Boolean checkoutPermitted = Boolean.parseBoolean(user.get("CheckoutPermitted"));
  String[] accounts = user.get("TwitterAccounts").split(",");

  /**
   * Returns a twitter object with the user info
   *
   * @return Twitter object containing API keys
   */
  public static Twitter getTwitter() {
    return new TwitterFactory().getSingleton();
  }

  /**
   * Takes a status and prints relevant info about the tweet
   * to the console for users to see.
   *
   * @param s Tweet found
   */
  private static void printTweetInfo(Status s) {
    System.out.println("Date Tweeted: " + s.getCreatedAt());
    System.out.println("Tweet ID: " + s.getId());
    System.out.println("Body: {\n" + s.getText() + "\n}");
  }

  /**
   * Records tweet info into designated .txt file
   * of the corresponding twitter account.
   * <p>
   * This prevents a tweet from being found twice and
   * accidentally purchasing the same shoe twice by
   * reassigning the value of lastIDUsed long to the tweets ID.
   * <p>
   * This method will only be called after a purchase has been made.
   *
   * @param s Latest status from specified twitter account
   */
  private static void recordTweet(Status s) {
    // Try to create PrintWriter object
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(adidasalerts, true)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Write info of the tweet to the file
    out.println("tweedID=" + s.getId());
    out.println("tweetDate=" + s.getCreatedAt());
    out.println("Body={\n" + s.getText() + "\n}");
    out.println();
    out.close();
  }

  /**
   * Listens for new tweets from specified twitter user
   * <p>
   * If the user tweets a restock or release of new shoe,
   * it will travel to the provided link in the tweet to
   * purchase the shoe
   * <p>
   * TODO: Finish tweet listener and add support for shoe value logic
   *
   * @param username          Twitter users @
   * @param checkoutPermitted if true, will purchase the shoe, otherwise it just prints the tweet and moves on
   */
  public static void StartListening(final String username, final boolean checkoutPermitted) {
    try {
      Twitter twitter = getTwitter();
      User user = twitter.showUser(username);
      // Declaring TwitterStream object
      TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

      final StatusListener listener = new StatusListener() {
        // defining each status
        public void onStatus(Status status) {
          // Immediately filter out retweets and replies
          if (status.getUser().getScreenName().equals(username)) {
            // Print newly discovered tweet
            System.out.println("Here is a new tweet");
            printTweetInfo(status);

            if (checkoutPermitted) {
              String link = extractLink(status);
              Purchaser.buyShoe(link);
              recordTweet(status);

            }
          }
        }

        public void onDeletionNotice(StatusDeletionNotice sdn) {
        }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        }

        public void onException(Exception e) {
          e.printStackTrace();
        }

        public void onScrubGeo(long userID, long upToStatusID) {
        }

        public void onStallWarning(StallWarning warning) {
        }
      };

      // Add listener to TwitterStream object
      // TwitterStream only streams tweets from specified user
      twitterStream.addListener(listener);
      FilterQuery query = new FilterQuery(user.getId());
      twitterStream.filter(query);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Restcok and release announcement always include a link and
   * this method returns the link to the shoe
   *
   * @param s Status
   * @return link
   */
  public static String extractLink(Status s) {
    return s.getURLEntities()[0].getURL();
  }

}
