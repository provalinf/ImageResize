import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TestSC {
    public static void main(String[] args) {
        int table[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}, {220, 40, 50, 30}};
        int table2[][] = {{3, 11, 24, 39}, {8, 21, 29, 39}, {200, 60, 25, 0}};

        int itr2[][] = SeamCarving.interest(table2);
        //SeamCarving.toGraph(itr2).writeFile("test222ori.dot");
        //SeamCarving.dijkstra2(SeamCarving.toGraph2(itr2), 0, itr2.length * itr2[0].length + 1);
        System.out.println(SeamCarving.dijkstra2(SeamCarving.toGraph2(itr2), 0, (itr2.length * itr2[0].length + 2 + ((itr2.length - 2) * itr2[0].length)) - 1));
        //SeamCarving.toGraph2(itr2).writeFile("dij");
        ArrayList<Integer>[] res = SeamCarving.twoPath(SeamCarving.toGraph2(itr2), 0, (itr2.length * itr2[0].length + 2 + ((itr2.length - 2) * itr2[0].length)) - 1);
        SeamCarving.toGraph2(itr2).writeFile("test222double.dot");
/*        System.out.println(res[0]);
        System.out.println(res[1]);*/
        //SeamCarving.removeZero(itr2, SeamCarving.toGraph2(itr2));
        //System.out.println("La conversion : "+SeamCarving.convertNumNode0ToPixel(26, 5));
    }
}
