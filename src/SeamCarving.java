import java.io.*;
import java.util.*;

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
            flotFiltre.println(image[0].length + " " + image.length);
            flotFiltre.println("255");
            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[i].length; j++) {
                    flotFiltre.print(image[i][j] + " ");
                }
                System.out.println();
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

    private static ArrayList<Integer> dijkstra(Graph graph, int s, int t) {
        int min, cost;
        HashMap<Integer, Edge> parent = new HashMap<Integer, Edge>(graph.vertices());
        ArrayList<Integer> path = new ArrayList<>(graph.vertices());
        boolean[] visitedNodes = new boolean[graph.vertices()];
        Heap heap = new Heap(graph.vertices());
        heap.decreaseKey(0, 0); // initialisation du départ
        while (!heap.isEmpty()) {
            min = heap.pop();
            visitedNodes[min] = true;
            for (Edge e : graph.next(min)) {
                if (!visitedNodes[e.to]) {
                    cost = heap.priority(min) + e.cost;
                    if (cost < heap.priority(e.to)) {
                        heap.decreaseKey(e.to, cost);
                        parent.put(e.to, e);
                    }
                }
            }
        }
        int vertice = t;
        while (vertice != s) {
            path.add(parent.get(vertice).from);
            vertice = parent.get(vertice).from;
        }
        Collections.reverse(path);
        path.add(t);
        return path;
    }

    private static int[][] removePixels(int[][] img, ArrayList<Integer> removedNodes) {
        ArrayList<Integer> newPxl = new ArrayList<>(img.length);
        int[][] newPxlTab = new int[img.length][];

        for (int ligne = 0; ligne < img.length; ligne++) {
            int nbcolonne = img[ligne].length;
            newPxl.clear();
            for (int colonne = 0; colonne < nbcolonne; colonne++) {
                int numNoeud = colonne + nbcolonne * ligne + 1;
                if (!removedNodes.contains(numNoeud)) {
                    newPxl.add(img[ligne][colonne]);
                }
            }
            newPxlTab[ligne] = new int[newPxl.size()];
            newPxlTab[ligne] = newPxl.stream().mapToInt(i -> i).toArray();
        }
        return newPxlTab;
    }

    public static void main(String[] args) {
        //int table[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}};
        int table[][] = readpgm("ex41.pgm");
        //writepgm(table, "test1.pgm");

        for (int ii = 0; ii < 100; ii++) {
            System.out.println(ii + "ème cycle");

            int itr[][] = interest(table);
           /* for (int i = 0; i < itr.length; i++) {
                for (int j = 0; j < itr[i].length; j++) {
                    System.out.print(itr[i][j] + ",");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("Dijkstra");*/
            ArrayList<Integer> dijkstra = dijkstra(toGraph(itr), 0, itr.length * itr[0].length + 1);
            System.out.println(dijkstra);

            //System.out.println("Image finale");
            int[][] newImg = removePixels(table, dijkstra);
            /*for (int i = 0; i < newImg.length; i++) {
                for (int j = 0; j < newImg[i].length; j++) {
                    System.out.print(newImg[i][j] + ",");
                }
                System.out.println();
            }
            System.out.println();*/
            table = newImg;
        }
        writepgm(table, "essai.pgm");
    }
}
