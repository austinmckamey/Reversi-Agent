import java.util.*;

class Node {

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

	double score;
	double alpha;
	double beta;

	public Node(Node parent, int me, int[][] state, int depth, int turn, int round, int numValidMoves, int[] validMoves, int move , double alpha, double beta) {
		this.parent = parent;
		this.me = me;
		this.state = state;
		this.depth = depth;
		this.turn = turn;
		this.round = round;
		this.numValidMoves = numValidMoves;
		this.validMoves = validMoves;
		this.move = move;
		this.alpha = alpha;
		this.beta = beta;
		int maxDepth = 6;
		if (round > 40) {
			maxDepth = 9;
		}
		if (depth < maxDepth) {
			makeChildren();
			pickChild();
		} else {
			calculateScore();
		}
	}

	private void calculateScore() {
		double center = calculateCenter();
		double count = calculateCount();
		double corners = calculateCorners();
		double edges = calculateEdges();
		double moves = calculateMoves();
		double sum = center + count + corners + edges + moves;
		center = center / sum;
		count = count / sum;
		corners = corners /sum;
		edges = edges /sum;
		moves = moves /sum;
		score =  (center * 50) + (count) + (corners * 1000) + (edges * 50) + (moves * 20);
		if (turn == me) {
        	if (score > parent.alpha) {
        		parent.alpha = score;
			}
		}
        else {
			if (score < parent.beta) {
				parent.beta = score;
			}
		}
	}
	private double calculateCenter() {
		double myCount = 0;
		double theirCount = 0;
		if (state[3][3] == me) {myCount++;} else if (state [3][3]!= 0) {theirCount++;}
		if (state[3][4] == me) {myCount++;} else if (state [3][4]!= 0) {theirCount++;}
		if (state[4][3] == me) {myCount++;} else if (state [4][3]!= 0) {theirCount++;}
		if (state[4][4] == me) {myCount++;} else if (state [4][4]!= 0) {theirCount++;}
		return (2 * myCount) - theirCount;
	}
	private double calculateCorners() {
		double myCount = 0;
		double theirCount = 0;
		if (state[0][0] == me) {myCount++;} else if (state [0][0]!= 0) {theirCount++;}
		if (state[0][7] == me) {myCount++;} else if (state [0][7]!= 0) {theirCount++;}
		if (state[7][0] == me) {myCount++;} else if (state [7][0]!= 0) {theirCount++;}
		if (state[7][7] == me) {myCount++;} else if (state [7][7]!= 0) {theirCount++;}
		return (2 * myCount) - theirCount;
	}
	private double calculateEdges() {
		double myCount = 0;
		double theirCount = 0;
		for (int i = 1; i < 7; i++) {if (state[0][i] == me) {myCount++;} else if (state [0][i]!= 0) {theirCount++;}}
		for (int i = 1; i < 7; i++) {if (state[i][0] == me) {myCount++;} else if (state [i][0]!= 0) {theirCount++;}}
		for (int i = 1; i < 7; i++) {if (state[i][7] == me) {myCount++;} else if (state [i][7]!= 0) {theirCount++;}}
		for (int i = 1; i < 7; i++) {if (state[7][i] == me) {myCount++;} else if (state [7][i]!= 0) {theirCount++;}}
		return (2 * myCount) - theirCount;
	}
	private double calculateMoves() {
		double mobility = 0;
		for(int i = 0; validMoves[i] != 0; i++) {mobility++;}
		return (3 * mobility);
	}
	private double calculateCount() {
		double myCount = 0;
		double theirCount = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (state[i][j] == me) {
					myCount++;
				} else if (state[i][j] != 0){
					theirCount++;
				}
			}
		}
		return (2 * myCount) - theirCount;
	}

	private void makeChildren() {
		int childrenTurn;
		if (turn == 1) {
			childrenTurn = 2;
		} else {
			childrenTurn = 1;
		}
		for (int i = 0; i < numValidMoves; i++) {
			if (alpha >= beta) {
				break;
			}
			int[][] childrenState = getChildrenState(validMoves[i], turn);
			int[] valid = getValidMoves(round+1, childrenState, childrenTurn);
			int num = getNumValidMoves(valid);
			children.add(new Node(this, me, childrenState, depth+1, childrenTurn, round+1, num, valid, validMoves[i], alpha, beta));
		}
	}

	private void pickChild() {
		double max = -1000;
		double min = 1000;
		Node child = null;
		if (this.turn == me) {
			for (Node node : children) {
				if (node.score > max) {
					max = node.score;
					child = node;
				}
			}
			if (alpha < max) {
				alpha = max;
				if (parent != null) {
					parent.beta = max;
				}
			}
		} else {
			for (Node node : children) {
				if (node.score < min) {
					min = node.score;
					child = node;
				}
			}
			if (beta > min) {
				beta = min;
				if (parent != null) {
					parent.alpha = min;
				}
			}
		}
		bestChild = child;
		if (child != null) {
			score = child.score;
		}
	}

	public int pickMove() {
		return bestChild.move;
	}

	private int[][] getChildrenState(int move, int turn) {
		int[][] child = new int[8][8];
		for (int i = 0; i < 8; i++) {
			System.arraycopy(state[i], 0, child[i], 0, 8);
		}
		changeColors(move / 8,move % 8, turn, child);
		child[move / 8][move % 8] = turn;
		return child;
	}

	public int[][] checkColorDirection(int row, int col, int incx, int incy, int turn, int[][] currState) {
		int sequence[] = new int[7];
		int seqLen;
		int i, r, c;

		seqLen = 0;
		for (i = 1; i < 8; i++) {
			r = row+incy*i;
			c = col+incx*i;

			if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
				break;

			sequence[seqLen] = currState[r][c];
			seqLen++;
		}

		int count = 0;
		for (i = 0; i < seqLen; i++) {
			if (turn == 1) {
				if (sequence[i] == 2)
					count ++;
				else {
					if ((sequence[i] == 1) && (count > 0))
						count = 20;
					break;
				}
			}
			else {
				if (sequence[i] == 1)
					count ++;
				else {
					if ((sequence[i] == 2) && (count > 0))
						count = 20;
					break;
				}
			}
		}

		if (count > 10) {
			if (turn == 1) {
				i = 1;
				r = row+incy*i;
				c = col+incx*i;
				while (currState[r][c] == 2) {
					currState[r][c] = 1;
					i++;
					r = row+incy*i;
					c = col+incx*i;
				}
			}
			else {
				i = 1;
				r = row+incy*i;
				c = col+incx*i;
				while (currState[r][c] == 1) {
					currState[r][c] = 2;
					i++;
					r = row+incy*i;
					c = col+incx*i;
				}
			}
		}
		return currState;
	}

	public int[][] changeColors(int row, int col, int turn, int[][] currState) {
		int incx, incy;

		for (incx = -1; incx < 2; incx++) {
			for (incy = -1; incy < 2; incy++) {
				if ((incx == 0) && (incy == 0))
					continue;

				currState = checkColorDirection(row, col, incx, incy, turn, currState);
			}
		}
		return currState;
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

	private int[] getValidMoves(int round, int[][] state, int currTurn) {
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
		}
		else {
			for (i = 0; i < 8; i++) {
				for (j = 0; j < 8; j++) {
					if (state[i][j] == 0) {
						if (couldBe(state, i, j, currTurn)) {
							valid[num] = i*8 + j;
							num ++;
						}
					}
				}
			}
		}
		return valid;
	}

	private boolean checkDirection(int[][] state, int row, int col, int incx, int incy, int currTurn) {
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
			if (currTurn == 1) {
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

	private boolean couldBe(int[][] state, int row, int col, int currTurn) {
		int incx, incy;
		for (incx = -1; incx < 2; incx++) {
			for (incy = -1; incy < 2; incy++) {
				if ((incx == 0) && (incy == 0))
					continue;

				if (checkDirection(state, row, col, incx, incy, currTurn))
					return true;
			}
		}
		return false;
	}
}