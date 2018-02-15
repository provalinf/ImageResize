
public class TestSC {
	public static void main(String[] args) {
		int table[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}, {220, 40, 50, 30}};

		int itr2[][] = SeamCarving.interest(table);
		SeamCarving.toGraph(itr2).writeFile("test222ori.dot");
		SeamCarving.toGraph2(itr2).writeFile("test222double.dot");
	}
}
