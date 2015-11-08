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

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        try
        {
            String serverAddress = args[0];
            Socket s = new Socket(serverAddress, 2334);
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line;
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("123");
            while ((line=input.readLine()) != null){
                System.out.println(line);
                if (line.equals("456")){
                    out.println("789");
                }
            }
            s.close();
            System.exit(0);
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    
}