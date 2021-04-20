
public class ObjetoM{
    private int matriz [][];

    
    public ObjetoM(int x[][]){
        matriz= new int[4][4];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                matriz[i][j]=x[i][j];
            }
        }
    }
    public int[][] getmatriz(){
        return matriz;
    }
    
}
