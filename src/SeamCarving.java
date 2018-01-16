import java.io.*;
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
                    interest[ligne][colonne] = Math.abs((image[ligne][colonne] - (image[ligne][colonne - 1] + image[ligne][colonne + 1])/ 2));
                } else if (colonne - 1 >= 0 && colonne + 1 >= nbcolonne) {
                    interest[ligne][colonne] = Math.abs(image[ligne][colonne - 1] - image[ligne][colonne]);
                } else {
                    interest[ligne][colonne] = image[ligne][colonne];
                }
            }
        }
        return interest;
    }

    public static void main(String[] args) {
        int table[][] = {{3,11,24,39}, {8,21,29,39}, {200,60,25,0}};
        writepgm(table, "test1.pgm");

        int res[][] = interest(table);
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(res[i][j]+",");
            }
            System.out.println();
        }

    }


}
