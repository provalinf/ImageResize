import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class TestSC {
    public static void main(String[] args) {
        //int table[][] = {{3, 11, 24, 39, 39}, {8, 21, 29, 39, 39}, {8, 21, 29, 39, 39}, {200, 60, 25, 0, 39}};
        int table2[][] = {{3, 11, 24, 39, 28, 14}, {8, 21, 29, 39, 56, 44}, {200, 60, 25, 0, 78, 23}};

        /*int itr2[][] = SeamCarving.interest(table2);
        SeamCarving.toGraph2(itr2).writeFile("test33.dot");
        System.exit(0);*/
        //SeamCarving.dijkstra2(SeamCarving.toGraph2(itr2), 0, itr2.length * itr2[0].length + 1);
        //System.out.println(SeamCarving.dijkstra2(SeamCarving.toGraph2(itr2), 0, (itr2.length * itr2[0].length + 2 + ((itr2.length - 2) * itr2[0].length)) - 1));
        //SeamCarving.toGraph2(itr2).writeFile("dij");
        //ArrayList<Integer>[] res = SeamCarving.twoPath(SeamCarving.toGraph2(itr2), 0, (itr2.length * itr2[0].length + 2 + ((itr2.length - 2) * itr2[0].length)) - 1);
        //SeamCarving.toGraph2(itr2).writeFile("test222double.dot");
/*        System.out.println(res[0]);
        System.out.println(res[1]);*/
        //SeamCarving.removeZero(itr2, SeamCarving.toGraph2(itr2));
        //System.out.println("La conversion : "+SeamCarving.convertNumNode0ToPixel(26, 5));
        table2 = SeamCarving.readpgm("ex1.pgm");
        for (int i = 0; i < 50; i++) {
			System.out.println(i);
            int itr[][] = SeamCarving.interest(table2);
			//SeamCarving.toGraph2(itr).writeFile("graph"+i+".dot");
            //ArrayList<Integer> dijkstra = SeamCarving.dijkstra2(SeamCarving.toGraph2(itr), 0, itr.length * itr[0].length + 1);
            ArrayList<Integer>[] twopath = SeamCarving.twoPath(SeamCarving.toGraph2(itr), 0, (itr.length * itr[0].length + 2 + ((itr.length - 2) * itr[0].length)) - 1);
			/*System.out.println(twopath[0]);
			System.out.println(twopath[1]);*/
            table2 = SeamCarving.removePixels(table2, twopath, true);	// newImg
        }
        SeamCarving.writepgm(table2, "ex12Resize.pgm");
    }
}
