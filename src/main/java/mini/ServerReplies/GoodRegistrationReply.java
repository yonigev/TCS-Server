package mini.ServerReplies;

import org.apache.ftpserver.ftplet.DefaultFtpReply;

public class GoodRegistrationReply extends DefaultFtpReply {
    public GoodRegistrationReply(int code, String message) {
        super(code, message);
    }
}
