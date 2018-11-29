package com.prince.algo;

/**
 * Tower of Hanoi puzzle
 *
 * It consists of three rods and a number of disks of different sizes, which can slide onto any rod.
 * The puzzle starts with the disks in a neat stack in ascending order of size on one rod, the
 * smallest at the top, thus making a conical shape.
 *
 * The objective of the puzzle is to move the entire stack to another rod, obeying the following
 * simple rules:
 *
 * <pre>
 * 1. Only one disk can be moved at a time.
 * 2. Each move consists of taking the upper disk from one of the stacks and placing it on top of another stack or on an empty rod.
 * 3. No disk may be placed on top of a smaller disk.
 * </pre>
 *
 * @author Prince Raj
 */
public class TowerOfHanoi {

    public static void main(String[] args) {
        towerOfHanoi(3, "A", "B", "C");
    }

    private static void towerOfHanoi(int n, String from, String aux, String to) {

        // only one disk left
        if (n == 1) {
            System.out.printf("Move %d disk from %s peg to %s peg\n", 1, from, to);
            return;
        }

        towerOfHanoi(n - 1, from, to, aux);

        System.out.printf("Move %d disk from %s peg to %s peg\n", n, from, to);

        towerOfHanoi(n - 1, aux, from, to);
    }
}
