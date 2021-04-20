package utiles;
import java.awt.Color;
public enum Colores {
    
    cyan(255, 153, 153),
    verde(255, 204, 153),
    azul(255, 255, 153),
    azuloscuro(204, 255, 153),
    verdeoscuro(102,255,153),
    amarillo(153, 255, 204),
    amarillooscuro(153, 255, 255),
    marron(153,204,255),
    naranjaoscuro(153,153,255),
    rojo(204,153,255),
    rojooscuro(255,153,255),  
    ;
    Colores(int a,int b,int c){
        valor1=a;
        valor2=b;
        valor3=c;
    }
    
    public int getValor1() {
        return valor1;
    }
    public int getValor2() {
        return valor2;
    }
    public int getValor3() {
        return valor3;
    }
    public Color getcolor(){
        return new Color(valor1,valor2,valor3);
    }

    private final int valor1;
    private final int valor2;
    private final int valor3;

}
