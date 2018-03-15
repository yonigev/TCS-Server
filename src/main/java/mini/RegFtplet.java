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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gevaj on 3/8/18.
 */
public class RegFtplet extends DefaultFtplet {
    static String REGISTER_COMMAND= "USER !REGISTER!";

    FtpServerFactory serverFactory;
    public RegFtplet(FtpServerFactory serverFactory){
        this.serverFactory=serverFactory;
    }


    @Override
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
        if(request.toString().startsWith(REGISTER_COMMAND)){
            handleRegisterCommand(session,request);
            return FtpletResult.DEFAULT;
        }
        return super.beforeCommand(session, request);

    }

    /**
     * Called when receiving USER !REGISTER! command
     * @param session
     * @param request
     */
    private void handleRegisterCommand(FtpSession session,FtpRequest request) {
        String[] user_pass=request.getArgument().split(" ");    //user_pass=!REGISTER! user pass
        String username=user_pass[1];//
        String password=user_pass[2];
        BaseUser toAdd=new BaseUser();
        toAdd.setName(username);
        toAdd.setPassword(password);
        toAdd.setHomeDirectory("/"+username);
        List<Authority> authorities=new ArrayList<Authority>();
        authorities.add(new WritePermission());
        toAdd.setAuthorities(authorities);
        try {
            serverFactory.getUserManager().save(toAdd);
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

}
