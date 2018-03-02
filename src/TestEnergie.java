import java.util.ArrayList;
import java.util.Objects;

public class TestEnergie {
	public static void main(String[] args) {
		int table[][] = SeamCarving.readpgm("jeandel.pgm");
		for (int i = 0; i < 200; i++) {
			ArrayList<Integer> dijkstra = SeamCarving.dijkstra(SeamCarving.toGraphEnergie(Objects.requireNonNull(table)),0, table.length * table[0].length + 1);
			//System.out.println(dijkstra);
			table = SeamCarving.removePixels(table, dijkstra);	// newImg
		}
		SeamCarving.writepgm(table, "jeandelEnergieResize.pgm");
	}
}
