package com.github.beothorn.sorts.algorithms;

import com.github.beothorn.sorts.Common;

/**
 * This BubbleSort implementation may call more functions than actually needed.
 * This is because I want the flamegraph to show exactly what is going on.
 */
public class BubbleSort {

    public static int[] sort(int[] array) {
        int[] result = new int[array.length];
        Common.copyArray(array, result);
        for (int i = array.length - 1; i > 0; i--) {
            boolean thereWasASwapping = bubbleUp(i, result);
            boolean alreadyOrdered = !thereWasASwapping;
            if (alreadyOrdered) break;
        }
        return result;
    }

    private static boolean isABiggerThanB(int a, int b){
        return a > b;
    }

    private static boolean swapIfBiggerThanNext(int cursorPosition, int[] array) {
        if ( !isABiggerThanB(array[cursorPosition], array[cursorPosition+1]) ) return false;
        Common.swap(array, cursorPosition, cursorPosition + 1);
        return true;
    }

    private static boolean bubbleUp(int i, int[] result) {
        boolean swapped = false;
        for (int cursor = 0; cursor < i; cursor++) {
            swapped = swapIfBiggerThanNext(cursor, result) || swapped;
        }
        return swapped;
    }
}
