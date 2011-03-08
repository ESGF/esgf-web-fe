package org.esgf.accounts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to validate string input from users.
 * 
 * @author Luca Cinquini
 *
 */
public class StringValidationUtils {
    
    private static Pattern notAlphaNumeric = Pattern.compile(".*[^a-zA-Z0-9].*");
    private static Pattern oneUpperCaseLetter = Pattern.compile(".*[A-Z].*");
    private static Pattern oneLowerCaseLetter = Pattern.compile(".*[a-z].*");
    private static Pattern oneNumber = Pattern.compile(".*[0-9].*");
    private static Pattern invalidCharacters = Pattern.compile(".*[><#$&!\\/\\\\+=].*");
    private static Pattern email = Pattern.compile(".+[@].+[\\.].+");
    
    public static boolean isNotAlphanumeric(final String s) {
        final Matcher m = notAlphaNumeric.matcher(s);
        return m.matches();
    }
    
    public static boolean hasOneUpperCaseLetter(final String s) {
        final Matcher m = oneUpperCaseLetter.matcher(s);
        return m.matches();
    }
    
    public static boolean hasOneLowerCaseLetter(final String s) {
        final Matcher m = oneLowerCaseLetter.matcher(s);
        return m.matches();
    }
    
    public static boolean hasOneNumber(final String s) {
        final Matcher m = oneNumber.matcher(s);
        return m.matches();
    }
    
    public static boolean hasInvalidCharacters(final String s) {
        final Matcher m = invalidCharacters.matcher(s);
        return m.matches();
    }

    public static boolean isEmail(final String s) {
        final Matcher m = email.matcher(s);
        return m.matches();
    }

}
