import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SeamCarving {

    public static int[][] readpgm(String fn) {
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();
            String line = d.readLine();
            while (line.startsWith("#")) {
                line = d.readLine();
            }
            Scanner s = new Scanner(line);
            int width = s.nextInt();
            int height = s.nextInt();
            line = d.readLine();
            s = new Scanner(line);
            int maxVal = s.nextInt();
            int[][] im = new int[height][width];
            s = new Scanner(d);
            int count = 0;
            while (count < height * width) {
                im[count / width][count % width] = s.nextInt();
                count++;
            }
            return im;
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return null;
        }
    }

    public static void writepgm(int[][] image, String filename) {
        FileWriter flot;
        PrintWriter flotFiltre;
        File fichier;
        try {
            String nomFichier = filename;
            fichier = new File(nomFichier);
            if (fichier.exists()) {
                throw new IOException("Le fichier existe deja");
            }
            flot = new FileWriter(fichier);
            flotFiltre = new PrintWriter(flot);
            flotFiltre.println("P2");
            flotFiltre.println(image.length + " " + image[0].length);
            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[i].length; j++) {
                    flotFiltre.print(image[i][j] + " ");
                }
            }
            flotFiltre.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int[][] interest(int[][] image) {
        int interest[][] = new int[image.length][];

        for (int ligne = 0; ligne < image.length; ligne++) {
            int nbcolonne = image[ligne].length;
            interest[ligne] = new int[nbcolonne];
            for (int colonne = 0; colonne < nbcolonne; colonne++) {
                if (colonne - 1 < 0 && colonne + 1 < nbcolonne) {    // premier pixel, (rien à gauche)
                    interest[ligne][colonne] = Math.abs(image[ligne][colonne] - image[ligne][colonne + 1]);
                } else if (colonne - 1 >= 0 && colonne + 1 < nbcolonne) {
                    interest[ligne][colonne] = Math.abs((image[ligne][colonne] - (image[ligne][colonne - 1] + image[ligne][colonne + 1]) / 2));
                } else if (colonne - 1 >= 0 && colonne + 1 >= nbcolonne) {
                    interest[ligne][colonne] = Math.abs(image[ligne][colonne - 1] - image[ligne][colonne]);
                } else {
                    interest[ligne][colonne] = image[ligne][colonne];
                }
            }
        }
        return interest;
    }

    public static Graph toGraph(int[][] itr) {
        Graph graph = new Graph(itr.length * itr[0].length + 2);
        for (int i = 1; i < itr[0].length + 1; i++) {
            Edge edge = new Edge(0, i, 0);
            graph.addEdge(edge);
        }

        for (int ligne = 0; ligne < itr.length; ligne++) {
            int nbcolonne = itr[ligne].length;
            for (int colonne = 0; colonne < nbcolonne; colonne++) {

                int numNoeud = colonne + nbcolonne * ligne + 1;

                if (ligne + 1 < itr.length) {
                    if (colonne - 1 < 0 && colonne + 1 < nbcolonne) {    // premier noeud, (rien à gauche)

                        Edge edge = new Edge(numNoeud, numNoeud + nbcolonne, itr[ligne][colonne]);
                        Edge edgeP1 = new Edge(numNoeud, numNoeud + nbcolonne + 1, itr[ligne][colonne]);

                        graph.addEdge(edge);
                        graph.addEdge(edgeP1);

                    } else if (colonne - 1 >= 0 && colonne + 1 < nbcolonne) {

                        Edge edgeM1 = new Edge(numNoeud, numNoeud + nbcolonne - 1, itr[ligne][colonne]);
                        Edge edge = new Edge(numNoeud, numNoeud + nbcolonne, itr[ligne][colonne]);
                        Edge edgeP1 = new Edge(numNoeud, numNoeud + nbcolonne + 1, itr[ligne][colonne]);

                        graph.addEdge(edgeM1);
                        graph.addEdge(edge);
                        graph.addEdge(edgeP1);

                    } else if (colonne - 1 >= 0 && colonne + 1 >= nbcolonne) {

                        Edge edgeM1 = new Edge(numNoeud, numNoeud + nbcolonne - 1, itr[ligne][colonne]);
                        Edge edge = new Edge(numNoeud, numNoeud + nbcolonne, itr[ligne][colonne]);

                        graph.addEdge(edgeM1);
                        graph.addEdge(edge);

                    }
                } else {
                    Edge edge = new Edge(numNoeud, itr.length * itr[0].length + 1, itr[ligne][colonne]);
                    graph.addEdge(edge);
                }
            }
        }
        return graph;
    }

    public static ArrayList dijkstra(Graph graph, int s, int t) {
        ArrayList path = new ArrayList();
        Object[][] weightTab = new Object[graph.vertices()][3];
        for (int i = 0; i < weightTab.length; i++) {
            weightTab[i][0] = i; // provisoire : nom du sommet
            if (i == s) weightTab[i][1] = 0; // point de départ
            else weightTab[i][1] = -1; // cout
            weightTab[i][2] = false; // visité ?
        }
        Integer[][] parentTab = new Integer[graph.vertices()][2];
        for (int i = 0; i < parentTab.length; i++) {
            parentTab[i][0] = i; //provisoire : nom du sommet
            parentTab[i][1] = null; // prédecesseur
        }
        int min;

        do {
            min = min(weightTab);
            weightTab[min][2] = true; // visité
            Iterator ite = graph.adj((Integer) weightTab[min][0]).iterator();
            while (ite.hasNext()) {
                Edge e = (Edge) ite.next();
                int poidsP = (int) weightTab[min][1];
                int poidsPF = e.cost;
                int fils = e.to;
                int poidsF = (int) weightTab[fils][1];
                if ((fils != (Integer) weightTab[min][0] && !(boolean) weightTab[fils][2]) && ((poidsP + poidsPF < poidsF) || poidsF == -1)) {
                    weightTab[fils][1] = poidsP + poidsPF;
                    parentTab[fils][1] = min;
                }
            }
        }
        while (min != t);

        int o = t;
        path.add(o);
        while(parentTab[o][1] != null){
            path.add(parentTab[o][1]);
            o = parentTab[o][1];
        }
/*        System.out.println();

        for (int i = 0; i < weightTab.length; i++) {
            for (int j = 0; j < weightTab[i].length; j++) {
                System.out.print(weightTab[i][j] + ",");
            }
            System.out.println();
        }
*/
        /*for (int i = 0; i < parentTab.length; i++) {
            for (int j = 0; j < parentTab[i].length; j++) {
                System.out.print(parentTab[i][j] + ",");
            }
            System.out.println();
        }*/
        return path;
    }

    private static int min(Object[][] weightTab) {
        int i = 0;
        boolean trouve = false;
        while (i < weightTab.length && !trouve) {
            if ((int) weightTab[i][1] >= 0 && !(boolean) weightTab[i][2]) {
                trouve = true;
            }
            i++;
        }
        if (!trouve) {
            return -1; // erreur, il n'y a pas de minimum
        }
        int min = (int) weightTab[i - 1][1];
        int res = i - 1;
        for (int j = i; j < weightTab.length; j++) {
            if (min > (int) weightTab[j][1] && (int) weightTab[j][1] >= 0 && !(boolean) weightTab[i][2]) {
                min = (int) weightTab[j][1];
                res = j;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int table[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}};
        writepgm(table, "test1.pgm");

        int itr[][] = interest(table);
        for (int i = 0; i < itr.length; i++) {
            for (int j = 0; j < itr[i].length; j++) {
                System.out.print(itr[i][j] + ",");
            }
            System.out.println();
        }
        System.out.println();

        toGraph(itr).writeFile("testtttt.txt");

        System.out.println(dijkstra(toGraph(itr), 0, 13));
    }


}
