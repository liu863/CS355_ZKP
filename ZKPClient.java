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
    
    private static Graph g1;   //iso to g2p
    private static Graph g2;   //larger graph
    private static Graph g2p;  //subgraph of g2, iso to g1
    
    private static int[] subgraph;        //vertices in subgraph g2p
    private static int[] isomorphism;     //isomorphic permutation from g2p to g2
    
    private static int random;  //random number to commit graph
    
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
        //input file name
        String infile = args[1];
        Scanner sc = new Scanner(infile);
        //number of vertices in g2
        int vnum = Integer.parseInt(sc.nextLine());
        //read g2 string
        String g2s = vnum + "|";
        g2s = g2s.concat(sc.nextLine());
        g2 = new Graph(g2s);
        
        //read subgraph array
        String sub = sc.nextLine();
        subgraph = new int[sub.length()];
        for (int i = 0; i < sub.length(); i++) {
            subgraph[i] = Character.getNumericValue(sub.charAt(i));
        }
        
        //read subgraph
        String g2ps = vnum + "|";
        g2ps = g2ps.concat(sc.nextLine());
        g2p = new Graph(g2ps);
        
        //read isomorphic permutation
        String iso = sc.nextLine();
        isomorphism = new int[vnum];
        for (int i = 0; i < vnum; i++) {
            isomorphism[i] = Character.getNumericValue(iso.charAt(i));
        }
        
        //read g1
        String g1s = vnum + "|";
        g1s = g1s.concat(sc.nextLine());
        g1 = new Graph(g1s);
      
        
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
