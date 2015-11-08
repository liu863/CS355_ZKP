import java.util.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ZKPClient {

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        String serverAddress = args[0];
         System.out.println("flag-1");
        Socket s = new Socket(serverAddress, 2334);
        System.out.println("flag0");
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        System.out.println("flag1");
        String line;
        line=input.readLine();
        System.out.println(line);
        
        System.exit(0);
    }
    
}