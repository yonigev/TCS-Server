package mini;

import org.apache.ftpserver.ftplet.DefaultFtpReply;
import org.apache.ftpserver.ftplet.FtpReply;

/**
 * Represets a Reply sent back to the client, when registering a BAD username\password
 */
public class BadRegistrationReply extends DefaultFtpReply {
    //private static final String ERROR_MESSAGE="Bad username or password.";
    private String message;
    private int code;
    private long sentTime;

    public BadRegistrationReply(int code, String message) {
        super(code, message);
        this.code=code;
        this.message=message;
        this.sentTime=System.currentTimeMillis();
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public long getSentTime() {
        return sentTime;
    }

    public String toString() {
        return getMessage();
    }

    public boolean isPositive() {
        return false;
    }
}
