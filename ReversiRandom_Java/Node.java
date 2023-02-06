import java.util.*;

class Node {

	Node parent;
	List<Node> children = new ArrayList();
	int depth;

    int me;
    int boardState;
    int state[][] = new int[8][8];
    int turn = -1;
    int round;
    
    int validMoves[] = new int[64];
    int numValidMoves;

	int score;

	public Node(Node parent, int me, int boardState, int depth) {
		this.parent = parent;
		this.me = me;
		this.boardState = boardState;
		this.depth = depth;
		calculateScore();
		if (depth < 8) {
			makeChildren();
		}
	}

	private void calculateScore() {
		int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (state[i][j] == me)
                    count++;
            }
        }
		this.score = count;
	}

	private void makeChildren() {
		for (int i = 0; i < numValidMoves; i++) {
			children.add(new Node(this, me, boardState, depth+1));
		}
	}

}