package mini;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;

/**
 * And Auxiliary class containing functions for different parts of the codes
 * mainly for cleaner code.
 */
public class AuxFunctions {

    /**
     * @param s the string
     * @return  True if s contains a letter. else-False.
     */
    public static boolean hasLetters(String s){
        for(char c: s.toCharArray()){
            if(c>=65 && c<=90 || c>=97 && c<=122){
                return true;
            }
        }
        return false;
    }

    /**
     * @param s the string
     * @return True if String s has only Digits OR Letters. (Non-Illegal characters)
     */
    public static boolean allDigitsOrLetters(String s){
        for(char c:s.toCharArray()){
            if(!(c>=48 && c<=57 || c>=65 && c<=90 || c>=97 && c<=122 )) {
                return false;
            }
         }
         return true;
    }

    /**
     * Indicates if a given username is available-meaning it does not exist yet.
     * @param username the given Username
     * @param factory the FtpServerFactory to check
     * @return true if available
     * @throws FtpException exception.
     */
    public static boolean usernameAvailable(String username, FtpServerFactory factory) throws FtpException {
        String[] allUserNames=factory.getUserManager().getAllUserNames();
        for(String someUser:factory.getUserManager().getAllUserNames()){
            if(someUser.equals(username))
                return false;
        }
        return true;
    }
}
