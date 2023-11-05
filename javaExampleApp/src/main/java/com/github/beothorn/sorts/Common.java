package com.github.beothorn.sorts;

public class Common {

    public static int[] swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return array;
    }

    public static int[] copyArray(int[] src, int[] dest){
        System.arraycopy(src, 0, dest, 0, dest.length);
        return dest;
    }

    public static int[] copyArray(int[] src, int startSrc,  int[] dest, int startDest, int howMany){
        System.arraycopy(src, startSrc, dest, startDest, howMany);
        return dest;
    }
}
