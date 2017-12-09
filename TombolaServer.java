package tombola.server;

import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import javax.xml.bind.DatatypeConverter;

public class TombolaServer {
    
    ArrayList<Gamer> gamers = new ArrayList<Gamer>();
    
    public TombolaServer() {        
        try {
           ServerSocket server = new ServerSocket(80); 
           System.out.println("Server has started on 127.0.0.1:80.");
           while (true) {
               System.out.println("Waiting for a connection...");
               Socket client = server.accept();
               Gamer g = new Gamer(client, gamers);
               gamers.add(g);
               new Thread(g).start();
           }
        } catch (Exception e) {

        }
    }
    
    public static void main(String[] args) {
        new TombolaServer();
    }
    
}
