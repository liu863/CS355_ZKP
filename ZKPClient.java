import java.util.*;
import java.lang.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ZKPClient {
    
    private Socket s;
    static private int sflag;
    
    public ZKPClient ( String addr ) {
        String serverAddress = addr; 
        try {
            s = new Socket(serverAddress, 2334);
            sflag = 1;
        } catch ( IOException e ) {
            ;
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        sflag = 0;
        /* establish connection with server */
        ZKPClient client = new ZKPClient(args[0]);
        String ret = client.send ("123");
        System.out.println("Client received : " + ret );
        ret = client.send ("789");
        System.out.println("Client received : " + ret );
        ret = client.send ("exit");
        System.out.println("Client received : " + ret );
        ret = client.send ("789");
        System.out.println("Client received : " + ret );
        
    }
    
    public int[] rdper(int len) { return null; }
    
    public String send(String str) {
        if ( sflag == 0 ) {
            System.out.println( "err: socket non-exists" );
            System.exit(1);
        }
        String line = null; 
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println( str );
            while ( (line = input.readLine()) == null );
            //System.out.println(line);
            if ( line.equals("exit") ) {
                s.close();
                sflag = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
    
}
