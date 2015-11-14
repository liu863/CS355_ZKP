import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ZKPServer {
	public static void main(String[] args) throws IOException {
        int portNumber = 2334;
        ServerSocket serverSocket = new ServerSocket(portNumber);


        while (true) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Accepted");
			try {
			    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
			    String inputLine, outputLine;
				while ( true ) {
					if ( ( inputLine = in.readLine() ) == null )
						break;
			        if ( inputLine.equals("123") ) 
			        	out.println("456");
			        if ( inputLine.equals("789") ) 
			        	out.println("456");
			        if ( inputLine.equals("exit") ) {
			        	serverSocket.close();
			        	break;
			        }
	    		}
	    		System.out.println("flag0");
			}
			finally {
				;
			}
		}
	}
}