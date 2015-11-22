import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ZKPClient {
    
    private Socket s;
    private int sflag;
    
    private Graph g1;   //iso to g2p
    private Graph g2;   //larger graph
    private Graph g2p;  //subgraph of g2, iso to g1
    
    private int[] subgraph;        //vertices in subgraph g2p
    private int[] isomorphism;     //isomorphic permutation from g2p to g2
    
    private int[][] rand_matrix;  //random number matrix to commit graph
    private int[][] sub_rand_matrix;
    
    public ZKPClient(String addr) {
        sflag = 0;
        String serverAddress = addr; 
        try {
            s = new Socket(serverAddress, 2334);
            sflag = 1;
        } catch ( IOException e ) {
            ;
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        
        /* Initialize random matrix and flags */
        //rand_matrix = gen_randmatrix(vnum);
        //sub_rand_matrix = gen_sub_randmatrix(rand_matrix, g2); 
        
        /* establish connection with server */
        ZKPClient client = new ZKPClient(args[0]);
        client.readgraph(args[1]);
        String ret = client.send ("123");
        System.out.println("Client received : " + ret );
        ret = client.send ("789");
        System.out.println("Client received : " + ret );
        ret = client.send ("exit");
        System.out.println("Client received : " + ret );
        ret = client.send ("789");
        System.out.println("Client received : " + ret );
        
    }
    
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
    
    private static int[][] gen_randmatrix( int vnum ) {
        int[][] randm = new int[vnum][vnum];
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                randm[i][j] = 10000 + (int)( Math.random() * 90000  );
            }
        } 
        return randm;
    }
    
    private static int[][] gen_sub_randmatrix( int[][] randm, Graph subgraph ) {
        int vnum = subgraph.getvnum();
        int[][] sub_randm = new int[vnum][vnum];
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                if ( subgraph.getv(i,j) == 1 ) 
                    sub_randm[i][j] = randm[i][j];
                else 
                    sub_randm[i][j] = 0;
                
            }
        } 
        return sub_randm;
    }
    
    private void readgraph(String file) {
        try {
            //input file name
            Scanner sc = new Scanner(new FileReader(file));
            
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
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        }
    }
}
