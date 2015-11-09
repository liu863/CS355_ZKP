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
        
    }
    
    /*
    public static void main(String[] ris) {
        int[][] adj = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        Graph g = new Graph(adj, 3);
        System.out.println(g.tostring());
    }
    */
}
