import java.util.*;

class Node {

	public static void main(String[] args) {
		int[] valid = new int[64];
		valid[0] = 27;
		valid[1] = 28;
		valid[2] = 35;
		valid[3] = 36;
		Node parent = new Node(null, 1, new int[8][8], 0, 1, 0, 4, valid, 0);
		int move = parent.pickMove();
	}

	Node parent;
	List<Node> children = new ArrayList();
	Node bestChild;
	int depth;

    int me;
    int[][] state;
    int turn;
    int round;
    
    int[] validMoves;
    int numValidMoves;
	int move;

	int score;
	int alpha;
	int beta;

	public Node(Node parent, int me, int[][] state, int depth, int turn, int round, int numValidMoves, int[] validMoves, int move) {
		this.parent = parent;
		this.me = me;
		this.state = state;
		this.depth = depth;
		this.turn = turn;
		this.round = round;
		this.numValidMoves = numValidMoves;
		this.validMoves = validMoves;
		this.move = move;
		calculateScore();
		if (depth < 8) {
			makeChildren();
		}
		pickBestChild();
	}

	private void calculateScore() {
		int myCount = 0;
		int theirCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
				if (state[i][j] == me) {
					myCount++;
				} else if (state[i][j] != 0){
					theirCount++;
				}
            }
        }
		if(turn == me) {
			score = myCount - theirCount;
		} else {
			score = theirCount - myCount;
		}
	}

	private void makeChildren() {
		int childrenTurn;
		if (turn == 1) {
			childrenTurn = 2;
		} else {
			childrenTurn = 1;
		}
		for (int i = 0; i < numValidMoves; i++) {
			int[][] childrenState = getChildrenState(validMoves[i], childrenTurn);
			int[] valid = getValidMoves(round+1, childrenState);
			int num = getNumValidMoves(valid);
			children.add(new Node(this, me, childrenState, depth+1, childrenTurn, round+1, num, valid, validMoves[i]));
		}
	}

	private int[][] getChildrenState(int move, int turn) {
		int[][] child = new int[8][8];
		for (int i = 0; i < 8; i++) {
			System.arraycopy(state[i], 0, child[i], 0, 8);
		}
		child[move / 8][move % 8] = turn;
		return child;
	}

	private int getNumValidMoves(int[] valid) {
		int length = 0;
		for (int i : valid) {
			if (i == 0) {
				break;
			}
			length++;
		}
		return length;
	}

	private int[] getValidMoves(int round, int[][] state) {
		int i, j;
		int[] valid = new int[64];
		int num = 0;
		if (round < 4) {
			if (state[3][3] == 0) {
				valid[num] = 3*8 + 3;
				num ++;
			}
			if (state[3][4] == 0) {
				valid[num] = 3*8 + 4;
				num ++;
			}
			if (state[4][3] == 0) {
				valid[num] = 4*8 + 3;
				num ++;
			}
			if (state[4][4] == 0) {
				valid[num] = 4*8 + 4;
				num ++;
			}
			System.out.println("Valid Moves:");
			for (i = 0; i < num; i++) {
				System.out.println(valid[i] / 8 + ", " + valid[i] % 8);
			}
		}
		else {
			System.out.println("Valid Moves:");
			for (i = 0; i < 8; i++) {
				for (j = 0; j < 8; j++) {
					if (state[i][j] == 0) {
						if (couldBe(state, i, j)) {
							valid[num] = i*8 + j;
							num ++;
							System.out.println(i + ", " + j);
						}
					}
				}
			}
		}
		return valid;
	}

	private boolean checkDirection(int[][] state, int row, int col, int incx, int incy) {
		int[] sequence = new int[7];
		int seqLen;
		int i, r, c;
		seqLen = 0;
		for (i = 1; i < 8; i++) {
			r = row+incy*i;
			c = col+incx*i;

			if ((r < 0) || (r > 7) || (c < 0) || (c > 7)) {
				break;
			}

			sequence[seqLen] = state[r][c];
			seqLen++;
		}
		int count = 0;
		for (i = 0; i < seqLen; i++) {
			if (me == 1) {
				if (sequence[i] == 2) {
					count++;
				} else {
					if ((sequence[i] == 1) && (count > 0)) {
						return true;
					}
					break;
				}
			}
			else {
				if (sequence[i] == 1) {
					count++;
				} else {
					if ((sequence[i] == 2) && (count > 0)) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	private boolean couldBe(int[][] state, int row, int col) {
		int incx, incy;
		for (incx = -1; incx < 2; incx++) {
			for (incy = -1; incy < 2; incy++) {
				if ((incx == 0) && (incy == 0))
					continue;

				if (checkDirection(state, row, col, incx, incy))
					return true;
			}
		}
		return false;
	}

	private void pickBestChild() {
		int max = -1;
		Node child = null;
		for (Node node : children) {
			if (node.score > max) {
				max = node.score;
				child = node;
			}
		}
		bestChild = child;
	}

	public int pickMove() {
		return bestChild.move;
	}
}