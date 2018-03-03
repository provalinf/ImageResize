import me.tongfei.progressbar.ProgressBar;

import java.io.*;
import java.util.*;

public class SeamCarving {
	private static HashMap<Integer, Integer> costMap;

	/**
	 * Lit et converti une image NB de type PGM ASCII
	 * en un tableau de pixels
	 *
	 * @param fn : path de l'image
	 * @return tableau de pixels
	 */
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

	/**
	 * Lit et converti une image RVB de type PPM ASCII
	 * en un tableau de pixels
	 *
	 * @param fn : path de l'image
	 * @return tableau de pixels
	 */
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

	/**
	 * Converti un tableau de pixels en une image (fichier) NB de type PGM ASCII
	 *
	 * @param image    : tableau de pixels
	 * @param filename : nom du fichier image
	 */
	public static void writepgm(int[][] image, String filename) {
		FileWriter flot;
		PrintWriter flotFiltre;
		File fichier;
		try {
			fichier = new File(filename);
			if (fichier.exists()) {
				throw new IOException("Le fichier existe deja");
			}
			flot = new FileWriter(fichier);
			flotFiltre = new PrintWriter(flot);
			flotFiltre.println("P2");
			flotFiltre.println(image[0].length + " " + image.length);
			flotFiltre.println("255");
			for (int[] anImage : image) {
				for (int anAnImage : anImage) {
					flotFiltre.print(anAnImage + " ");
				}
				System.out.println();
			}
			flotFiltre.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Converti un tableau de pixels en une image (fichier) RVB de type PPM ASCII
	 *
	 * @param image    : tableau de pixels
	 * @param filename : nom du fichier image
	 */
	public static void writeppm(int[][][] image, String filename) {
		FileWriter flot;
		PrintWriter flotFiltre;
		File fichier;
		try {
			fichier = new File(filename);
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

	/**
	 * Calcul du facteur d'intéret de chaque pixels NB
	 *
	 * @param image : image à traiter
	 * @return tableau comprenant les facteurs d'intéret
	 */
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

	/*public static int[][] interestRows(int[][] image) {
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
	}*/

	/**
	 * Calcul du facteur d'intéret de chaque pixels RVB
	 *
	 * @param image : image à traiter
	 * @return tableau comprenant les facteurs d'intéret
	 */
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

	/**
	 * Réalise la moyenne des 3 couleurs RVB d'un pixel
	 *
	 * @param RVB : tableau de 3 couleur
	 * @return moyenne (double)
	 */
	@SuppressWarnings("ConstantConditions")
	private static double averageRVB(int[] RVB) {
		return Arrays.stream(RVB).average().getAsDouble();
	}

	/**
	 * Crée un graph à partir des facteurs d'intéret d'une image
	 *
	 * @param itr : tableau de facteurs d'intéret
	 * @return graph
	 */
	@SuppressWarnings("Duplicates")
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

	/**
	 * Crée un graph énergie à partir d'une image
	 * représentant
	 *
	 * @param img : image source
	 * @return graph
	 */
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

	/**
	 * Crée un graph à partir des facteurs d'intéret d'une image
	 * tout replaçant chaque sommet intérieur par deux sommets
	 * relié par une arête de poids nul
	 *
	 * @param itr : tableau de facteurs d'intéret
	 * @return graph
	 */
	@SuppressWarnings("Duplicates")
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

	/**
	 * Algorithme de dijsktra recherchant les noeuds
	 * composant le plus court chemin
	 *
	 * @param graph : graph source
	 * @param s     : sommet de départ
	 * @param t     : sommet de d'arrivé
	 * @return liste des noeuds du plus court chemin
	 */
	public static ArrayList<Integer> dijkstra(Graph graph, int s, int t) {
		int min, cost;
		HashMap<Integer, Edge> parent = new HashMap<>(graph.vertices());
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

	/**
	 * Algorithme de dijsktra recherchant les noeuds
	 * composant le plus court chemin
	 * Enregistre également pour chaque noeud son coût minimal
	 * <p>
	 * /!\ variable globale costMap : cout minimal des noeuds
	 *
	 * @param graph : graph source
	 * @param s     : sommet de départ
	 * @param t     : sommet de d'arrivé
	 * @return liste des noeuds du plus court chemin
	 */
	private static ArrayList<Integer> dijkstra2(Graph graph, int s, int t) {
		int min, cost;
		costMap = new HashMap<>(graph.vertices());
		costMap.put(0, 0);    // cout premer noeud
		HashMap<Integer, Edge> parent = new HashMap<>(graph.vertices());
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
					if (costMap.get(e.to) == null || e.cost + costMap.get(e.from) < costMap.get(e.to)) {
						costMap.put(e.to, e.cost + costMap.get(e.from));
					}
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

	/**
	 * Recherche deux plus courts chemin dans un graph
	 *
	 * @param g : graph source
	 * @param s : sommet de départ
	 * @param t : sommet d'arrivé
	 * @return listes de deux plus courts chemins
	 */
	public static ArrayList<Integer>[] twoPath(Graph g, int s, int t) {
		@SuppressWarnings("unchecked") ArrayList<Integer>[] res = new ArrayList[2];
		res[0] = dijkstra2(g, s, t);
		res[1] = new ArrayList<>();
		ArrayList<Integer> shortPath = res[0];
		Iterator ite = g.edges().iterator();

		//différence entre les aretes
		int j = 0;
		while (ite.hasNext()) {
			Edge e = (Edge) ite.next();
			//System.out.print("noeud : "+e.from);
			e.cost = e.cost + (costMap.get(e.from) - costMap.get(e.to));
			//System.out.println(" coutmin : " + costMap.get(e.from));
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
							for (int idd = 0; idd < res[0].size(); idd++) {
								if (res[0].get(idd) == e.from) {
									res[0].set(idd + 1, e.to);
									break;
								}
							}
							dij.removeIf(value -> value == e.to);
							//System.exit(0);
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * Inverse le sens de l'arête du noeud
	 *
	 * @param e : noeud
	 */
	private static void inverseSens(Edge e) {
		int tempo = e.from;
		e.from = e.to;
		e.to = tempo;
	}

	/**
	 * Converti un numéro de noeud en son équivalent pixel
	 * pour un graph dont on a ajouté des noeuds
	 * reliés par des arêtes de coût nul.
	 * <p>
	 * Retourne -1 si le noeud n'est pas un pixel
	 *
	 * @param numNode : numéro du noeud
	 * @param width   : largeur du graph
	 * @param max     : numéro du noeud de fin
	 * @return numéro du pixel
	 */
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

	/**
	 * Supprime les pixels, passés en paramètres, de l'image NB
	 * <p>
	 * Surcharge de la méthode removePixels()
	 *
	 * @param img          : image source
	 * @param removedNodes : liste des numéros des pixels à supprimer
	 * @return image traitée
	 */
	@SuppressWarnings("unchecked")
	public static int[][] removePixels(int[][] img, ArrayList<Integer> removedNodes) {
		return removePixels(img, new ArrayList[]{removedNodes}, false);
	}

	/**
	 * Supprime les pixels, passés en paramètres, de l'image NB
	 * Traite les listes (max 2) des numéros des noeuds
	 * ou numéros des pixels à supprimer
	 * avec la méthode convertNumNodeZeroToPixel() si
	 * le numNodeWithZero est à true
	 *
	 * @param img             : image source
	 * @param removedNodes    : listes (max 2) des numéros des pixels ou des noeuds à supprimer
	 * @param numNodeWithZero : boolean true si liste de noeuds, false si liste de pixels
	 * @return image traitée
	 */
	public static int[][] removePixels(int[][] img, ArrayList<Integer>[] removedNodes, boolean numNodeWithZero) {
		if (numNodeWithZero) {
			removedNodes[0].addAll(removedNodes[1]);
			removedNodes[0].forEach(value -> value = convertNumNodeZeroToPixel(value, img[0].length, (img.length * img[0].length + 2 + ((img.length - 2) * img[0].length)) - 1));
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

	/**
	 * Supprime les pixels, passés en paramètres, de l'image RVB
	 *
	 * @param img          : image source
	 * @param removedNodes : liste des numéros des pixels à supprimer
	 * @return image traitée
	 */
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

	/**
	 * Vérifie la validité des arguments lors de l'exécution du programme
	 *
	 * @param args : tableau d'arguments
	 */
	private static void verifieArguments(String[] args) {
		if (args.length == 1 && args[0].equals("666")) {
			System.out.println("Easter Egg");
			System.out.println("                   :I. ..\n" +
					"                  :III/ I.\n" +
					"                 : III  II\n" +
					" Modelisation   :  III .II\n" +
					"     666        : .III III\n" +
					"                : III' III\n" +
					"                : III  II'\n" +
					"                : `I/__L_\n" +
					"              ./'        ~~-.\n" +
					"             .\"   -~~-       `.\n" +
					"             :    .==.         :\n" +
					"             |    `..b'      ___:\n" +
					"             |           __.`\\__/__ \n" +
					"              `.       ----   _i_---\n" +
					"              / `-........:`----'\n" +
					"            /'    ,MMMMMMM\n" +
					"          :'     .MMMMMMMMm\n" +
					"         :'      mMMMMMMMMMm\n" +
					"        /:       MMMMMMMMMMM\n" +
					"      /' :       MMMMMMMMMMM\n" +
					"    /\"           `MMMMMMMMM'\n" +
					"   :      \\       `MMMMMMM'\\\n" +
					"  :'       `:      MMMMMMM: `\\\n" +
					"  :         `:     `MMMMM':   `:\n" +
					"  :      mMMMm      MMMM' :    :\n" +
					"  :     mMMMMMMm    mMMm  :    :\n" +
					"   \\    `MMMMMMm    mMMm  :   /\n" +
					"  /~~~   MMMMMMm    mJVm  : /'___\n" +
					":'| |   /`JMMMMm . .m96m  \\      \\\n" +
					" ~~~~~~~        \\_:_|   L_/~~~~~~\n" +
					"Cyril BASILE - Valentin BONNAL\n");
		}

		if (args.length < 2 || args.length > 4) {
			help();
			System.exit(0);
		}

		int ext1 = args[0].lastIndexOf('.');
		int ext2 = args[1].lastIndexOf('.');

		if (!args[0].substring(ext1).equalsIgnoreCase(".pgm")
				&& !args[0].substring(ext1).equalsIgnoreCase(".ppm")) {
			System.out.println("L'extension n'est pas supportée");
			help();
			System.exit(0);
		}

		if (!args[0].substring(ext1).equalsIgnoreCase(args[1].substring(ext2))) {
			System.out.println("L'extension de sortie doit être la même que l'extension d'entrée");
			help();
			System.exit(0);
		}

		if (args.length == 4 && !args[3].equals("twopath") && !args[3].equals("energie")) {
			help();
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

	private static void help() {
		System.out.println("Vous devez spécifier en arguments :\n" +
				" - ./SeamCarving imageInitiale.pgm imageFinale.pgm [nbPixelsHorizontalSuppr]\n" +
				" - ./SeamCarving imageInitiale.ppm imageFinale.ppm [nbPixelsHorizontalSuppr]\n" +
				" - ./SeamCarving imageInitiale.pgm imageFinale.pgm nbPixelsHorizontalSuppr twopath /!\\pixels rem x2\n" +
				" - ./SeamCarving imageInitiale.pgm imageFinale.pgm nbPixelsHorizontalSuppr energie\n" +
				" [nbPixelsHorizontalSuppr] default=50\n\n");
	}

	public static void main(String[] args) {
		verifieArguments(args);

		if (args[0].substring(args[0].lastIndexOf('.')).equalsIgnoreCase(".pgm")) {
			pgm(args);
		} else {
			ppm(args);
		}

	}

	private static void ppm(String[] args) {
		int table[][][] = readppm(args[0]);
		if (table == null || table.length <= 0 || table[0].length <= 4) {
			System.out.println(table == null);
			System.out.println(Objects.requireNonNull(table).length);
			System.out.println(table[0].length);
			System.out.println("L'image sélectionnée est trop petite !");
			System.exit(1);
		}

		int nbPixels = args.length > 2 ? Integer.parseInt(args[2]) : 50;

		if (table[0].length <= nbPixels) {
			System.out.println("L'image selectionnée n'est pas assez grande par rapport au nombre de pixel à retirer !");
			System.exit(2);
		}

		ProgressBar pb = new ProgressBar(" " + args[0] + " -> " + args[1], nbPixels/*, ProgressBarStyle.ASCII*/);

		pb.start();
		for (int ii = 0; ii < nbPixels; ii++) {
			pb.setExtraMessage("Calcul de l'interet de chaque pixels.");
			int itr[][] = SeamCarving.interestColor(Objects.requireNonNull(table));
			pb.setExtraMessage("Calcul des pixels à supprimer........");
			ArrayList<Integer> dijkstra = SeamCarving.dijkstra(SeamCarving.toGraph(itr), 0, itr.length * itr[0].length + 1);

			pb.setExtraMessage("Suppression des pixels...............");
			table = SeamCarving.removePixelsColor(table, dijkstra);    // newImg
			pb.step();
		}

		pb.stepTo(nbPixels);
		pb.setExtraMessage("Ecriture du fichier " + args[1]);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		writeppm(table, args[1]);
		pb.setExtraMessage("Fin !");
		pb.stop();
	}

	private static void pgm(String[] args) {
		int table[][] = readpgm(args[0]);
		if (table == null || table.length <= 0 || table[0].length <= 4) {
			System.out.println(table == null);
			System.out.println(Objects.requireNonNull(table).length);
			System.out.println(table[0].length);
			System.out.println("L'image sélectionnée est trop petite !");
			System.exit(1);
		}

		int nbPixels = args.length > 2 ? Integer.parseInt(args[2]) : 50;

		if (table[0].length <= nbPixels) {
			System.out.println("L'image selectionnée n'est pas assez grande par rapport au nombre de pixel à retirer !");
			System.exit(2);
		}

		ProgressBar pb = new ProgressBar(" " + args[0] + " -> " + args[1], nbPixels/*, ProgressBarStyle.ASCII*/);

		pb.start();

		if (args.length > 3) {
			if (args[3].equals("energie")) {
				for (int ii = 0; ii < nbPixels; ii++) {
					pb.setExtraMessage("Calcul des pixels (energie) à supprimer........");
					ArrayList<Integer> dijkstra = dijkstra(toGraphEnergie(table), 0, table.length * table[0].length + 1);

					pb.setExtraMessage("Suppression des pixels...............");
					table = removePixels(table, dijkstra);    // newImg
					pb.step();
				}
			} else {
				for (int ii = 0; ii < nbPixels; ii++) {
					pb.setExtraMessage("Calcul de l'interet de chaque pixels.");
					int itr[][] = interest(table);
					pb.setExtraMessage("Calcul des pixels (twopath) à supprimer........");
					ArrayList<Integer>[] twopath = SeamCarving.twoPath(SeamCarving.toGraph2(itr), 0, (itr.length * itr[0].length + 2 + ((itr.length - 2) * itr[0].length)) - 1);

					pb.setExtraMessage("Suppression des pixels...............");
					table = SeamCarving.removePixels(table, twopath, true);    // newImg
					pb.step();
				}
			}
		} else {
			for (int ii = 0; ii < nbPixels; ii++) {
				pb.setExtraMessage("Calcul de l'interet de chaque pixels.");
				int itr[][] = interest(table);
				pb.setExtraMessage("Calcul des pixels à supprimer........");
				ArrayList<Integer> dijkstra = dijkstra(toGraph(itr), 0, itr.length * itr[0].length + 1);

				pb.setExtraMessage("Suppression des pixels...............");
				table = removePixels(table, dijkstra);    // newImg
				pb.step();
			}
		}


		pb.stepTo(nbPixels);
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
