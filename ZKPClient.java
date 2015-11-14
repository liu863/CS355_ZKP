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

    public ZKPClient ( String addr ) {
        String serverAddress = addr; 
        try {
            s = new Socket(serverAddress, 2334);
        } catch ( IOException e ) {
            ;
        }
        
    }

    public static void main(String[] args) throws IOException {
        /* establish connection with server */
        ZKPClient client = new ZKPClient(args[0]);
        String ret = client.send ("123");
        System.out.println("Main received : " + ret );

    }

    public int[] rdper(int len) { return null; }

    public String send(String str) {
        String line = null; 
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println( str );
            while ( (line = input.readLine()) == null );
            System.out.println(line);
            if ( line.equals("exit") )
                s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
    
}
