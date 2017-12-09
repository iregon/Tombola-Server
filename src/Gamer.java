import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

public class Gamer implements Runnable{
    private Socket con;
    private InputStream in;
    private OutputStream out;
    
    private ArrayList<Gamer> gamers;
    
    private byte[] key;
    
    public Gamer(Socket con, ArrayList<Gamer> gamers) {
        try {
            this.con = con;
            this.in = con.getInputStream();
            this.out = con.getOutputStream();
            
            this.gamers = gamers;
            
            System.out.println("Nuovo gamer: " + con.getRemoteSocketAddress());
        } catch (Exception e) {
            
        }
    }

    public void run() {
        try {
            //Handshake
            
            //translate bytes of request to string
            String data = new Scanner(in,"UTF-8").useDelimiter("\\r\\n\\r\\n").next();
            
            Matcher get = Pattern.compile("^GET").matcher(data);
            
            if (get.find()) {
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                match.find();
                key = MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8"));
                byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Sec-WebSocket-Accept: "
                        + DatatypeConverter.printBase64Binary(key)
                        + "\r\n\r\n")
                        .getBytes("UTF-8");

                out.write(response, 0, response.length);
            }
            //Fine Handshake
            
            while (true) {
                
                byte[] decoded = new byte[6];
                byte[] encoded = new byte[6];
                in.read(encoded, 0, encoded.length);
                byte[] key = new byte[] {(byte) 167, (byte) 225, (byte) 225, (byte) 210};

                for (int i = 0; i < encoded.length; i++) {
                    decoded[i] = (byte)(encoded[i] ^ key[i & 0x3]);
                }
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
