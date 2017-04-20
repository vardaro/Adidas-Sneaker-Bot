import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by justa on 4/18/2017.
 *
 * This class simple reads in the user properties of a user
 */
public class PropertiesReader {
  Properties properties = new Properties();
  String propertiesPath;

  public PropertiesReader(String propertiesPath) {
    this.propertiesPath = propertiesPath;
  }

  /**
   * Returns the value of the requested key from the properties path
   *
   * @param key key of value requested
   * @return value of key
   */
  public String get(String key) {
    try {
      InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesPath);
      properties.load(input);
      return properties.getProperty(key);
    } catch (IOException io) {
      System.out.println("Error getting property " + key + "\nException: " + io);
      return null;
    }
  }
}
