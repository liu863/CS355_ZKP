import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ZKPServer {

    private Graph HQ;
    private int HR;
    private Graph G1;
    private Graph G2;
    private static boolean[] res;
    private Graph Q;
    private Graph Qp;
    private int[] perm;
    private int rand;

    public static void main(String[] args) throws IOException {
        
        int portNumber = 2334;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        res = new boolean[100];
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client Request Accepted");
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
                String inputLine, outputLine;
                while ( true ) {
                    if ( ( inputLine = in.readLine() ) == null )
                        break;
                    System.out.println("Handling Request: " + inputLine );
                    if ( inputLine.equals("123") ) {
                        out.println("456");
                    }
                    if ( inputLine.equals("789") ) {
                        out.println("101112");
                    }
                    if ( inputLine.equals("exit") ) {
                        out.println("exit");
                        serverSocket.close();
                        System.exit(0);
                    }
                }
            }
            finally {
                ;
            }
        }
    }

    public boolean execute(int mode) {
        boolean ret = false;
        if ( int_commitment(rand) != HR )
            return false;
        if (mode == 0) {
            Graph myHQ = Q.commitment(rand);
            Graph PiG2 = G2.isomorphism(perm);
            ret = myHQ.compareTo(HQ) && Q.compareTo(PiG2);
        }
        else {
            Graph myHQp = Qp.commitment(rand);
            
        }
        return ret;
    }

    public int int_commitment(int ran) {
        return (int)Math.pow(ran, 322) % 666;
    }
}