package com.github.beothorn.sorts.algorithms;

import com.github.beothorn.sorts.Common;

/**
 * This InplaceQuickSort implementation may call more functions than actually needed.
 * This is because I want the flamegraph to show exactly what is going on.
 * Quicksort works by choosing a pivot (a value to divide the array into two parts,
 * bigger and smaller or equal the pivot). Do it repeatedly until the last cases are two or three
 * items that will trivial to sort.
 * First step is to choose a pivot. This algorithm chooses the last item as the pivot.
 * That is a terrible choice if the array is already sorted but let's go with it.
 * Then we store the pivot value. Now we want all values smaller than the pivot to be on the left
 * and all values bigger than the pivot to be on the right.
 * We don't know how many items are smaller than the pivot, but it doesn't matter as we will
 * have two cursors, one on the left and one on the right.
 * The left cursor will traverse the array from left to right, searching for the next value that is
 * bigger than the pivot. The right cursor will traverse the array from right to left, searching for
 * the values that are smaller than the pivot.
 * On each step we just swap the values on the left cursor and the right cursor and then move them
 * to the next value (bigger on the left, smaller or equal on the right).
 * One gotcha is that the last item with the pivot is not special. Because our comparison is smaller or equals
 * goes to the right, after we have our pivot value we don't care from which index it came from.
 * We just sort it together with all the others.
 * When both cursors meet we know that at that point, all values smaller than the pivot were swapped
 * to the left and all values bigger than the pivot were swapped to the right.
 * Now we can have two sublists to sort (left from the pivot and right from the pivot).
 * We just redo the process on each sublist.
 * Eventuallly we reach the case where sublists have size 2.
 * We only call the sort function if the cursors moved.
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
