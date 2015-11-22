import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ZKPServer {

    private static Graph HQ;
    private static Graph HQp;
    private static Graph G1;
    private static Graph G2;
    private static Graph Q;
    private static Graph Qp;
    private static int[] perm;
    private static int[][] rand_matrix;
    private static int[][] sub_rand_matrix;

    public static void main(String[] args) throws IOException {
        
        /* Initilization */
        int req = 0;
        String vnumstr = null;
        int vnum = 0;
        int round = 0;
        int portNumber = 2334;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        boolean[] res = new boolean[100];
        
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
                    System.out.println("reqmsg>> " + inputLine );
                    if ( inputLine.contains("g1g2") ) {
                        String[] str = inputLine.split("\\|");
                        vnumstr = str[1];
                        vnum = Integer.parseInt(str[1]);
                        String str2 = new String(str[1] + "|" + str[2]);
                        String str4 = new String(str[1] + "|" + str[4]);
                        G1 = new Graph(str2);
                        G2 = new Graph(str4);
                        out.println("eg1g2");
                    }
                    if ( inputLine.contains("hq") ) {
                        String[] str = inputLine.split("\\|");
                        String str1 = new String(vnumstr + "|" + str[2]);
                        HQ = new Graph(str1);
                        if ( Math.random() < 0.5) {
                            out.println("req|0");
                        }
                        else {
                            out.println("req|1");
                            req = 1;
                        }
                    }
                    if ( inputLine.contains("prf") ) {
                        String[] str = inputLine.split("\\|");
                        if ( req == 0 ) {
                            String Qs = new String(vnumstr + "|" + str[2]);
                            Q = new Graph(Qs);
                            perm = stringtoArray(str[3], vnum);
                            rand_matrix = str2arry(str[4]);
                            HQ = Q.commitment(rand_matrix);
                            out.println(execute(0));
                        }
                        else {
                            String Qps = new String(vnumstr + "|" + str[2]);
                            Qp = new Graph(Qps);
                            perm = stringtoArray(str[3], vnum);
                            sub_rand_matrix = str2arry(str[4]);
                            HQp = Qp.commitment(sub_rand_matrix);
                            out.println(execute(1));
                        }
                        req = 0;
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
    private static boolean execute(int mode) {
        boolean ret = false;
        if (mode == 0) {
            Graph myHQ = Q.commitment(rand_matrix);
            for (int i = 0; i < myHQ.getvnum(); i++) {
                for (int j = 0; j < myHQ.getvnum(); j++) {
                    if ( ( Q.getv(i,j) == 1 ) && ( myHQ.getv(i,j) != HQ.getv(i,j) ) )
                        return false;
                }
            }
            Graph PiG2 = G2.isomorphism(perm);
            ret = Q.equals(PiG2);
        }
        else {
            Graph myHQp = Qp.commitment(sub_rand_matrix);
            for (int i = 0; i < myHQp.getvnum(); i++) {
                for (int j = 0; j < myHQp.getvnum(); j++) {
                    if ( ( Qp.getv(i,j) == 1 ) && ( myHQp.getv(i,j) != HQp.getv(i,j) ) )
                        return false;
                }
            }
            Graph alphaQp = Qp.isomorphism(perm);
            ret = G1.equals(alphaQp);
            
        }
        return ret;
    }

    public static int[][] str2arry(String str) {
        int num = (int)Math.sqrt(str.length() / 5);
        int[][] ret = new int[num][num];
        for (int i = 0; i < str.length();) {
            String temp = str.substring(i, i + 5);
            int t = Integer.parseInt(temp);
            ret[(i / 5) / num][(i / 5) % num] = t;
            i = i + 5;
        }
        return ret;
    }

    private static int[] stringtoArray(String s, int vnum) {
        String str[] = s.split(",");
        int[] tmp = new int[vnum];
        for(int i = 0; i < vnum; i++){
            tmp[i] = Integer.parseInt(str[i]) ;
        }
        return tmp;
    }
 
}