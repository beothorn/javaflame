var executionMetadata = "<p>Flags: []</p><p>Output: '/tmp/1698841501345_snap'</p><p>Excludes: [com.github.junrar.unpack]</p><p>Filters: []</p>";
var data = [[
{"thread":"BubbleSort","snapshotTime":1698841502498,"span":{"name":"BubbleSortRoot","entryTime":1698841502463,"exitTime":-1,"value":0,"children":[{"name":"sort(int[] arg0 = [10, 14, 7, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]","entryTime":1698841502463,"exitTime":1698841502491,"value":28,"children":[{"name":"bubbleUp(int arg0 = 19, int[] arg1 = [10, 14, 7, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502464,"exitTime":1698841502474,"value":10,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [10, 14, 7, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => false","entryTime":1698841502464,"exitTime":1698841502473,"value":9,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 14) => false","entryTime":1698841502464,"exitTime":1698841502464,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [10, 14, 7, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 7) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [10, 7, 14, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => false","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 16) => false","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [10, 7, 14, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => false","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 18) => false","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [10, 7, 14, 16, 18, 11, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 11) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [10, 7, 14, 16, 11, 18, 8, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 8) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [10, 7, 14, 16, 11, 8, 18, 5, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 5) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 18, 17, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 17) => true","entryTime":1698841502473,"exitTime":1698841502473,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 18, 6, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 6) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 18, 12, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 12) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => false","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 20) => false","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 20, 1, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 1) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 20, 9, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 9) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 13, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 20, 19, 3, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 19) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 14, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 20, 3, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 3) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 15, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 20, 4, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 4) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 16, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 20, 2, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 2) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 17, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 20, 13, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 13) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 18, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 20, 15]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 20, int arg1 = 15) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 18, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502474,"exitTime":1698841502476,"value":2,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [10, 7, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 7) => true","entryTime":1698841502474,"exitTime":1698841502474,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [7, 10, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 14) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [7, 10, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 16) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [7, 10, 14, 16, 11, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 11) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [7, 10, 14, 11, 16, 8, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 8) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [7, 10, 14, 11, 8, 16, 5, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 5) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 17) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 17, 6, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 6) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 17, 12, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 12) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 18) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 18, 1, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 1) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 18, 9, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 9) => true","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 19, 3, 4, 2, 13, 15, 20]) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 19) => false","entryTime":1698841502475,"exitTime":1698841502475,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 13, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 19, 3, 4, 2, 13, 15, 20]) => true","entryTime":1698841502475,"exitTime":1698841502476,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 19, int arg1 = 3) => true","entryTime":1698841502475,"exitTime":1698841502476,"value":1}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 14, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 19, 4, 2, 13, 15, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 19, int arg1 = 4) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 15, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 19, 2, 13, 15, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 19, int arg1 = 2) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 16, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 19, 13, 15, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 19, int arg1 = 13) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 17, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 19, 15, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 19, int arg1 = 15) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 17, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502476,"exitTime":1698841502477,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => false","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 10) => false","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => false","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 14) => false","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [7, 10, 14, 11, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 11) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [7, 10, 11, 14, 8, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 8) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [7, 10, 11, 8, 14, 5, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 5) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [7, 10, 11, 8, 5, 14, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => false","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 16) => false","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [7, 10, 11, 8, 5, 14, 16, 6, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 6) => true","entryTime":1698841502476,"exitTime":1698841502476,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 16, 12, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502476,"exitTime":1698841502477,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 12) => true","entryTime":1698841502476,"exitTime":1698841502477,"value":1}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 17) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 17, 1, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 1) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 17, 9, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 9) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 18, 3, 4, 2, 13, 15, 19, 20]) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 18) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 18, 3, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 3) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 13, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 18, 4, 2, 13, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 4) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 14, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 18, 2, 13, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 2) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 15, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 18, 13, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 13) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 16, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 18, 15, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 18, int arg1 = 15) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 16, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502478,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 10) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [7, 10, 11, 8, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 8) => true","entryTime":1698841502477,"exitTime":1698841502477,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [7, 10, 8, 11, 5, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 5) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [7, 10, 8, 5, 11, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => false","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 14) => false","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [7, 10, 8, 5, 11, 14, 6, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 6) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [7, 10, 8, 5, 11, 6, 14, 12, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 12) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => false","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 16) => false","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 16, 1, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 1) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 16, 9, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 9) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => false","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 17) => false","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 17, 3, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 3) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 17, 4, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 4) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 13, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 17, 2, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 2) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 14, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 17, 13, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 13) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 15, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 17, 15, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 17, int arg1 = 15) => true","entryTime":1698841502478,"exitTime":1698841502478,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 15, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502478,"exitTime":1698841502480,"value":2,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 10) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [7, 10, 8, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 8) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [7, 8, 10, 5, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 5) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [7, 8, 5, 10, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [7, 8, 5, 10, 11, 6, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 6) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 12) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 14) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 14, 1, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 1) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 14, 9, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 9) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 16) => false","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 16, 3, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 3) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 16, 4, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 4) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 4, 16, 2, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 2) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 13, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 16, 13, 15, 17, 18, 19, 20]) => true","entryTime":1698841502479,"exitTime":1698841502480,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 13) => true","entryTime":1698841502479,"exitTime":1698841502479,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 14, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 13, 16, 15, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 16, int arg1 = 15) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 14, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502481,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [7, 8, 5, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 5) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [7, 5, 8, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 10) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [7, 5, 8, 10, 6, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 6) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [7, 5, 8, 6, 10, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [7, 5, 8, 6, 10, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 12) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [7, 5, 8, 6, 10, 11, 12, 1, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 1) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 12, 9, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 9) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 14) => false","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 14, 3, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 3) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 3, 14, 4, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 4) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 3, 4, 14, 2, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 2) => true","entryTime":1698841502480,"exitTime":1698841502480,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 3, 4, 2, 14, 13, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502481,"exitTime":1698841502481,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 13) => true","entryTime":1698841502481,"exitTime":1698841502481,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 13, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502481,"exitTime":1698841502481,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 14, int arg1 = 15) => false","entryTime":1698841502481,"exitTime":1698841502481,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 13, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502481,"exitTime":1698841502485,"value":4,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [7, 5, 8, 6, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502481,"exitTime":1698841502481,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 5) => true","entryTime":1698841502481,"exitTime":1698841502481,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [5, 7, 8, 6, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502481,"exitTime":1698841502481,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502481,"exitTime":1698841502481,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [5, 7, 8, 6, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502481,"exitTime":1698841502481,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 6) => true","entryTime":1698841502481,"exitTime":1698841502481,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [5, 7, 6, 8, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502481,"exitTime":1698841502482,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 10) => false","entryTime":1698841502481,"exitTime":1698841502482,"value":1}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [5, 7, 6, 8, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502482,"exitTime":1698841502483,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502482,"exitTime":1698841502482,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [5, 7, 6, 8, 10, 11, 1, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502483,"exitTime":1698841502483,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 1) => true","entryTime":1698841502483,"exitTime":1698841502483,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [5, 7, 6, 8, 10, 1, 11, 9, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502483,"exitTime":1698841502483,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 9) => true","entryTime":1698841502483,"exitTime":1698841502483,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502483,"exitTime":1698841502483,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 12) => false","entryTime":1698841502483,"exitTime":1698841502483,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 12, 3, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502483,"exitTime":1698841502483,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 3) => true","entryTime":1698841502483,"exitTime":1698841502483,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 12, 4, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502483,"exitTime":1698841502485,"value":2,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 4) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 4, 12, 2, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 2) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 13) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 12, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 13, int arg1 = 14) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 12, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502485,"exitTime":1698841502486,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 7) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [5, 7, 6, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 6) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [5, 6, 7, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [5, 6, 7, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 10) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [5, 6, 7, 8, 10, 1, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 1) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [5, 6, 7, 8, 1, 10, 9, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 9) => true","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502485,"exitTime":1698841502485,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 11, 3, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 3) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 11, 4, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 4) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 11, 2, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 2) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 12) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 11, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 12, int arg1 = 13) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 11, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502486,"exitTime":1698841502487,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 7) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [5, 6, 7, 8, 1, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 1) => true","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [5, 6, 7, 1, 8, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 9) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [5, 6, 7, 1, 8, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 9, int arg1 = 10) => false","entryTime":1698841502486,"exitTime":1698841502486,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [5, 6, 7, 1, 8, 9, 10, 3, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502486,"exitTime":1698841502487,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 3) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 10, 4, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 4) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 10, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 2) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 10, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 11, int arg1 = 12) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 10, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502487,"exitTime":1698841502488,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 7) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [5, 6, 7, 1, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 1) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [5, 6, 1, 7, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [5, 6, 1, 7, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 9) => false","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [5, 6, 1, 7, 8, 9, 3, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 9, int arg1 = 3) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [5, 6, 1, 7, 8, 3, 9, 4, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 9, int arg1 = 4) => true","entryTime":1698841502487,"exitTime":1698841502487,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [5, 6, 1, 7, 8, 3, 4, 9, 2, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 9, int arg1 = 2) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [5, 6, 1, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 9, int arg1 = 10) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 9, int[] arg1 = [5, 6, 1, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 10, int arg1 = 11) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 9, int[] arg1 = [5, 6, 1, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502488,"exitTime":1698841502489,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [5, 6, 1, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [5, 6, 1, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 1) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [5, 1, 6, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 7) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [5, 1, 6, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [5, 1, 6, 7, 8, 3, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 3) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [5, 1, 6, 7, 3, 8, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 4) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [5, 1, 6, 7, 3, 4, 8, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 2) => true","entryTime":1698841502488,"exitTime":1698841502488,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [5, 1, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 9) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 8, int[] arg1 = [5, 1, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 9, int arg1 = 10) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 8, int[] arg1 = [5, 1, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [5, 1, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 1) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [1, 5, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [1, 5, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 7) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [1, 5, 6, 7, 3, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 3) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [1, 5, 6, 3, 7, 4, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 4) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [1, 5, 6, 3, 4, 7, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 2) => true","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [1, 5, 6, 3, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 7, int[] arg1 = [1, 5, 6, 3, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 8, int arg1 = 9) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 7, int[] arg1 = [1, 5, 6, 3, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502490,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [1, 5, 6, 3, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 1, int arg1 = 5) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [1, 5, 6, 3, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502489,"exitTime":1698841502489,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [1, 5, 6, 3, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502489,"exitTime":1698841502490,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 3) => true","entryTime":1698841502489,"exitTime":1698841502490,"value":1}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [1, 5, 3, 6, 4, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 4) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [1, 5, 3, 4, 6, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 2) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [1, 5, 3, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 7) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 6, int[] arg1 = [1, 5, 3, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 7, int arg1 = 8) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 6, int[] arg1 = [1, 5, 3, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [1, 5, 3, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 1, int arg1 = 5) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [1, 5, 3, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 3) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [1, 3, 5, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 4) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [1, 3, 4, 5, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 2) => true","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 5, int[] arg1 = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 6, int arg1 = 7) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 5, int[] arg1 = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502491,"value":1,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 1, int arg1 = 3) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 3, int arg1 = 4) => false","entryTime":1698841502490,"exitTime":1698841502490,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502490,"exitTime":1698841502491,"value":1,"children":[{"name":"isABiggerThanB(int arg0 = 4, int arg1 = 2) => true","entryTime":1698841502490,"exitTime":1698841502491,"value":1}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 4, int arg1 = 5) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 4, int[] arg1 = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 5, int arg1 = 6) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 4, int[] arg1 = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 1, int arg1 = 3) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => true","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 3, int arg1 = 2) => true","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 3, int arg1 = 4) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 3, int[] arg1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 4, int arg1 = 5) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
]}
,{"name":"bubbleUp(int arg0 = 3, int[] arg1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"swapIfBiggerThanNext(int arg0 = 0, int[] arg1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 1, int arg1 = 2) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 1, int[] arg1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 2, int arg1 = 3) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
,{"name":"swapIfBiggerThanNext(int arg0 = 2, int[] arg1 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0,"children":[{"name":"isABiggerThanB(int arg0 = 3, int arg1 = 4) => false","entryTime":1698841502491,"exitTime":1698841502491,"value":0}
]}
]}
]}
]}
},{"thread":"main","snapshotTime":1698841502573,"span":{"name":"mainRoot","entryTime":1698841502440,"exitTime":-1,"value":0,"children":[{"name":"main(Object[] arg0 = []) => null","entryTime":1698841502440,"exitTime":1698841502491,"value":51}
]}
}
],
];