import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ZKPClient {
    
    private Socket s;
    private int sflag;
    
    public static int vnum;
    
    private static Graph g1;   //iso to g2p
    private static Graph g2;   //larger graph
    private static Graph g2p;  //subgraph of g2, iso to g1
    
    private static int[] subgraph;        //vertices in subgraph g2p
    private static int[] isomorphism;     //isomorphic permutation from g2p to g2
    
    private static int[][] rand_matrix;  //random number matrix to commit graph
    private static int[][] sub_rand_matrix;
    
    private static HashMap<String, Integer> iso_perm;    //random permutation to generate Graph Q
    private static Integer iteration;
    
    public ZKPClient() {
        iteration = 0;
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
        ZKPClient client = new ZKPClient(args[0]);
        String ret;
        System.out.println("\n\n                  Zero-Knowledge Proof Verification");

        while (true) {

            System.out.print("\n\n  Usage: Input filename to test; Input \"exit\" to quit\n\n  test-case file : ");
            Console c = System.console();
            String fileName = c.readLine();

            if (fileName.equals("exit")){
                ret = client.send("exit");
                System.exit(0);
            }
            
            ZKPClient read = new ZKPClient();
            read.readgraph(fileName);
            for (int i = 0; i < 100; i++) {
                int[] temp = read.rand_permutation(read.vnum);
                for (int j = 0; j < temp.length; j++)
                    System.out.print(temp[j] + " ");
                System.out.println();
        }
        
        /* establish connection with server */
        int round = 0;

        /* Initialize random matrix and flags */
        rand_matrix = gen_randmatrix(vnum);
        sub_rand_matrix = gen_sub_randmatrix(rand_matrix, g2); 

        while ( round < 100 ) {

            /* establish connection with server */
            int[] qiso = rand_permutation(vnum); //pi
            int[] alpha = genAlpha(qiso, isomorphism);

            Graph Q = g2.isomorphism(qiso);
            Graph Qp = g2p.isomorphism(qiso);

            Graph HQ = Q.commitment(rand_matrix);
            Graph HQp = Qp.commitment(sub_rand_matrix);

            // send G1, G2
            ret = client.send("g1g2|" + g1.toString() + "|" + g2.toString());
            System.out.println("clintmsg>> " + ret );

            // send HQ
            ret = client.send("hq|" + HQ.toString());
            System.out.println("clintmsg>> " + ret );

            // check server request and respond
            String[] str = ret.split("\\|");
            int req = Integer.parseInt(str[1]);

            if ( req == 0 ) {
                // send Q, pi, rand_matrix
                String s = arraytoString(qiso, vnum);
                ret = client.send("prf|" + Q.toString() + "|" +  s + "|" + darraytoString(rand_matrix, vnum) );

            }
            else {
                // send Qp, alpha
                String s = arraytoString(alpha, vnum);
                ret = client.send("prf|" + Qp.toString() + "|" +  s + "|" + darraytoString(sub_rand_matrix, vnum) );
            }
            System.out.println("clintmsg>> " + ret );
            if ( ret.equals("true") ) {
                round ++;
            }
            else {
                System.out.println("\n\n---------------------------\n\n  ZKP verification Failed!\n\n---------------------------");
                break;
            }
        }
        if ( round == 100 )
            System.out.println("\n\n---------------------------\n\n  ZKP verification Passed!\n\n---------------------------");
    }
    }

    private static int[] rand_permutation(int vnum){
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

    public static int[][] gen_sub_randmatrix( int[][] randm, Graph subgraph ) {
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

    private static String arraytoString(int[] arr, int vnum) {
        StringBuilder sb = new StringBuilder(vnum);
            for (int i : arr) {
              sb.append(i);
              sb.append(",");
            }
        String s = sb.toString();
        return s;
    }

    private static String darraytoString(int[][] darr, int vnum) {
        String str = new String();
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                if ( darr[i][j] != 0 )
                    str = str.concat(Integer.toString(darr[i][j]));
                else {
                    str = str.concat("00000");
                }
            }
        }
        return str;
    }

    private static int[] genAlpha(int[] Pi, int[] Iso) {
        int[] tmp = new int[vnum];
        int[] alpha = new int[vnum];

        for ( int i = 0; i < vnum; i++ )
            tmp[Pi[i]] = i;

        for ( int i = 0; i < vnum; i++ )
            alpha[i] = Iso[tmp[i]];

        return alpha;
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

    
    private void readgraph(String file) {
        try {
            //input file name
            Scanner sc = new Scanner(new FileReader(file));
            
            //number of vertices in g2
            vnum = Integer.parseInt(sc.nextLine());
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
            sb = new StringBuilder(vnum);
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
