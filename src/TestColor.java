import java.util.ArrayList;
import java.util.Objects;

public class TestColor {
	public static void main(String[] args) {
		int table[][][] = SeamCarving.readppm("jeandel.ppm");
		for (int i = 0; i < 200; i++) {
			int itr[][] = SeamCarving.interestColor(Objects.requireNonNull(table));
			ArrayList<Integer> dijkstra = SeamCarving.dijkstra(SeamCarving.toGraph(itr), 0, itr.length * itr[0].length + 1);
			//System.out.println(dijkstra);
			table = SeamCarving.removePixelsColor(table, dijkstra);	// newImg
		}
		SeamCarving.writeppm(table, "jeandelresize.ppm");
	}
}
