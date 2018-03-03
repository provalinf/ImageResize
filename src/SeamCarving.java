import me.tongfei.progressbar.ProgressBar;

import java.io.*;
import java.util.*;

public class SeamCarving {
	static HashMap<Integer, Integer> costMap;

	public static int[][] readpgm(String fn) {
		try {
			//InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
			BufferedReader d = new BufferedReader(new FileReader(fn));
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

	public static int[][][] readppm(String fn) {
		try {
			BufferedReader d = new BufferedReader(new FileReader(fn));
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
			int[][][] im = new int[height][width][3];
			s = new Scanner(d);
			int count = 0;
			while (count < height * width) {
				im[count / width][count % width] = new int[]{s.nextInt(), s.nextInt(), s.nextInt()};
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

	public static void writeppm(int[][][] image, String filename) {
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
			flotFiltre.println("P3");
			flotFiltre.println(image[0].length + " " + image.length);
			flotFiltre.println("255");
			for (int[][] anImage : image) {
				for (int[] anAnImage : anImage)
					for (int RVB : anAnImage)
						flotFiltre.print(RVB + " ");
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

	public static int[][] interestRows(int[][] image) {
		int nbcolonne = image[0].length;
		int interest[][] = new int[nbcolonne][];
		for (int colonne = nbcolonne-1; colonne <= 0; colonne--) {
			for (int ligne = 0; ligne < image.length; ligne++) {
				interest[colonne-nbcolonne-1] = new int[image.length];
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

	public static int[][] interestColor(int[][][] image) {
		int interest[][] = new int[image.length][];

		for (int ligne = 0; ligne < image.length; ligne++) {
			int nbcolonne = image[ligne].length;
			interest[ligne] = new int[nbcolonne];
			for (int colonne = 0; colonne < nbcolonne; colonne++) {
				if (colonne - 1 < 0 && colonne + 1 < nbcolonne) {    // premier pixel, (rien à gauche)
					interest[ligne][colonne] = (int) Math.abs(averageRVB(image[ligne][colonne]) - averageRVB(image[ligne][colonne + 1]));
				} else if (colonne - 1 >= 0 && colonne + 1 < nbcolonne) {
					interest[ligne][colonne] = (int) Math.abs((averageRVB(image[ligne][colonne]) - (averageRVB(image[ligne][colonne - 1]) + averageRVB(image[ligne][colonne + 1])) / 2));
				} else if (colonne - 1 >= 0 && colonne + 1 >= nbcolonne) {
					interest[ligne][colonne] = (int) Math.abs(averageRVB(image[ligne][colonne - 1]) - averageRVB(image[ligne][colonne]));
				} else {
					interest[ligne][colonne] = (int) averageRVB(image[ligne][colonne]);
				}
			}
		}
		return interest;
	}

	private static double averageRVB(int[] RVB) {
		return Arrays.stream(RVB).average().getAsDouble();
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

	public static Graph toGraphEnergie(int[][] img) {
		Graph graph = new Graph(img.length * img[0].length + 2);
		for (int i = 1; i < img[0].length + 1; i++) {
			Edge edge = new Edge(0, i, 0);
			graph.addEdge(edge);
		}

		for (int ligne = 0; ligne < img.length; ligne++) {
			int nbcolonne = img[ligne].length;
			for (int colonne = 0; colonne < nbcolonne; colonne++) {

				int numNoeud = colonne + nbcolonne * ligne + 1;

				if (ligne + 1 < img.length) {
					if (colonne - 1 < 0 && colonne + 1 < nbcolonne) {    // premier noeud, (rien à gauche)

						Edge edge = new Edge(numNoeud, numNoeud + nbcolonne, img[ligne][colonne + 1]);
						Edge edgeP1 = new Edge(numNoeud, numNoeud + nbcolonne + 1, Math.abs(img[ligne][colonne + 1] - img[ligne + 1][colonne]));

						graph.addEdge(edge);
						graph.addEdge(edgeP1);

					} else if (colonne - 1 >= 0 && colonne + 1 < nbcolonne) {

						Edge edgeM1 = new Edge(numNoeud, numNoeud + nbcolonne - 1, Math.abs(img[ligne][colonne - 1] - img[ligne + 1][colonne]));
						Edge edge = new Edge(numNoeud, numNoeud + nbcolonne, Math.abs(img[ligne][colonne + 1] - img[ligne][colonne - 1]));
						Edge edgeP1 = new Edge(numNoeud, numNoeud + nbcolonne + 1, Math.abs(img[ligne][colonne + 1] - img[ligne + 1][colonne]));

						graph.addEdge(edgeM1);
						graph.addEdge(edge);
						graph.addEdge(edgeP1);

					} else if (colonne - 1 >= 0 && colonne + 1 >= nbcolonne) {

						Edge edgeM1 = new Edge(numNoeud, numNoeud + nbcolonne - 1, Math.abs(img[ligne][colonne - 1] - img[ligne + 1][colonne]));
						Edge edge = new Edge(numNoeud, numNoeud + nbcolonne, img[ligne][colonne - 1]);

						graph.addEdge(edgeM1);
						graph.addEdge(edge);

					}
				} else {
					Edge edge = new Edge(numNoeud, img.length * img[0].length + 1, Math.abs(((colonne - 1) < 0 ? 0 : img[ligne][colonne - 1]) - ((colonne + 1) >= nbcolonne ? 0 : img[ligne][colonne + 1])));
					graph.addEdge(edge);
				}
			}
		}
		return graph;
	}

	public static Graph toGraph2(int[][] itr) {
		int nbNodes = itr.length * itr[0].length + 2 + ((itr.length - 2) * itr[0].length);
		Graph graph = new Graph(nbNodes);
		for (int i = 1; i < itr[0].length + 1; i++) {
			Edge edge = new Edge(0, i, 0);
			graph.addEdge(edge);
		}

		int nbNodesInterm = 0;
		for (int ligne = 0; ligne < itr.length; ligne++) {
			int nbcolonne = itr[ligne].length;
			for (int colonne = 0; colonne < nbcolonne; colonne++) {

				int numNoeud = colonne + nbcolonne * (ligne + nbNodesInterm) + 1;

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

					if (ligne + 2 < itr.length) {
						Edge edgeV = new Edge(numNoeud + nbcolonne, numNoeud + nbcolonne * 2, 0);
						graph.addEdge(edgeV);
					}

				} else {
					Edge edge = new Edge(numNoeud, nbNodes - 1, itr[ligne][colonne]);
					graph.addEdge(edge);
				}
			}
			if (ligne + 2 < itr.length) nbNodesInterm++;
		}
		return graph;
	}

	public static ArrayList<Integer> dijkstra(Graph graph, int s, int t) {
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

	public static ArrayList<Integer> dijkstra2(Graph graph, int s, int t) {
		int min, cost;
		costMap = new HashMap<>(graph.vertices());
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
					costMap.put(e.from, heap.priority(min));
					if (cost < heap.priority(e.to)) {
						heap.decreaseKey(e.to, cost);
						parent.put(e.to, e);
					}
				}
			}
			costMap.put(graph.vertices() - 1, heap.priority(min));
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

	public static ArrayList<Integer>[] twoPath(Graph g, int s, int t) {
		ArrayList<Integer>[] res = new ArrayList[2];
		res[0] = dijkstra2(g, s, t);
		res[1] = new ArrayList<Integer>();
		ArrayList<Integer> shortPath = res[0];
		Iterator ite = g.edges().iterator();

		//différence entre les aretes
		int j = 0;
		while (ite.hasNext()) {
			Edge e = (Edge) ite.next();
			e.cost = e.cost + (costMap.get(e.from) - costMap.get(e.to));
			//System.out.println(shortPath);
			if (j + 1 < shortPath.size()) {
				int from = shortPath.get(j);
				int to = shortPath.get(j + 1);
				if (e.from == from && e.to == to) {
					inverseSens(e);
					j++;
				}
			}
			//System.out.println(e);
		}

		ArrayList<Integer> dij = dijkstra2(g, s, t);
		int max = dij.get(0);
		//for (int node : dij) { // /!\ Non
		for (int id = 0; id < dij.size(); id++) {
			if (dij.get(id) >= max) {
				max = dij.get(id);
				res[1].add(dij.get(id));
			} else {
				ite = g.edges().iterator();
				while (ite.hasNext()) {
					Edge e = (Edge) ite.next();
					if (e.from == dij.get(id)) {
						if (dij.contains(e.to)) {
							dij.removeIf(value -> value == e.to);
						}
					}
				}
			}
		}
		return res;
	}

	private static void inverseSens(Edge e) {
		int tempo = e.from;
		e.from = e.to;
		e.to = tempo;
	}

	private static int convertNumNodeZeroToPixel(int numNode, int width, int max) {
		if (numNode <= width) return numNode;
		if (numNode == max) return numNode;
		numNode = numNode - 1;
		int numligne = (numNode / width);
		int nbLigneZero = numligne / 2;
		/*System.out.println("ligne : " + numligne);
		System.out.println(numligne % 2);
		System.out.println(nbLigneZero + " à zero");*/
		return numligne % 2 == 0 ? -1 : numNode - nbLigneZero * width + 1;
	}

	public static int[][] removePixels(int[][] img, ArrayList<Integer> removedNodes) {
		return removePixels(img, new ArrayList[]{removedNodes}, false);
	}

	public static int[][] removePixels(int[][] img, ArrayList<Integer>[] removedNodes, boolean numNodeWithZero) {
		if (numNodeWithZero) {
			removedNodes[0].addAll(removedNodes[1]);
			removedNodes[0].forEach(value -> value = convertNumNodeZeroToPixel(value, img[0].length, img.length));
			removedNodes[0].removeIf(value -> value == -1);
		}
		ArrayList<Integer> newPxl = new ArrayList<>(img.length);
		int[][] newPxlTab = new int[img.length][];

		for (int ligne = 0; ligne < img.length; ligne++) {
			int nbcolonne = img[ligne].length;
			newPxl.clear();
			for (int colonne = 0; colonne < nbcolonne; colonne++) {
				int numNoeud = colonne + nbcolonne * ligne + 1;
				if (!removedNodes[0].contains(numNoeud)) {
					newPxl.add(img[ligne][colonne]);
				}
			}
			newPxlTab[ligne] = new int[newPxl.size()];
			newPxlTab[ligne] = newPxl.stream().mapToInt(i -> i).toArray();
		}
		return newPxlTab;
	}

	public static int[][][] removePixelsColor(int[][][] img, ArrayList<Integer> removedNodes) {
		ArrayList<int[]> newPxl = new ArrayList<>(img.length);
		int[][][] newPxlTab = new int[img.length][][];

		for (int ligne = 0; ligne < img.length; ligne++) {
			int nbcolonne = img[ligne].length;
			newPxl.clear();
			for (int colonne = 0; colonne < nbcolonne; colonne++) {
				int numNoeud = colonne + nbcolonne * ligne + 1;
				if (!removedNodes.contains(numNoeud)) {
					newPxl.add(img[ligne][colonne]);
				}
			}
			newPxlTab[ligne] = new int[newPxl.size()][];
			for (int colonne = 0; colonne < newPxl.size(); colonne++) {
				newPxlTab[ligne][colonne] = Arrays.stream(newPxl.get(colonne)).toArray();
			}
		}
		return newPxlTab;
	}

	private static void verifieArguments(String[] args) {
		if (args.length != 3) {
			System.out.println("Vous devez spécifier en arguments ./SeamCarving imageInitiale.pgm imageFinale.pgm nbPixelsHorizontalSuppr");
			System.exit(0);
		}

		if (!new File(args[0]).exists()) {
			System.out.println("Le fichier source n'existe pas !");
			System.exit(0);
		}

		if (new File(args[1]).exists()) {
			System.out.println("Le fichier de destination existe déjà !");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		verifieArguments(args);
		int table[][] = readpgm(args[0]);

		if (table == null || table.length <= 0 || table[0].length <= 0) {
			System.out.println(table == null);
			System.out.println(table.length);
			System.out.println(table[0].length);
			System.out.println("L'image sélectionnée est trop petite !");
			System.exit(1);
		}
		if (table[0].length <= Integer.parseInt(args[2])) {
			System.out.println("L'image selectionnée n'est pas assez grande par rapport au nombre de pixel que vous souhaitez retirer !");
			System.exit(2);
		}

		ProgressBar pb = new ProgressBar(" " + args[0] + " -> " + args[1], Integer.parseInt(args[2])/*, ProgressBarStyle.ASCII*/);

		pb.start();
		for (int ii = 0; ii < Integer.parseInt(args[2]); ii++) {
			//System.out.println(ii + "ème cycle");
			pb.setExtraMessage("Calcul de l'interet de chaque pixels.");
			int itr[][] = interest(table);
           /* for (int i = 0; i < itr.length; i++) {
                for (int j = 0; j < itr[i].length; j++) {
                    System.out.print(itr[i][j] + ",");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("Dijkstra");*/
			pb.setExtraMessage("Calcul des pixels à supprimer........");
			ArrayList<Integer> dijkstra = dijkstra(toGraph(itr), 0, itr.length * itr[0].length + 1);
			//System.out.println(dijkstra);

			pb.setExtraMessage("Suppression des pixels...............");
			int[][] newImg = removePixels(table, dijkstra);
			pb.step();
			table = newImg;
		}

		pb.stepTo(Integer.parseInt(args[2]));
		pb.setExtraMessage("Ecriture du fichier " + args[1]);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		writepgm(table, args[1]);
		pb.setExtraMessage("Fin !");
		pb.stop();
	}
}
