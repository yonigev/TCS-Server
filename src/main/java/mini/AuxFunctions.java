package mini;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;

public class AuxFunctions {

    /**
     * @param s
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
     * @param s
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
     * @param username
     * @param factory
     * @return
     * @throws FtpException
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
