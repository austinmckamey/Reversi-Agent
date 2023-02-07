
public class Test {
    public static void main(String[] args) {
        int[][] state =  new int[8][8];
        test1();
    }
    public static void test1() {
        System.out.println("Starting Test");
        int[] valid = new int[64];
        valid[0] = 19;
        valid[1] = 25;
        valid[2] = 26;
        valid[3] = 30;
        valid[4] = 33;
        valid[5] = 37;
        valid[6] = 38;
        valid[7] = 47;
        valid[8] = 51;
        valid[9] = 53;
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
        Node parent = new Node(null, 2, state, 0, 2, 15, 10, valid, 0);
        int move = parent.pickMove();
        System.out.println("Move:");
        System.out.println(move);
    }
}
