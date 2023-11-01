package com.github.beothorn.sorts;

public class BubbleSort {

    private static boolean isABiggerThanB(int a, int b){
        return a > b;
    }

    private static boolean swapIfBiggerThanNext(int cursorPosition, int[] array) {
        if ( !isABiggerThanB(array[cursorPosition], array[cursorPosition+1]) ) return false;
        int temp = array[cursorPosition];
        array[cursorPosition] = array[cursorPosition+1];
        array[cursorPosition+1] = temp;
        return true;
    }

    public static int[] sort(int[] array) {
        int[] result = new int[array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        for (int i = array.length - 1; i > 0; i--) {
            boolean thereWasASwapping = bubbleUp(i, result);
            boolean alreadyOrdered = !thereWasASwapping;
            if (alreadyOrdered) break;
        }
        return result;
    }

    private static boolean bubbleUp(int i, int[] result) {
        boolean swapped = false;
        for (int cursor = 0; cursor < i; cursor++) {
            swapped = swapIfBiggerThanNext(cursor, result) || swapped;
        }
        return swapped;
    }
}
