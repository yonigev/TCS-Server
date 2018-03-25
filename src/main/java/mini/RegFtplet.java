package mini;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.command.Command;
import org.apache.ftpserver.command.CommandFactory;
import org.apache.ftpserver.command.CommandFactoryFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.UserFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gevaj on 3/8/18.
 */
public class RegFtplet extends DefaultFtplet {
    static String REGISTER_COMMAND = "USER !REGISTER!";
    private static final String REG_ERROR_MESSAGE = "Bad username or password.";
    private static final int MINIMAL_USERNAME_LENGTH = 3;
    FtpServerFactory serverFactory;

    public RegFtplet(FtpServerFactory serverFactory) {
        this.serverFactory = serverFactory;
    }

    @Override
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
        if (request.toString().startsWith(REGISTER_COMMAND)) {        //if Registration command
            handleRegisterCommand(session, request);                 //Handle it.
            return FtpletResult.DEFAULT;
        }
        return super.beforeCommand(session, request);
    }

    /**
     * Called when receiving USER !REGISTER! command
     *
     * @param session
     * @param request
     */
    private void handleRegisterCommand(FtpSession session, FtpRequest request) {
        String[] user_pass = request.getArgument().split(" ");    //user_pass=!REGISTER! user pass
        if (user_pass.length == 3) {
            String username = user_pass[1];               // the username to register
            String password = user_pass[2];               //the password
            try {
                if (isLegalRegistration(username, password)) {
                    serverFactory.getUserManager().save(createUser(username, password));

                } else {
                    //TODO:Send back an error to the user! (CHECK IF WORKS)
                    session.write(new BadRegistrationReply(600, REG_ERROR_MESSAGE));
                }
            } catch (FtpException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: ERROR IN USER INPUT
        }
    }

    private User createUser(String username, String password) {
        File dir = new File("/" + username);
        boolean dirCreated=dir.mkdir();
        BaseUser toAdd = new BaseUser();
        if(dirCreated) {
            //create new user
            toAdd.setName(username);
            toAdd.setPassword(password);
            toAdd.setHomeDirectory("/" + username);
            List<Authority> authorities = new ArrayList<Authority>();
            authorities.add(new WritePermission());
            toAdd.setAuthorities(authorities);
            return toAdd;
        }
        else{
            //TODO handle dir creation failed
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
