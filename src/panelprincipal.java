
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utiles.Colores;

/**
 *
 * @author kevin
 */
public class panelprincipal extends JPanel implements KeyListener {
    private ArrayList<ObjetoM> jugadasAnt;
    private boolean flechaDer;
    private boolean flechaIzq;
    private boolean flechaarriba;
    private boolean flechaabajo;
    private int jugadaTemporal[][];
    private int ndeshacer;
    private JButton deshacer;
    private JButton reinicio;
    Font fuente;
    Random r = new Random();
    JLabel[][] fichas;
    JPanel tablero;
    int dos, cuatro, i, j;
    int[][] nucleo;
    boolean jugada = false;
    Color vacio = new Color(238, 238, 238);
    

    /*
     * 2 = gris; 4 =
     */
    public panelprincipal() {
        ndeshacer = 3;
        fuente = new Font("Britannic Bold", 1, 20);
        this.requestFocusInWindow();
        setLayout(new BorderLayout());
        setOpaque(true);
        setSize(600, 600);
        instanciarTablero();
        instanciarNucleo();
        addKeyListener(this);
        generarFichas();
        generarFichas();
        flechaDer = false;
        flechaIzq = false;
        flechaabajo = false;
        flechaarriba = false;
        jugadaTemporal = new int[4][4];
        jugadasAnt = new ArrayList<>();

        // probando cambio para subir a git
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {

    }

    /*
     * | 38 | (Arriba) | | 40 | (Abajo) | | 37 | (Izqierda) | | 39 | (Derecha) |
     * 
     * 
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == 37 || ke.getKeyCode() == 39 || ke.getKeyCode() == 38 || ke.getKeyCode() == 40) {
            // objetoM obs=new objetoM(nucleo);
            /*
             * if(jugada){ jugadasAnt.add(new ObjetoM(nucleo));
             * 
             * }
             */
            pasar(nucleo, jugadaTemporal);
            if (ke.getKeyCode() == 39) {
                movDerecha();
                flechaDer = false;
            } else if (ke.getKeyCode() == 37) {
                movIzq();
                flechaIzq = false;
            } else if (ke.getKeyCode() == 38) {
                movArri();
                flechaarriba = false;
            } else if (ke.getKeyCode() == 40) {
                movAbaj();
                flechaDer = false;
            }

            if (jugada) {
                jugadasAnt.add(new ObjetoM(jugadaTemporal));

                System.out.println("jugadas ant: " + jugadasAnt.size());
                if (v2048()) {
                    JOptionPane.showMessageDialog(null, "gano mi prro felicidades");
                } else {
                    generarFichas();
                    jugada = false;
                    if (!estaLibre()) {
                        if (!isMov()) {
                            JOptionPane.showMessageDialog(null, "ya perdio compa");
                        }
                    }
                }

            }

        }

    }

    // pasar el contenido de la matris A a B
    public void pasar(int a[][], int b[][]) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                b[i][j] = a[i][j];
            }
        }
    }

    public void instanciarNucleo() {
        nucleo = new int[4][4];
        for (int k = 0; k < nucleo.length; k++) {
            for (int l = 0; l < nucleo[0].length; l++) {
                nucleo[k][l] = 0;
            }
        }
    }

    public void instanciarTablero() {
        tablero = new JPanel();
        tablero.setLayout(new GridLayout(4, 4, 4, 4));
        tablero.setOpaque(true);
        tablero.setBackground(Color.black);
        tablero.setBounds(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        fichas = new JLabel[4][4];

        for (int i = 0; i < fichas.length; i++) {
            for (int j = 0; j < fichas[0].length; j++) {
                fichas[i][j] = new JLabel();
                fichas[i][j].setOpaque(true);
                fichas[i][j].setHorizontalAlignment((int) CENTER_ALIGNMENT);
                fichas[i][j].setVerticalAlignment((int) CENTER_ALIGNMENT);
                //fichas[i][j].setFont(new Font("comic kings", 0, 25));
                fichas[i][j].setFont(fuente);
                tablero.add(fichas[i][j]);
            }
        }
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 2));
        top.setBackground(Colores.amarillo.getcolor());
        // top.setSize(100,100);

        reinicio = new JButton("reiniciar");
        reinicio.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 10));
        reinicio.setContentAreaFilled(false);
        reinicio.setFont(fuente);
        reinicio.addActionListener(new manejador());
        reinicio.setFocusable(false);
        top.add(reinicio);

        deshacer = new JButton("deshacer (" + ndeshacer + ")");
        deshacer.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 10));
        deshacer.setContentAreaFilled(false);
        deshacer.setFont(fuente);
        deshacer.addActionListener(new manejadorD());
        deshacer.setFocusable(false);
        top.add(deshacer);

        add(top, BorderLayout.NORTH);
        add(tablero, BorderLayout.CENTER);
    }

    private class manejador implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            reseteo();

        }
    }

    private class manejadorD implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // System.out.println("*************************************");
            if (jugadasAnt.size() > 0) {
                // if(!Arrays.deepEquals(nucleo,jugadaTemporal)){
                resetear_a_nucleo(jugadasAnt.get(jugadasAnt.size() - 1).getmatriz());
                ndeshacer -= 1;
                deshacer.setText("deshacer (" + ndeshacer + ")");
                if (ndeshacer < 1) {

                    deshacer.setEnabled(false);
                }

                // }
            }
            // imprimir(jugadasAnt.get(jugadasAnt.size() - 1).getmatriz());

        }
    }

    public static void imprimir(int x[][]) {
        System.out.println("imprimiendo la que voy a eliminar");
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                System.out.print(x[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public void resetear_a_nucleo(int temporal[][]) {

        for (int i = 0; i < temporal.length; i++) {
            for (int j = 0; j < temporal[i].length; j++) {
                fichas[i][j].setBackground(vacio);
                switch (temporal[i][j]) {
                case 2:
                    fichas[i][j].setBackground(Colores.cyan.getcolor());
                    break;
                case 4:
                    fichas[i][j].setBackground(Colores.verde.getcolor());
                    break;
                case 8:
                    fichas[i][j].setBackground(Colores.azul.getcolor());
                    break;
                case 16:
                    fichas[i][j].setBackground(Colores.azuloscuro.getcolor());
                    break;
                case 32:
                    fichas[i][j].setBackground(Colores.verdeoscuro.getcolor());
                    break;
                case 64:
                    fichas[i][j].setBackground(Colores.amarillo.getcolor());
                    break;
                case 128:
                    fichas[i][j].setBackground(Colores.amarillooscuro.getcolor());
                    break;
                case 256:
                    fichas[i][j].setBackground(Colores.marron.getcolor());
                    break;
                case 512:
                    fichas[i][j].setBackground(Colores.naranjaoscuro.getcolor());
                    break;
                case 1024:
                    fichas[i][j].setBackground(Colores.rojo.getcolor());
                    break;
                case 2048:
                    fichas[i][j].setBackground(Colores.rojooscuro.getcolor());
                    break;
                default:
                    break;
                }
                if (temporal[i][j] == 0) {
                    fichas[i][j].setText("");
                } else {
                    fichas[i][j].setText("" + temporal[i][j]);
                }

            }
        }
        // faltaria la parte de igualar el nucleo a la variable que tengo almacenado las
        // jugadas anteriores

        // System.out.println("dimension antes de eliminar: "+ jugadasAnt.size());
        if (jugadasAnt.size() > 0) {
            nucleo = jugadasAnt.get(jugadasAnt.size() - 1).getmatriz();
            jugadasAnt.remove(jugadasAnt.size() - 1);
        }

    }

    public void reseteo() {
        for (int i = 0; i < fichas.length; i++) {
            for (int j = 0; j < fichas[i].length; j++) {
                fichas[i][j].setText("");
                fichas[i][j].setBackground(vacio);
                nucleo[i][j] = 0;
            }
        }
        generarFichas();
        generarFichas();
        jugadasAnt.clear();
        ndeshacer = 3;
        deshacer.setText("deshacer (" + ndeshacer + ")");
        deshacer.setEnabled(true);
    }

    public void generarFichas() {
        do {
            dos = r.nextInt(60);
            cuatro = r.nextInt(40);
        } while (dos == cuatro);
        do {
            i = r.nextInt(4);
            j = r.nextInt(4);
        } while (nucleo[i][j] != 0);
        if (cuatro > dos) {
            fichas[i][j].setText("4");
            fichas[i][j].setBackground(Colores.verde.getcolor());
            nucleo[i][j] = 4;
        } else {
            fichas[i][j].setText("2");
            nucleo[i][j] = 2;
            fichas[i][j].setBackground(Colores.cyan.getcolor());
        }
        // tablero.repaint();
        // getnucleo();

    }

    public void getnucleo() {
        for (int k = 0; k < nucleo.length; k++) {
            for (int l = 0; l < nucleo[0].length; l++) {
                System.out.print(nucleo[k][l] + "\t");
            }
            System.out.println("");
        }
        System.out.println("-----------");
    }

    /*
     * public void pausa(int i) { try { Thread.sleep(i); } catch (Exception e) { } }
     */

    public boolean estaLibre() {
        for (int k = 0; k < nucleo.length; k++) {
            for (int l = 0; l < nucleo[0].length; l++) {
                if (nucleo[k][l] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMov() {
        for (int k = 1; k < nucleo[0].length; k++) {
            for (int l = 0; l < nucleo.length; l++) {
                if (sonIguales(k, l, k - 1, l)) {
                    return true;
                }

            }
        }

        for (int k = nucleo[0].length - 2; k >= 0; k--) {
            for (int l = 0; l < nucleo.length; l++) {
                if (sonIguales(k, l, k + 1, l)) {
                    return true;
                }
            }
        }

        for (int k = 0; k < nucleo.length; k++) {
            for (int l = 1; l < nucleo[0].length; l++) {
                if (sonIguales(k, l, k, l - 1)) {
                    return true;
                }
            }
        }

        for (int k = 0; k < nucleo.length; k++) {
            for (int l = nucleo[0].length - 2; l >= 0; l--) {
                if (sonIguales(k, l, k, l + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * public void run(int e) { new Thread() {
     * 
     * @Override public void run() { pausa(1000); generarFichas();
     * 
     * 
     * } }.start(); }
     */

    public void movArri() {
        for (int k = 1; k < nucleo[0].length; k++) {
            for (int l = 0; l < nucleo.length; l++) {
                switch (k) {
                case 1:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k - 1][l] == 0) {
                            mover(k, l, k - 1, l);
                        } else if (sonIguales(k, l, k - 1, l)) {
                            sumar(k, l, k - 1, l);
                        }
                    }
                    break;
                case 2:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k - 1][l] == 0 && nucleo[k - 2][l] == 0) {
                            mover(k, l, k - 2, l);
                        } else if (sonIguales(k, l, k - 2, l) && nucleo[k - 1][l] == 0) {
                            sumar(k, l, k - 2, l);
                        } else if (nucleo[k - 1][l] == 0) {
                            mover(k, l, k - 1, l);
                        } else if (sonIguales(k, l, k - 1, l)) {
                            sumar(k, l, k - 1, l);
                        }
                    }
                    break;
                case 3:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k - 1][l] == 0 && nucleo[k - 2][l] == 0 && nucleo[k - 3][l] == 0) {
                            mover(k, l, k - 3, l);
                        } else if (sonIguales(k, l, k - 3, l) && nucleo[k - 2][l] == 0 && nucleo[k - 1][l] == 0) {
                            sumar(k, l, k - 3, l);
                        } else if (nucleo[k - 1][l] == 0 && nucleo[k - 2][l] == 0) {
                            mover(k, l, k - 2, l);
                        } else if (sonIguales(k, l, k - 2, l) && nucleo[k - 1][l] == 0) {
                            sumar(k, l, k - 2, l);
                        } else if (nucleo[k - 1][l] == 0) {
                            mover(k, l, k - 1, l);
                        } else if (sonIguales(k, l, k - 1, l)) {
                            sumar(k, l, k - 1, l);
                        }

                    }
                    break;
                }
            }
        }
        flechaarriba = true;
    }

    public void movAbaj() {
        for (int k = nucleo[0].length - 2; k >= 0; k--) {
            for (int l = 0; l < nucleo.length; l++) {
                switch (k) {
                case 2:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k + 1][l] == 0) {
                            mover(k, l, k + 1, l);
                        } else if (sonIguales(k, l, k + 1, l)) {
                            sumar(k, l, k + 1, l);
                        }
                    }
                    break;
                case 1:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k + 1][l] == 0 && nucleo[k + 2][l] == 0) {
                            mover(k, l, k + 2, l);
                        } else if (sonIguales(k, l, k + 2, l) && nucleo[k + 1][l] == 0) {
                            sumar(k, l, k + 2, l);
                        } else if (nucleo[k + 1][l] == 0) {
                            mover(k, l, k + 1, l);
                        } else if (sonIguales(k, l, k + 1, l)) {
                            sumar(k, l, k + 1, l);
                        }
                    }
                    break;
                case 0:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k + 1][l] == 0 && nucleo[k + 2][l] == 0 && nucleo[k + 3][l] == 0) {
                            mover(k, l, k + 3, l);
                        } else if (sonIguales(k, l, k + 3, l) && nucleo[k + 2][l] == 0 && nucleo[k + 1][l] == 0) {
                            sumar(k, l, k + 3, l);
                        } else if (nucleo[k + 1][l] == 0 && nucleo[k + 2][l] == 0) {
                            mover(k, l, k + 2, l);
                        } else if (sonIguales(k, l, k + 2, l) && nucleo[k + 1][l] == 0) {
                            sumar(k, l, k + 2, l);
                        } else if (nucleo[k + 1][l] == 0) {
                            mover(k, l, k + 1, l);
                        } else if (sonIguales(k, l, k + 1, l)) {
                            sumar(k, l, k + 1, l);
                        }

                    }
                    break;
                }
            }
        }
        flechaabajo = true;
    }

    public void movIzq() {
        for (int k = 0; k < nucleo.length; k++) {
            for (int l = 1; l < nucleo[0].length; l++) {
                switch (l) {
                case 1:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k][l - 1] == 0) {
                            mover(k, l, k, l - 1);
                        } else if (sonIguales(k, l, k, l - 1)) {
                            sumar(k, l, k, l - 1);
                        }
                    }
                    break;
                case 2:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k][l - 1] == 0 && nucleo[k][l - 2] == 0) {
                            mover(k, l, k, l - 2);
                        } else if (sonIguales(k, l, k, l - 2) && nucleo[k][l - 1] == 0) {
                            sumar(k, l, k, l - 2);
                        } else if (nucleo[k][l - 1] == 0) {
                            mover(k, l, k, l - 1);
                        } else if (sonIguales(k, l, k, l - 1)) {
                            sumar(k, l, k, l - 1);
                        }
                    }
                    break;
                case 3:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k][l - 1] == 0 && nucleo[k][l - 2] == 0 && nucleo[k][l - 3] == 0) {
                            mover(k, l, k, l - 3);
                        } else if (sonIguales(k, l, k, l - 3) && nucleo[k][l - 2] == 0 && nucleo[k][l - 1] == 0) {
                            sumar(k, l, k, l - 3);
                        } else if (nucleo[k][l - 1] == 0 && nucleo[k][l - 2] == 0) {
                            mover(k, l, k, l - 2);
                        } else if (sonIguales(k, l, k, l - 2) && nucleo[k][l - 1] == 0) {
                            sumar(k, l, k, l - 2);
                        } else if (nucleo[k][l - 1] == 0) {
                            mover(k, l, k, l - 1);
                        } else if (sonIguales(k, l, k, l - 1)) {
                            sumar(k, l, k, l - 1);
                        }

                    }
                    break;
                }
            }
        }
        flechaIzq = true;
    }

    public void movDerecha() {
        for (int k = 0; k < nucleo.length; k++) {
            for (int l = nucleo[0].length - 2; l >= 0; l--) {
                switch (l) {
                case 2:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k][l + 1] == 0) {
                            mover(k, l, k, l + 1);
                        } else if (sonIguales(k, l, k, l + 1)) {
                            sumar(k, l, k, l + 1);
                        }
                    }
                    break;
                case 1:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k][l + 1] == 0 && nucleo[k][l + 2] == 0) {
                            mover(k, l, k, l + 2);
                        } else if (sonIguales(k, l, k, l + 2) && nucleo[k][l + 1] == 0) {
                            sumar(k, l, k, l + 2);
                        } else if (nucleo[k][l + 1] == 0) {
                            mover(k, l, k, l + 1);
                        } else if (sonIguales(k, l, k, l + 1)) {
                            sumar(k, l, k, l + 1);
                        }
                    }
                    break;
                case 0:
                    if (nucleo[k][l] != 0) {
                        if (nucleo[k][l + 1] == 0 && nucleo[k][l + 2] == 0 && nucleo[k][l + 3] == 0) {
                            mover(k, l, k, l + 3);
                        } else if (sonIguales(k, l, k, l + 3) && nucleo[k][l + 2] == 0 && nucleo[k][l + 1] == 0) {
                            sumar(k, l, k, l + 3);
                        } else if (nucleo[k][l + 1] == 0 && nucleo[k][l + 2] == 0) {
                            mover(k, l, k, l + 2);
                        } else if (sonIguales(k, l, k, l + 2) && nucleo[k][l + 1] == 0) {
                            sumar(k, l, k, l + 2);
                        } else if (nucleo[k][l + 1] == 0) {
                            mover(k, l, k, l + 1);
                        } else if (sonIguales(k, l, k, l + 1)) {
                            sumar(k, l, k, l + 1);
                        }

                    }
                    break;
                }
            }
        }
        flechaDer = true;
    }

    public boolean sonIguales(int i, int j, int k, int l) {
        return nucleo[i][j] == nucleo[k][l];
    }

    public void mover(int i, int j, int k, int l) {

        fichas[k][l].setText("" + fichas[i][j].getText());
        fichas[k][l].setBackground(fichas[i][j].getBackground());
        nucleo[k][l] = nucleo[i][j];
        nucleo[i][j] = 0;
        fichas[i][j].setBackground(vacio);
        fichas[i][j].setText("");
        jugada = true;
    }

    public void sumar(int i, int j, int k, int l) {
        switch (nucleo[k][l]) {
        case 2:
            fichas[k][l].setBackground(Colores.verde.getcolor());
            break;
        case 4:
            fichas[k][l].setBackground(Colores.azul.getcolor());
            break;
        case 8:
            fichas[k][l].setBackground(Colores.azuloscuro.getcolor());
            break;
        case 16:
            fichas[k][l].setBackground(Colores.verdeoscuro.getcolor());
            break;
        case 32:
            fichas[k][l].setBackground(Colores.amarillo.getcolor());
            break;
        case 64:
            fichas[k][l].setBackground(Colores.amarillooscuro.getcolor());
            break;
        case 128:
            fichas[k][l].setBackground(Colores.marron.getcolor());
            break;
        case 256:
            fichas[k][l].setBackground(Colores.naranjaoscuro.getcolor());
            break;
        case 512:
            fichas[k][l].setBackground(Colores.rojo.getcolor());
            break;
        case 1024:
            fichas[k][l].setBackground(Colores.rojooscuro.getcolor());
            break;
        default:
            break;

        }

        nucleo[k][l] = nucleo[i][j] + nucleo[k][l];
        nucleo[i][j] = 0;
        fichas[k][l].setText("" + nucleo[k][l]);
        fichas[i][j].setBackground(vacio);
        fichas[i][j].setText("");
        jugada = true;
    }

    public boolean v2048() {
        for (int k = 0; k < nucleo.length; k++) {
            for (int l = 0; l < nucleo[0].length; l++) {
                if (nucleo[k][l] == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

}
