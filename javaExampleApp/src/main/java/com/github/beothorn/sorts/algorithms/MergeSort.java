package com.github.beothorn.sorts.algorithms;

import com.github.beothorn.sorts.Common;

public class MergeSort {

    public static int[] sort(int[] array) {
        int[] result = new int[array.length];
        Common.copyArray(array, result);
        return MergeSort.topDownMergeSort(result);
    }

    private static int[] topDownMergeSort(int[] result) {
        int[] resultCopy = new int[result.length];
        Common.copyArray(result, resultCopy);
        topDownSplitMerge(result, 0, resultCopy.length, resultCopy);
        return result;
    }

    private static int[] topDownSplitMerge(
        int[] arrayB,
        int start,
        int end,
        int[] arrayA
    ) {
        if (end - start <= 1) return null;
        int middle = (end + start) / 2;
        topDownSplitMerge(arrayA, start, middle, arrayB);
        topDownSplitMerge(arrayA, middle, end, arrayB);
        topDownMerge(arrayB, start, middle, end, arrayA);
        return arrayB;
    }

    private static int[] topDownMerge(
        int[] result,
        int start,
        int middle,
        int end,
        int[] origin
    ) {
        int leftCursor = start; // left segment is [start, middle)
        int rightCursor = middle; // right segment is [middle, end)
        int copyCursor = start; // cursor will copy from origin to result, from start to end

        // While we have not reached the end of either segment
        while (leftCursor < middle && rightCursor < end) {
            if(isLeftSmallerOrEquals(origin[leftCursor], origin[rightCursor])){
                // Copy from left segment, advance left cursor
                copyToResult(result, copyCursor, origin, leftCursor);
                leftCursor = leftCursor + 1;
            } else {
                // Copy from right segment, advance right cursor
                copyToResult(result, copyCursor, origin, rightCursor);
                rightCursor = rightCursor + 1;
            }
            copyCursor++;
        }

        // If we copied a whole segment already, we just need to copy the rest of the other segment
        boolean leftWasFullyCopied = leftCursor == middle;
        if (leftWasFullyCopied) {
            // Then we copy what is left of the right segment
            Common.copyArray(
                origin,
                rightCursor,
                result,
                copyCursor,
                end - rightCursor
            );
        } else {
            // Then we copy what is left of the left segment
            Common.copyArray(
                origin,
                leftCursor,
                result,
                copyCursor,
                middle - leftCursor
            );
        }

        return result;
    }

    private static boolean isLeftSmallerOrEquals(int a, int b) {
        return a <= b;
    }

    private static int[] copyToResult(
        int[] result,
        int destinationIndex,
        int[] origin,
        int originIndex
    ) {
        result[destinationIndex] = origin[originIndex];
        return result;
    }

}
