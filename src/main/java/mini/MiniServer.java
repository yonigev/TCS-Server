package mini;

import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;

import java.util.Map;


public class MiniServer
{
    private static final Integer PORT=44444;
    FtpServerFactory serverFactory;                                             //server factory
    ListenerFactory  listenerFactory;                                           //listener factory


    public void init_server(){
        serverFactory=new FtpServerFactory();
        listenerFactory=new ListenerFactory();
        listenerFactory.setPort(PORT);                                           //Set the port to listen to
        serverFactory.addListener("default", listenerFactory.createListener());  //Add as the default listener of the server

        Ftplet regFtplet=new RegFtplet(serverFactory);                              //Custom Ftplet to support Registration
        serverFactory.getFtplets().put("SIGNUP", regFtplet);
        FtpServer s=serverFactory.createServer();                                 // create the server

        try {
            System.out.println("Starting the server");
            s.start();


        } catch (FtpException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args){
        MiniServer ms=new MiniServer();

        ms.init_server();




    }



}
