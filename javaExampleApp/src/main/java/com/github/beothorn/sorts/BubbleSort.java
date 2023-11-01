package com.github.beothorn.sorts;

public class BubbleSort {

    static void bubbleSort(int arr[], int n)
    {
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {

                    // Swap arr[j] and arr[j+1]
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            // If no two elements were
            // swapped by inner loop, then break
            if (swapped == false)
                break;
        }
    }

    private static boolean swapIfBiggerThanNext(int[] array, int i) {
        if (array[i] <= array[i + 1]) return false;
        int temp = array[i];
        array[i] = array[i+1];
        array[i+1] = temp;
        return true;
    }

    public static int[] sort(int[] array) {
        boolean swapped;
        int arrayLastIndex = array.length - 1;
        // copy array to result
        int[] result = new int[array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        for (int i = array.length - 1; i > 0; i--) {
            swapped = false;
            for (int cursor = 0; cursor < i; cursor++) {
                swapped = swapIfBiggerThanNext(result, cursor);
            }
            if (!swapped) break;
        }
        return result;
    }
}
