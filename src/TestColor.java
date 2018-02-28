import java.util.ArrayList;

public class TestColor {
	public static void main(String[] args) {
		int table[][][] = SeamCarving.readppm("jeandel.ppm");
		for (int i = 0; i < 200; i++) {
			int itr[][] = SeamCarving.interestColor(table);
			ArrayList<Integer> dijkstra = SeamCarving.dijkstra(SeamCarving.toGraph(itr), 0, itr.length * itr[0].length + 1);
			//System.out.println(dijkstra);
			int[][][] newImg = SeamCarving.removePixelsColor(table, dijkstra);
			table = newImg;
		}
		SeamCarving.writeppm(table, "jeandelresize.ppm");
	}
}
