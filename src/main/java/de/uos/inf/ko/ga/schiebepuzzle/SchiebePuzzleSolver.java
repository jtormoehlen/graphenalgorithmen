package de.uos.inf.ko.ga.schiebepuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class SchiebePuzzleSolver {

	public static void main(String[] args) {
		final int n = 3;
		
		/* generate random puzzle */
		final Puzzle start = generateRandomPuzzle(n); 

		System.out.println(start);

		/* test if the final state is reachable at all by performing DFS without any limit on the recursion */
		if (!depthFirstSearch(start)) {
			System.out.println("Problem is not solvable");
			return;
		}

		System.out.println("depth: " + breadthFirstSearch(start));

		final long timeStart = System.currentTimeMillis();
		for (int depth = 1; depth < 36; ++depth) {
			if (depthLimitedDepthFirstSearch(start, depth)) {
				System.out.println("found goal after " + depth + " steps");
				break;
			}
		}
		final long timeEnd = System.currentTimeMillis();
		
		System.out.println("Time: " + ((timeEnd - timeStart) / 1000.0) + "s");
	}
	
	/**
	 * Breadth-first search for finding the minimum number of moves required for solving a puzzle.
	 * @param start Starting configuration
	 * @return number of moves required for transforming the puzzle into the goal state, -1 if the goal state is not reachable
	 */
	private static int breadthFirstSearch(Puzzle start) {
		if (start.isGoal()) {
			return 0;
		}

		/* function that maps a state to its distance to the starting state */
		final Map<Puzzle, Integer> visited = new TreeMap<>();
		
		/* queue containing all states that need to be visited next */
		Queue<Puzzle> queue = new LinkedList<>();

		/* add the starting state to the queue */
		visited.put(start, 0);
		queue.add(start);
		
		while (!queue.isEmpty()) {
			final Puzzle current = queue.poll();
			final Integer depth = visited.get(current);

			/* iterate over all states that can be obtained from this state via a move of a single tile */
			for (int i = 0; i < current.getNumNeighbors(); ++i) {
				final Puzzle neighbor = current.getNeighbor(i);

				if (!visited.containsKey(neighbor)) {
					if (neighbor.isGoal()) {
						/* return if the goal has been found */
						return depth + 1;
					}

					visited.put(neighbor, depth + 1);
					queue.add(neighbor);
				}
			}
		}

		return -1;
	}

	/**
	 * Depth-limited depth-first search that tries to find a path from the starting state to the goal state within a bounded number of moves.
	 * This is a depth-first state that bounds the maximum height of the stack that is used for visiting the nodes of the graph. A node is
	 * seen as 'has-not-been-visited-yet' if it is not contained in the stack in that moment.
	 * @param start Starting state
	 * @param maxDepth maximum stack height
	 * @return true if the goal state is reachable within 'maxDepth' moves, false otherwise
	 */
	private static boolean depthLimitedDepthFirstSearch(Puzzle start, int maxDepth) {
		if (start.isGoal()) {
			return true;
		}

		Stack<StackElement> stack = new Stack<>();
		stack.push(new StackElement(start));

		System.out.println("starting dfs with max depth " + maxDepth);

		while (!stack.isEmpty()) {
			/* get the top element of the stack */
			final StackElement top = stack.peek();

			/* the maximum stack height has not been reached yet; we can move to unvisited vertices */
			if (stack.size() <= maxDepth) {
				if (top.nextNeighbor < top.numNeighbors) {
					/* there are still neighbors of this vertex that we might not have visited yet */
					final Puzzle next = top.puzzle.getNeighbor(top.nextNeighbor++);
					if (next.isGoal()) {
						return true;
					}

					/* only visit this vertex if it is not contained in the stack */
					if (!stack.contains((Object) next)) {
						stack.push(new StackElement(next));
					}
				} else {
					/* all neighbors of this vertex have been visited */
					stack.pop();
				}
			} else {
				/* maximum stack height has been reached */
				stack.pop();
			}
		}

		return false;
	}

	/**
	 * Depth-first search that determines whether the goal state is reachable from the starting state.
	 * @param start Starting state
	 * @return true if the goal state is reachable from the starting state, false otherwise
	 */
	private static boolean depthFirstSearch(Puzzle start) {
		if (start.isGoal()) {
			return true;
		}

		/* stack of current elements */
		Stack<StackElement> stack = new Stack<>();
		/* set of nodes that have been visited */
		Set<Puzzle> visited = new TreeSet<>();

		stack.push(new StackElement(start));
		visited.add(start);

		System.out.println("starting dfs");

		while (!stack.isEmpty()) {
			final StackElement top = stack.peek();

			if (top.nextNeighbor < top.numNeighbors) {
				final Puzzle next = top.puzzle.getNeighbor(top.nextNeighbor++);

				if (next.isGoal()) {
					return true;
				}

				if (!visited.contains((Object) next)) {
					stack.push(new StackElement(next));
					visited.add(next);
				}
			} else {
				stack.pop();
			}
		}
		
		return false;
	}

	private static Puzzle generateExample1() {
		Puzzle puzzle = new Puzzle(3);
		puzzle.setValue(0, 0, 1);
		puzzle.setValue(0, 1, 5);
		puzzle.setValue(0, 2, 8);
		puzzle.setValue(1, 0, 4);
		puzzle.setValue(1, 1, 0);
		puzzle.setValue(1, 2, 7);
		puzzle.setValue(2, 0, 3);
		puzzle.setValue(2, 1, 6);
		puzzle.setValue(2, 2, 2);
		return puzzle;
	}
	private static Puzzle generateSimplePuzzle(int n, int maxMoves, Random random) {
		Puzzle puzzle = new Puzzle(n);

		for (int x = 0; x < n; ++x) {
			for (int y = 0; y < n; ++y) {
				puzzle.setValue(x, y, x * n + y + 1);
			}
		}
		
		puzzle.setValue(n - 1, n - 1, 0);
		if (!puzzle.isGoal()) {
			throw new IllegalStateException("isGoal() returned wrong answer");
		}
		
		for (int i = 0; i < maxMoves; ++i) {
			final int nextNeighbor = Math.abs(random.nextInt()) % puzzle.getNumNeighbors();
			puzzle = puzzle.getNeighbor(nextNeighbor);
		}
		
		return puzzle;
	}

	private static Puzzle generateRandomPuzzle(int n) {
		final List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < n * n; ++i) {
			l.add(i);
		}

		Collections.shuffle(l);

		final Puzzle puzzle = new Puzzle(n);
		for (int x = 0; x < n; ++x) {
			for (int y = 0; y < n; ++y) {
				puzzle.setValue(x, y, l.get(x * n + y));
			}
		}
		
		return puzzle;
	}
	
	private static class StackElement {
		public final Puzzle puzzle;
		public int nextNeighbor;
		public int numNeighbors;
		
		public StackElement(Puzzle puzzle) {
			this.puzzle = puzzle;
			this.nextNeighbor = 0;
			this.numNeighbors = puzzle.getNumNeighbors();
		}
	}
}
