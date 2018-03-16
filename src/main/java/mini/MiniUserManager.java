package mini;

import org.apache.ftpserver.ftplet.*;

public class MiniUserManager implements UserManager {

    public User getUserByName(String s) throws FtpException {
        return null;
    }

    public String[] getAllUserNames() throws FtpException {
        return new String[0];
    }

    public void delete(String s) throws FtpException {

    }

    public void save(User user) throws FtpException {

    }

    public boolean doesExist(String s) throws FtpException {
        return false;
    }

    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        return null;
    }

    public String getAdminName() throws FtpException {
        return null;
    }

    public boolean isAdmin(String s) throws FtpException {
        return false;
    }
}
