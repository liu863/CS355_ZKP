import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ZKPServer {

    private static int round;
    private Graph HQ;
    private int HR;
    private Graph G1;
    private Graph G2;
    private static boolean[] res;
    private Graph Q;
    private Graph Qp;
    private int[] perm;
    private int[][] rand_matrix;
    private int[][] sub_rand_matrix;

    public static void main(String[] args) throws IOException {
        
        /* Initilization */
        round = 0;
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

    /* Verify the proof */
    public boolean execute(int mode) {
        boolean ret = false;
        if (mode == 0) {
            Graph myHQ = Q.commitment(rand_matrix);
            Graph PiG2 = G2.isomorphism(perm);
            ret = myHQ.equals(HQ) && Q.equals(PiG2);
        }
        else {
            Graph myHQp = Qp.commitment(sub_rand_matrix);
            for (int i = 0; i < myHQp.getvnum(); i++) {
                for (int j = 0; j < myHQp.getvnum(); j++) {
                    if ( ( Qp.getv(i,j) == 1 ) && ( myHQp.getv(i,j) != HQ.getv(i,j) ) )
                        return false;
                }
            }
            Graph PiG1 = G1.isomorphism(perm);
            ret = Qp.equals(PiG1);
            
        }
        return ret;
    }
 
}