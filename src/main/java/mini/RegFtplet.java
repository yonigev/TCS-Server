package mini;

import mini.ServerReplies.BadRegistrationReply;
import mini.ServerReplies.GoodRegistrationReply;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Registration Request handler.
 */
public class RegFtplet extends DefaultFtplet {
    static final String REGISTER_COMMAND = "USER !REGISTER!";
    private static final int REG_ERROR_CODE=600;
    private static final int REG_SUCCESS_CODE=601;
    private static final String REG_ERROR_MESSAGE = "Bad username or password.";
    private static final String REG_SUCCESS_MESSAGE = "Registration Successful";
    private static final int MINIMAL_USERNAME_LENGTH = 3;
    private boolean REGISTRATION_SUCCESS=false;
    FtpServerFactory serverFactory;

    public RegFtplet(FtpServerFactory serverFactory) {
        this.serverFactory = serverFactory;
    }

    @Override
    /**
     * Handles cases where the Client sends a REGISTER Request
     */
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
        if (request.toString().startsWith(REGISTER_COMMAND)) {        //if Registration command
            this.REGISTRATION_SUCCESS=handleRegisterCommand(session, request);                 //Handle it.
            return FtpletResult.DEFAULT;
        }
        return super.beforeCommand(session, request);
    }

    @Override
    /**
     * If the request is a Registration request, send the client a reply.
     */
    public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException {

        DefaultFtpReply myReply;
        if(request.toString().startsWith(REGISTER_COMMAND)) {
            if (this.REGISTRATION_SUCCESS) {
                myReply = new DefaultFtpReply(REG_SUCCESS_CODE, REG_SUCCESS_MESSAGE);
            } else {
                myReply = new DefaultFtpReply(REG_ERROR_CODE, REG_ERROR_MESSAGE);
            }
            session.write(myReply);
            return super.afterCommand(session, request, myReply);

        }
        return super.afterCommand(session, request, reply);
    }


    /**
     * Called when receiving USER !REGISTER! command
     *
     * @param session
     * @param request
     */
    private boolean handleRegisterCommand(FtpSession session, FtpRequest request) {
        String[] user_pass = request.getArgument().split(" ");    //user_pass=!REGISTER! user pass
        if (user_pass.length == 3) {
            String username = user_pass[1];               // the username to register
            String password = user_pass[2];               //the password
            try {
                if (isLegalRegistration(username, password)) {
                    User toSave=createUser(username,password);
                    if(toSave!=null) {
                        serverFactory.getUserManager().save(toSave);
                        return true;
                    }
                    else{
                        //TODO: Handles double registration! (create a special reply for that).
                        //session.write(new BadRegistrationReply(REG_ERROR_CODE, REG_ERROR_MESSAGE));
                        return false;
                    }
                }
                else {
                    //TODO:Send back an error to the user! (CHECK IF WORKS)
                    //session.write(new BadRegistrationReply(REG_ERROR_CODE, REG_ERROR_MESSAGE));
                    return false;
                }
            }
            catch (FtpException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: ERROR IN USER INPUT
        }
        return false;
    }

    /**
     * Return a new user with a Directory and Write permissions.
     * @param username
     * @param password
     * @return
     */
    private User createUser(String username, String password) {
        try {
            File dir = new File("./" + username);
            boolean dirCreated = dir.mkdirs();
            BaseUser toAdd = new BaseUser();
            if (dirCreated) {
                //create new user
                toAdd.setName(username);
                toAdd.setPassword(password);
                toAdd.setHomeDirectory("./" + username);
                List<Authority> authorities = new ArrayList<Authority>();
                authorities.add(new WritePermission());
                toAdd.setAuthorities(authorities);
                return toAdd;
            } else {
                return null;
            }
        }
        catch (RuntimeException e){
            System.out.println("BAD");
            return null;

        }
    }

    /**
     * Indicates legal registration input from the user
     *
     * @param username
     * @param password
     * @return True if the username & password are legal
     */
    private boolean isLegalRegistration(String username, String password) {
        return (isLegalPassword(password) && isLegalUsername(username));
    }

    /**
     * Indicates if the user is trying to register a LEGAL username (legal characters and is available.)
     *
     * @param username
     * @return
     */
    private boolean isLegalUsername(String username) {
        boolean legalLength = username.length() >= MINIMAL_USERNAME_LENGTH;
        boolean usernameAvailable = false;
        try {
            usernameAvailable = AuxFunctions.usernameAvailable(username, this.serverFactory);
        } catch (FtpException e) {
            e.printStackTrace();

        }
        return (usernameAvailable && legalLength && AuxFunctions.hasLetters(username) && AuxFunctions.allDigitsOrLetters(username));


    }

    /**
     * Always true for now
     * TODO: ...
     *
     * @param password
     * @return
     */
    private boolean isLegalPassword(String password) {
        return true;
    }

    /**
     * Derive 3 keys from one Master key, in a deterministic manner
     * @param masterKey
     * @return
     */
    private String[] deriveKeys(String masterKey) {
        String[] keys = new String[3];


        return keys;
    }

}
