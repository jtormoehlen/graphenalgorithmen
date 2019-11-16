package de.uos.inf.ko.ga.schiebepuzzle;

import java.util.Arrays;

/**
 * State of the n-times-n puzzle.
 * The values of the fields are stored in a one-dimensional array.
 * The position (X, Y) is interpreted as (row X, column Y) and is stored
 * at position (X * n + y) inside the one-dimensional array.
 * @author Tobias Oelschlägel
 *
 */
public class Puzzle implements Comparable<Puzzle> {

	private final int n;
	private final int[] values;
	private int emptyX;
	private int emptyY;

	/**
	 * Initializes a state.
	 * The resulting state is not feasible since the 'values' are not feasible.
	 * The empty slot is considered to be at the bottom right.
	 * @param n Dimension of the puzzle
	 */
	public Puzzle(int n) {
		this.n = n;
		this.values = new int[n * n];
		this.emptyX = n - 1;
		this.emptyY = n - 1;
	}

	/**
	 * Initializes a state by copying another state.
	 * @param puzzle Puzzle to be copied
	 */
	public Puzzle(Puzzle puzzle) {
		this.n = puzzle.n;
		this.values = Arrays.copyOf(puzzle.values, puzzle.values.length);
		this.emptyX = puzzle.emptyX;
		this.emptyY = puzzle.emptyY;
	}

	/**
	 * Updates the value of a certain field.
	 * This method does not take care of duplicates.
	 * @param x x position
	 * @param y y position
	 * @param value new value to be inserted
	 */
	public void setValue(int x, int y, int value) {
		this.values[x * n + y] = value;

		if (value == 0) {
			this.emptyX = x;
			this.emptyY = y;
		}
	}

	/**
	 * Returns the value of a certain field.
	 * @param x x position
	 * @param y y position
	 * @return value of the field at the position
	 */
	public int getValue(int x, int y) {
		return this.values[x * n + y];
	}

	/**
	 * Tests whether the state is the goal state.
	 * The state is the goal state if the empty slot is at the bottom right
	 * and the remaining values are sorted in ascending order from top to bottom and left to right.
	 * @return true if the state is the goal state, false otherwise
	 */
	public boolean isGoal() {
		final int size = this.n * this.n;
		
		if ((this.emptyX != this.n - 1) || (this.emptyY != this.n - 1)) {
			// empty slot is not at the bottom right
			return false;
		}

		for (int i = 0; i < size - 1; ++i) {
			if (this.values[i] != i + 1) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Determines the number of neighboring states.
	 * @return number of states that can be obtained by swapping a field with the empty slot
	 */
	public int getNumNeighbors() {
		final int left = (this.emptyY > 0) ? 1 : 0; /* can only move left if the empty slot is not at the left border */
		final int right = (this.emptyY < this.n - 1) ? 1 : 0; /* can only move right if the empty slot is not at the right border */
		final int top = (this.emptyX > 0) ? 1 : 0;
		final int bottom = (this.emptyX < this.n - 1) ? 1 : 0;
		
		return left + right + top + bottom;
	}
	
	/**
	 * Gets a neighbor of the current state
	 * @param idx index of the neighbor to be constructed
	 * @return the state of the neighbor
	 */
	public Puzzle getNeighbor(int idx) {
		final int origIdx = idx;
		final boolean left = (this.emptyY > 0);
		final boolean right = (this.emptyY < this.n - 1);
		final boolean top = (this.emptyX > 0);
		final boolean bottom = (this.emptyX < this.n - 1);

		if (left) {
			if (idx-- == 0) {
				// move space to the left
				final Puzzle neighbor = new Puzzle(this);
				neighbor.setValue(this.emptyX, this.emptyY, this.getValue(this.emptyX, this.emptyY - 1));
				neighbor.setValue(this.emptyX, this.emptyY - 1, 0);
				return neighbor;
			}
		}
		
		if (right) {
			if (idx-- == 0) {
				// move space to the right
				final Puzzle neighbor = new Puzzle(this);
				neighbor.setValue(this.emptyX, this.emptyY, this.getValue(this.emptyX, this.emptyY + 1));
				neighbor.setValue(this.emptyX, this.emptyY + 1, 0);
				return neighbor;
			}
		}
		
		if (top) {
			if (idx-- == 0) {
				// move space to the top
				final Puzzle neighbor = new Puzzle(this);
				neighbor.setValue(this.emptyX, this.emptyY, this.getValue(this.emptyX - 1, this.emptyY));
				neighbor.setValue(this.emptyX - 1, this.emptyY, 0);
				return neighbor;
			}
		}
		
		if (bottom) {
			// move space to the bottom
			final Puzzle neighbor = new Puzzle(this);
			neighbor.setValue(this.emptyX, this.emptyY, this.getValue(this.emptyX + 1, this.emptyY));
			neighbor.setValue(this.emptyX + 1, this.emptyY, 0);
			return neighbor;
		}
		
		throw new IllegalArgumentException("could not determine neighbor: " + origIdx + ", " + this);
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int x = 0; x < this.n; x++) {
			for (int y = 0; y < this.n; y++) {
				final int val = this.getValue(x, y);
				if (val == 0) {
					s.append("- ");
				} else {
					s.append(val + " ");
				}
			}
			s.append('\n');
		}

		return s.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(this.values);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Puzzle)) {
			return false;
		}

		return Arrays.equals(this.values, ((Puzzle) obj).values);
	}

	@Override
	public int compareTo(Puzzle puzzle) {
		for (int i = 0; i < this.n * this.n; ++i) {
			if (this.values[i] < puzzle.values[i]) {
				return -1;
			} else if (this.values[i] > puzzle.values[i]) {
				return 1;
			}
		}

		return 0;
	}
}
