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
    
    private HashMap<String, Integer> iso_perm;    //random permutation to generate Graph Q
    private Integer iteration;
    
    public ZKPClient() {
    
    }
    
    
    public ZKPClient(String addr) {
        sflag = 0;
        iteration = 0;
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
        
        ZKPClient read = new ZKPClient();
        read.readgraph("graph.txt");
        
        /* establish connection with server */
        /*
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
        */
    }

    private int[] rand_permutation(int vnum){
        int[] permutation = new int[vnum];
        //ascending permutation
        for(int i = 0; i < vnum; i++){
            permutation[i] = i;
        }
        
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(vnum);
        String s;
        
        do{
            for(int i = 0; i < vnum; i++){
                int index = rand.nextInt(1+i);
                int a = permutation[index];
                permutation[index] = permutation[i];
                permutation[i] = a;
            }
            
            for (int i : permutation) {
                sb.append(i);
            }
            s = sb.toString();
        }while(iso_perm.get(s)!=null);
        
        iso_perm.put(s, iteration);
        iteration += 1;
        
        return permutation;
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
            iso_perm = new HashMap<String, Integer>(100);
            
            //read g2 string
            String g2s = vnum + "|";
            g2s = g2s.concat(sc.nextLine());
            g2 = new Graph(g2s);
            
            //read subgraph array
            int sub_vnum = Integer.parseInt(sc.nextLine());
            subgraph = new int[sub_vnum];
            for (int i = 0; i < sub_vnum; i++) {
                subgraph[i] = Integer.parseInt(sc.nextLine());
            }
            
            //read subgraph
            String g2ps = vnum + "|";
            g2ps = g2ps.concat(sc.nextLine());
            g2p = new Graph(g2ps);
            
            //read isomorphic permutation
            isomorphism = new int[vnum];
            for (int i = 0; i < vnum; i++) {
                isomorphism[i] = Integer.parseInt(sc.nextLine());
            }
            
            
            //add original permutation, iteration still 0
            int[] permutation = new int[vnum];
            for(int i = 0; i < vnum; i++){
                permutation[i] = i;
            }
            StringBuilder sb = new StringBuilder(vnum);
            String s;
            for (int i : permutation) {
                sb.append(i);
            }
            s = sb.toString();
            iso_perm.put(s,iteration);
            
            //add isomorphism permutation, iteration still 0
            for (int i : isomorphism) {
                sb.append(i);
            }
            s = sb.toString();
            iso_perm.put(s,iteration);
            
            
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
