
public class TestSC {
	public static void main(String[] args) {
		int table[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}, {220, 40, 50, 30}};
		int table2[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}};

		int itr2[][] = SeamCarving.interest(table2);
		//SeamCarving.toGraph(itr2).writeFile("test222ori.dot");
		//SeamCarving.toGraph2(itr2).writeFile("test222double.dot");
		SeamCarving.dijkstra2(SeamCarving.toGraph2(itr2), 0, itr2.length * itr2[0].length + 1);
		SeamCarving.twoPath(SeamCarving.toGraph2(itr2), 0, itr2.length * itr2[0].length + 1);
	}
}
