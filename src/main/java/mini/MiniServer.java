package mini;

import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * An FTP Server, Based on Apache FTP. Added ability to receive a Registration request
 */
public class MiniServer {
    private static final Integer PORT = 44444;
    private static final String USERS_FILE="./users";
    FtpServerFactory serverFactory;                                             // server factory
    ListenerFactory listenerFactory;                                            // listener factory
    File users;
    static TrayIcon trayIcon=new TrayIcon(createIcon("icon.png",""));

    /**
     * Create an image for an icon
     * @param path the image path
     * @param description description for the image
     * @return an Image
     */
    protected static Image createIcon(String path,String description){
        URL imageURL = MiniServer.class.getResource(path);
        if (imageURL == null) {
            System.err.println(path + " not found");
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    /**
     * Set a system tray icon for the Server
     */
    private static void setTrayIcon(){
        SystemTray tray=SystemTray.getSystemTray();
        try {
            if(trayIcon!=null)
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);


        } catch (AWTException e) {
            e.printStackTrace();
        }
        PopupMenu popupMenu=new PopupMenu();
        MenuItem exit=new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
        trayIcon.setPopupMenu(popupMenu);
        popupMenu.add(exit);
    }
    public void init_server() {
        setTrayIcon();
        serverFactory = new FtpServerFactory();
        listenerFactory = new ListenerFactory();
        listenerFactory.setPort(PORT);                                           //Set the port to listen to
        serverFactory.addListener("default", listenerFactory.createListener());  //Add as the default listener of the server
        this.users=new File(USERS_FILE);
        configUserManager(serverFactory);                                               //Config the UserManager to user Salted Passwords
        Ftplet regFtplet = new RegFtplet(serverFactory);                              //Custom Ftplet to support Registration
        serverFactory.getFtplets().put("SIGNUP", regFtplet);
        FtpServer s = serverFactory.createServer();                                 // create the server

        try {
            System.out.println("Starting the server");
            System.out.println(System.getProperty("user.dir"));
            s.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }

    }

    /**
     * Make the UserManager use a SaltedPasswordEncryptor.
     * Uses MD5 hash (128 bit)
     */
    private void configUserManager(FtpServerFactory serverFactory) {
        PropertiesUserManagerFactory factory=new PropertiesUserManagerFactory();
        PasswordEncryptor passwordEncryptor=new SaltedPasswordEncryptor();
        factory.setPasswordEncryptor(passwordEncryptor);
        factory.setFile(this.users);
        try {
            boolean createdNewFile=this.users.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        serverFactory.setUserManager(factory.createUserManager());
    }


    /**
     * Create a new Server Instance and initiate it.
     * @param args arguments
     */
    public static void main(String[] args) {
        MiniServer ms = new MiniServer();
        ms.init_server();

    }


}
