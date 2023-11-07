package com.github.beothorn.sorts.algorithms;

import com.github.beothorn.sorts.Common;

/**
 * This InsertionSort implementation may call more functions than actually needed.
 * This is because I want the flamegraph to show exactly what is going on.
 * On insertion sort, there is a headCursor and a pushForwardCursor.
 * The headCursor will start at the beginning of the array and go towards the end.
 * The value of the headCursor will move down the array (right to left) until it finds
 * a spot where the previous value is smaller than the head value.
 * On the pushForwardCursor, we compare the value at the pushForwardCursor with the value to the left
 * and if the lef is bigger, we swap the two values, or using a different description, we push the left value
 * forward to give space to the right value to be inserted.
 * Because we start pushing from the head, the right value on this comparison will always be the head value.
 * Since it goes from left to right, it is guaranteed that when we reach the point where the headValue
 * can be inserted, the list from that point to the left is sorted.
 * I think this algorithm can be though as an inverted bubble sort.
 */
public class InsertionSort {

    public static int[] sort(int[] array) {
        int[] result = new int[array.length];
        Common.copyArray(array, result);

        int headCursor = 1;
        while (headCursor < result.length) {
            int pushForwardCursor = headCursor;
            while (valueAtPushForwardCursorIsUnordered(pushForwardCursor, result)) {
                // headCursor value goes towards the bottom
                Common.swap(result, pushForwardCursor, pushForwardCursor - 1);
                pushForwardCursor = movePushForwardCursorDown(pushForwardCursor);
            }
            headCursor = moveHeadCursorUp(headCursor);
        }
        return result;
    }

    private static boolean valueAtPushForwardCursorIsUnordered(int pushForwardCursor, int[] result) {
        return pushForwardCursor > 0 && result[pushForwardCursor - 1] > result[pushForwardCursor];
    }

    private static int moveHeadCursorUp(int headCursor) {
        return headCursor + 1;
    }

    private static int movePushForwardCursorDown(int pushForwardCursor) {
        return pushForwardCursor - 1;
    }
}
