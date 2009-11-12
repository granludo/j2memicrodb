package edu.upc.BDAccessRemot;

public class URLEncoder {
	  /**
	   * This method translates the passed in string into x-www-form-urlencoded
	   * format using the character encoding to hex-encode the unsafe characters.
	   *
	   * @param s The String to convert
	   * @param encoding The encoding to use for unsafe characters
	   *
	   * @return The converted String
	   *
	   * @exception UnsupportedEncodingException If the named encoding is not
	   * supported
	   *
	   * @since 1.4
	   */
	  public static String encode(String s, String encoding) throws Exception
	  {
	    int length = s.length();
	    int start = 0;
	    int i = 0;

	    StringBuffer result = new StringBuffer(length);
	    while (true)
	      {
	    while (i < length && isSafe(s.charAt(i)))
	      i++;

	    // Safe character can just be added
	    result.append(s.substring(start, i));

	    // Are we done?
	    if (i >= length)
	      return result.toString();
	    else if (s.charAt(i) == ' ')
	      {
	        result.append('+'); // Replace space char with plus symbol.
	        i++;
	      }
	    else
	      {
	        // Get all unsafe characters
	        start = i;
	        char c;
	        while (i < length && (c = s.charAt(i)) != ' ' && ! isSafe(c))
	          i++;

	        // Convert them to %XY encoded strings
	        String unsafe = s.substring(start, i);
	        byte[] bytes = unsafe.getBytes(encoding);
	        for (int j = 0; j < bytes.length; j++)
	          {
	        result.append('%');
	        int val = bytes[j];
	        result.append(hex.charAt((val & 0xf0) >> 4));
	        result.append(hex.charAt(val & 0x0f));
	          }
	      }
	    start = i;
	      }
	  }

	  /**
	   * Private static method that returns true if the given char is either
	   * a uppercase or lowercase letter from 'a' till 'z', or a digit froim
	   * '0' till '9', or one of the characters '-', '_', '.' or '*'. Such
	   * 'safe' character don't have to be url encoded.
	   */
	  private static boolean isSafe(char c)
	  {
	    return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
	           || (c >= '0' && c <= '9') || c == '-' || c == '_' || c == '.'
	           || c == '*' || c == '=' || c == '&' );
	  }

	  /**
	   * Private constructor that does nothing. Included to avoid a default
	   * public constructor being created by the compiler.
	   */
	  private URLEncoder()
	  {
	  }

	  /**
	   * Used to convert to hex.  We don't use Integer.toHexString, since
	   * it converts to lower case (and the Sun docs pretty clearly
	   * specify upper case here), and because it doesn't provide a
	   * leading 0.
	   */
	  private static final String hex = "0123456789ABCDEF";

}
