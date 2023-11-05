package com.github.beothorn.sorts.algorithms;

import com.github.beothorn.sorts.Common;

/**
 * This InplaceQuickSort implementation may call more functions than actually needed.
 * This is because I want the flamegraph to show exactly what is going on.
 */
public class InplaceQuickSort {

    public static int[] sort(int[] array) {
        int[] result = new int[array.length];
        Common.copyArray(array, result);
        sort(result, 0, result.length - 1);
        return result;
    }

    private static void sort(int[] array, int left, int right) {
        if (right > left) {
            int indexForBiggerThenPivotOnTheLeft = left;
            int indexForSmallerThanPivotOnTheRight = right;

            int pivotValue =  getPivotAt(right, array);

            do {
                indexForBiggerThenPivotOnTheLeft = findNextValueBiggerOrEqualThanPivotOnLeft(pivotValue, indexForBiggerThenPivotOnTheLeft, array);
                indexForSmallerThanPivotOnTheRight = findNextValueSmallerOrEqualThanPivotOnRight(pivotValue, indexForSmallerThanPivotOnTheRight, array);

                if (indexForBiggerThenPivotOnTheLeft <= indexForSmallerThanPivotOnTheRight) {
                    // Smaller than pivot on the left (including pivot), bigger than pivot on the right
                    Common.swap(array, indexForBiggerThenPivotOnTheLeft, indexForSmallerThanPivotOnTheRight);
                    indexForBiggerThenPivotOnTheLeft++;
                    indexForSmallerThanPivotOnTheRight--;
                }
            } while (indexForBiggerThenPivotOnTheLeft <= indexForSmallerThanPivotOnTheRight);

            if (left < indexForSmallerThanPivotOnTheRight) sort(array, left, indexForSmallerThanPivotOnTheRight);
            if (indexForBiggerThenPivotOnTheLeft < right) sort(array, indexForBiggerThenPivotOnTheLeft, right);
        }
    }

    private static int findNextValueBiggerOrEqualThanPivotOnLeft(
        int pivot,
        int startIndex,
        int[] array
    ){
        int index = startIndex;
        while (array[index] < pivot) index++;
        return index;
    }

    private static int findNextValueSmallerOrEqualThanPivotOnRight(
            int pivot,
            int startIndex,
            int[] array
    ){
        int index = startIndex;
        while (array[index] > pivot) index--;
        return index;
    }

    private static int getPivotAt(int index, int[] array){
        return array[index];
    }
}
