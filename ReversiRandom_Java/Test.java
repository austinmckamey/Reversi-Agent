
public class Test {
    public static void main(String[] args) {
        int[][] state =  new int[8][8];
        test1();
    }
    public static void test1() {
        System.out.println("Starting Test");
        int[] valid = new int[64];
        valid[0] = 11;
        valid[1] = 13;
        valid[2] = 23;
        valid[3] = 25;
        valid[4] = 29;
        valid[5] = 30;
        valid[6] = 33;
        valid[7] = 34;
        valid[8] = 38;
        valid[9] = 43;
        int[][] state =
                {{0,0,0,2,0,0,0,0},
                 {0,0,0,0,2,0,0,0},
                 {0,0,2,1,1,1,1,0},
                 {0,0,1,2,1,0,0,0},
                 {0,0,0,1,2,1,0,0},
                 {0,0,1,0,0,0,1,0},
                 {0,0,0,0,0,0,0,0},
                 {0,0,0,0,0,0,0,0}};
//        int[][] state =
//                {{0,0,0,2,0,0,0,0},
//                 {0,0,0,0,2,0,0,0},
//                 {0,0,2,1,1,1,1,0},
//                 {0,0,1,2,1,0,0,0},
//                 {0,0,0,1,2,2,2,0},
//                 {0,0,1,0,0,0,1,0},
//                 {0,0,0,0,0,0,0,0},
//                 {0,0,0,0,0,0,0,0}};
        Node parent = new Node(null, 2, state, 0, 2, 15, 10, valid, 0, -10000,10000);
        int move = parent.pickMove();
        System.out.println("Move:");
        System.out.println(move);
    }
}
