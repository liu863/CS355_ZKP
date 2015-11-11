import java.lang.*;

public class Graph {
    int[][] adjmatrix;
    int vnum;
    
    public Graph(int[][] matrix, int num) {
        vnum = num;
        adjmatrix = new int[num][num];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                adjmatrix[i][j] = matrix[i][j];
            }
        }    
    }
    
    //str = n|xx..x
    public Graph(String str) {
        int pipe = str.indexOf('|');
        vnum = Integer.parseInt(str.substring(0, pipe));
        pipe++;
        adjmatrix = new int[vnum][vnum];
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                char val = str.charAt(pipe++);
                adjmatrix[i][j] = Character.getNumericValue(val);
            }
        }
    }
    
    public boolean link(int x, int y) {
        if (adjmatrix[x][y] == 1)
            return true;
        else
            return false;
    }
    
    public int vertexnum() {
        return vnum;
    }
    
    public String tostring() {
        String str = vnum + "|";
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                str = str.concat(Integer.toString(adjmatrix[i][j]));
            }
        }
        return str;
    }
    
    public Graph isomorphism(int[] p) {
        int[][] iso = new int[vnum][vnum];
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                iso[p[i]][p[j]] = adjmatrix[i][j];
            }
        }
        Graph g = new Graph(iso, vnum);
        return g;
    }
    
    public Graph commitment(int ran) {
        int[][] com = new int[vnum][vnum];
        for (int i = 0; i < vnum; i++) {
            for (int j = 0; j < vnum; j++) {
                com[i][j] = ((adjmatrix[i][j] + i + j) ^ ran) % 10;
            }
        }
        Graph commit = new Graph(com, vnum);
        return commit;
    }
    
    
    public static void main(String[] ris) {
        //int[][] adj = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        Graph g = new Graph("4|0111101011011010");
        System.out.println(g.tostring());
        Graph gc = g.commitment(365792);
        System.out.println(gc.tostring());
    }
    
}
