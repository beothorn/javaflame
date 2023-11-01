var executionMetadata = "<p>Flags: []</p><p>Output: '/tmp/1698867969417_snap'</p><p>Excludes: []</p><p>Filters: []</p>";
var data = [[
{"thread":"InPlaceQuickSort","snapshotTime":1698867970612,"span":{"name":"InPlaceQuickSortRoot","entryTime":1698867970580,"exitTime":-1,"value":0,"children":[{"name":"InplaceQuickSort.sort(int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970580,"exitTime":1698867970595,"value":15,"children":[{"name":"InplaceQuickSort.sort(int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 0, int = 14) => null","entryTime":1698867970580,"exitTime":1698867970595,"value":15,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 14, int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => 6","entryTime":1698867970580,"exitTime":1698867970580,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 6, int = 0, int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => 0","entryTime":1698867970583,"exitTime":1698867970583,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 6, int = 14, int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => 14","entryTime":1698867970583,"exitTime":1698867970583,"value":0}
,{"name":"Swap.swap(int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 0, int = 14) => [6, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 10]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 6, int = 1, int[] = [6, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 10]) => 1","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 6, int = 13, int[] = [6, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 10]) => 12","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [6, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 10], int = 1, int = 12) => [6, 2, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 14, 13, 10]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 6, int = 2, int[] = [6, 2, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 14, 13, 10]) => 2","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 6, int = 11, int[] = [6, 2, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 14, 13, 10]) => 11","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [6, 2, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 14, 13, 10], int = 2, int = 11) => [6, 2, 4, 11, 8, 5, 15, 12, 1, 9, 3, 7, 14, 13, 10]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 6, int = 3, int[] = [6, 2, 4, 11, 8, 5, 15, 12, 1, 9, 3, 7, 14, 13, 10]) => 3","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 6, int = 10, int[] = [6, 2, 4, 11, 8, 5, 15, 12, 1, 9, 3, 7, 14, 13, 10]) => 10","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [6, 2, 4, 11, 8, 5, 15, 12, 1, 9, 3, 7, 14, 13, 10], int = 3, int = 10) => [6, 2, 4, 3, 8, 5, 15, 12, 1, 9, 11, 7, 14, 13, 10]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 6, int = 4, int[] = [6, 2, 4, 3, 8, 5, 15, 12, 1, 9, 11, 7, 14, 13, 10]) => 4","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 6, int = 9, int[] = [6, 2, 4, 3, 8, 5, 15, 12, 1, 9, 11, 7, 14, 13, 10]) => 8","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [6, 2, 4, 3, 8, 5, 15, 12, 1, 9, 11, 7, 14, 13, 10], int = 4, int = 8) => [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 6, int = 5, int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 6","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 6, int = 7, int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 5","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 0, int = 5) => null","entryTime":1698867970590,"exitTime":1698867970592,"value":2,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 5, int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 5","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 5, int = 0, int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 0","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 5, int = 5, int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 5","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"Swap.swap(int[] = [6, 2, 4, 3, 1, 5, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 0, int = 5) => [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 5, int = 1, int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 5","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 5, int = 4, int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 4","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 0, int = 4) => null","entryTime":1698867970591,"exitTime":1698867970592,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 4, int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 1","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 1, int = 0, int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 0","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 1, int = 4, int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 4","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [5, 2, 4, 3, 1, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 0, int = 4) => [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 1, int = 1, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 1","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 1, int = 3, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 0","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 1, int = 4) => null","entryTime":1698867970591,"exitTime":1698867970592,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 4, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 5","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 5, int = 1, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 4","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 5, int = 4, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 4","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 4, int = 4) => [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 1, int = 3) => null","entryTime":1698867970591,"exitTime":1698867970592,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 3, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 3","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 3, int = 1, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 2","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 3, int = 3, int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 3","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 4, 3, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 2, int = 3) => [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 1, int = 2) => null","entryTime":1698867970592,"exitTime":1698867970592,"value":0,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 2, int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 3","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 3, int = 1, int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 2","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 3, int = 2, int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 2","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 2, int = 2) => [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
]}
]}
]}
]}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 6, int = 14) => null","entryTime":1698867970592,"exitTime":1698867970595,"value":3,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 14, int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 10","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 10, int = 6, int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 6","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 10, int = 14, int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10]) => 14","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 15, 12, 8, 9, 11, 7, 14, 13, 10], int = 6, int = 14) => [1, 2, 3, 4, 5, 6, 10, 12, 8, 9, 11, 7, 14, 13, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 10, int = 7, int[] = [1, 2, 3, 4, 5, 6, 10, 12, 8, 9, 11, 7, 14, 13, 15]) => 7","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 10, int = 13, int[] = [1, 2, 3, 4, 5, 6, 10, 12, 8, 9, 11, 7, 14, 13, 15]) => 11","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 10, 12, 8, 9, 11, 7, 14, 13, 15], int = 7, int = 11) => [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 10, int = 8, int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15]) => 10","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 10, int = 10, int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15]) => 9","entryTime":1698867970592,"exitTime":1698867970593,"value":1}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15], int = 6, int = 9) => null","entryTime":1698867970593,"exitTime":1698867970594,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 9, int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15]) => 9","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 9, int = 6, int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15]) => 6","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 9, int = 9, int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15]) => 9","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 10, 7, 8, 9, 11, 12, 14, 13, 15], int = 6, int = 9) => [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 9, int = 7, int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15]) => 9","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 9, int = 8, int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15]) => 8","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15], int = 6, int = 8) => null","entryTime":1698867970593,"exitTime":1698867970594,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 8, int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15]) => 8","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 8, int = 6, int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15]) => 6","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 8, int = 8, int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15]) => 8","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 9, 7, 8, 10, 11, 12, 14, 13, 15], int = 6, int = 8) => [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 8, int = 7, int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15]) => 8","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 8, int = 7, int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15]) => 7","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15], int = 6, int = 7) => null","entryTime":1698867970593,"exitTime":1698867970594,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 7, int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15]) => 7","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 7, int = 6, int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15]) => 6","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 7, int = 7, int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15]) => 7","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 8, 7, 9, 10, 11, 12, 14, 13, 15], int = 6, int = 7) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]","entryTime":1698867970593,"exitTime":1698867970594,"value":1}
]}
]}
]}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15], int = 10, int = 14) => null","entryTime":1698867970594,"exitTime":1698867970595,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 14, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]) => 15","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 15, int = 10, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]) => 14","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 15, int = 14, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]) => 14","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15], int = 14, int = 14) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15], int = 10, int = 13) => null","entryTime":1698867970594,"exitTime":1698867970595,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 13, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]) => 13","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 13, int = 10, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]) => 12","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 13, int = 13, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15]) => 13","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 13, 15], int = 12, int = 13) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15], int = 10, int = 12) => null","entryTime":1698867970594,"exitTime":1698867970595,"value":1,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 12, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => 13","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 13, int = 10, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => 12","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 13, int = 12, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => 12","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15], int = 12, int = 12) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"InplaceQuickSort.sort(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15], int = 10, int = 11) => null","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"InplaceQuickSort.getPivotAt(int = 11, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => 12","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"InplaceQuickSort.findNextValueBiggerOrEqualThanPivotOnLeft(int = 12, int = 10, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => 11","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"InplaceQuickSort.findNextValueSmallerOrEqualThanPivotOnRight(int = 12, int = 11, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => 11","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"Swap.swap(int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15], int = 11, int = 11) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
]}
]}
]}
]}
]}
]}
]}
},{"thread":"BubbleSort","snapshotTime":1698867970664,"span":{"name":"BubbleSortRoot","entryTime":1698867970572,"exitTime":-1,"value":0,"children":[{"name":"BubbleSort.sort(int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970572,"exitTime":1698867970601,"value":29,"children":[{"name":"BubbleSort.bubbleUp(int = 14, int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970572,"exitTime":1698867970591,"value":19,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => false","entryTime":1698867970572,"exitTime":1698867970583,"value":11,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 14) => false","entryTime":1698867970572,"exitTime":1698867970572,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970583,"exitTime":1698867970589,"value":6,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 7) => true","entryTime":1698867970583,"exitTime":1698867970583,"value":0}
,{"name":"Swap.swap(int[] = [10, 14, 7, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 1, int = 2) => [10, 7, 14, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [10, 7, 14, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 11) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 14, 11, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 2, int = 3) => [10, 7, 11, 14, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [10, 7, 11, 14, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 8) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 14, 8, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 3, int = 4) => [10, 7, 11, 8, 14, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [10, 7, 11, 8, 14, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 5) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 14, 5, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 4, int = 5) => [10, 7, 11, 8, 5, 14, 15, 12, 1, 9, 3, 4, 2, 13, 6]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [10, 7, 11, 8, 5, 14, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => false","entryTime":1698867970589,"exitTime":1698867970589,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 15) => false","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [10, 7, 11, 8, 5, 14, 15, 12, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 12) => true","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 15, 12, 1, 9, 3, 4, 2, 13, 6], int = 6, int = 7) => [10, 7, 11, 8, 5, 14, 12, 15, 1, 9, 3, 4, 2, 13, 6]","entryTime":1698867970589,"exitTime":1698867970589,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [10, 7, 11, 8, 5, 14, 12, 15, 1, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 1) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 15, 1, 9, 3, 4, 2, 13, 6], int = 7, int = 8) => [10, 7, 11, 8, 5, 14, 12, 1, 15, 9, 3, 4, 2, 13, 6]","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 8, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 15, 9, 3, 4, 2, 13, 6]) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 9) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 15, 9, 3, 4, 2, 13, 6], int = 8, int = 9) => [10, 7, 11, 8, 5, 14, 12, 1, 9, 15, 3, 4, 2, 13, 6]","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 9, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 15, 3, 4, 2, 13, 6]) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 3) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 15, 3, 4, 2, 13, 6], int = 9, int = 10) => [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 15, 4, 2, 13, 6]","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 10, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 15, 4, 2, 13, 6]) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 4) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 15, 4, 2, 13, 6], int = 10, int = 11) => [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 15, 2, 13, 6]","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 11, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 15, 2, 13, 6]) => true","entryTime":1698867970590,"exitTime":1698867970591,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 2) => true","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 15, 2, 13, 6], int = 11, int = 12) => [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 15, 13, 6]","entryTime":1698867970590,"exitTime":1698867970590,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 12, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 15, 13, 6]) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 13) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 15, 13, 6], int = 12, int = 13) => [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 15, 6]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 13, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 15, 6]) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 15, int = 6) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 15, 6], int = 13, int = 14) => [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 13, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970591,"exitTime":1698867970593,"value":2,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 7) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [10, 7, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15], int = 0, int = 1) => [7, 10, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [7, 10, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => false","entryTime":1698867970591,"exitTime":1698867970591,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 11) => false","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [7, 10, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 8) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 11, 8, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15], int = 2, int = 3) => [7, 10, 8, 11, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [7, 10, 8, 11, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 5) => true","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 11, 5, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15], int = 3, int = 4) => [7, 10, 8, 5, 11, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]","entryTime":1698867970591,"exitTime":1698867970591,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [7, 10, 8, 5, 11, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => false","entryTime":1698867970591,"exitTime":1698867970592,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 14) => false","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [7, 10, 8, 5, 11, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 12) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 14, 12, 1, 9, 3, 4, 2, 13, 6, 15], int = 5, int = 6) => [7, 10, 8, 5, 11, 12, 14, 1, 9, 3, 4, 2, 13, 6, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [7, 10, 8, 5, 11, 12, 14, 1, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 1) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 14, 1, 9, 3, 4, 2, 13, 6, 15], int = 6, int = 7) => [7, 10, 8, 5, 11, 12, 1, 14, 9, 3, 4, 2, 13, 6, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [7, 10, 8, 5, 11, 12, 1, 14, 9, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 9) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 14, 9, 3, 4, 2, 13, 6, 15], int = 7, int = 8) => [7, 10, 8, 5, 11, 12, 1, 9, 14, 3, 4, 2, 13, 6, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 8, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 14, 3, 4, 2, 13, 6, 15]) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 3) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 9, 14, 3, 4, 2, 13, 6, 15], int = 8, int = 9) => [7, 10, 8, 5, 11, 12, 1, 9, 3, 14, 4, 2, 13, 6, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 9, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 14, 4, 2, 13, 6, 15]) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 4) => true","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 14, 4, 2, 13, 6, 15], int = 9, int = 10) => [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 14, 2, 13, 6, 15]","entryTime":1698867970592,"exitTime":1698867970592,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 10, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 14, 2, 13, 6, 15]) => true","entryTime":1698867970592,"exitTime":1698867970593,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 2) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 14, 2, 13, 6, 15], int = 10, int = 11) => [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 14, 13, 6, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 11, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 14, 13, 6, 15]) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 13) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 14, 13, 6, 15], int = 11, int = 12) => [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 14, 6, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 12, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 14, 6, 15]) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 14, int = 6) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 14, 6, 15], int = 12, int = 13) => [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 12, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970593,"exitTime":1698867970595,"value":2,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => false","entryTime":1698867970593,"exitTime":1698867970593,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 10) => false","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 8) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [7, 10, 8, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15], int = 1, int = 2) => [7, 8, 10, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [7, 8, 10, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 5) => true","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 10, 5, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15], int = 2, int = 3) => [7, 8, 5, 10, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [7, 8, 5, 10, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => false","entryTime":1698867970593,"exitTime":1698867970593,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 11) => false","entryTime":1698867970593,"exitTime":1698867970593,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [7, 8, 5, 10, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => false","entryTime":1698867970593,"exitTime":1698867970594,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 12) => false","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [7, 8, 5, 10, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 1) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 12, 1, 9, 3, 4, 2, 13, 6, 14, 15], int = 5, int = 6) => [7, 8, 5, 10, 11, 1, 12, 9, 3, 4, 2, 13, 6, 14, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [7, 8, 5, 10, 11, 1, 12, 9, 3, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 9) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 1, 12, 9, 3, 4, 2, 13, 6, 14, 15], int = 6, int = 7) => [7, 8, 5, 10, 11, 1, 9, 12, 3, 4, 2, 13, 6, 14, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [7, 8, 5, 10, 11, 1, 9, 12, 3, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 3) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 1, 9, 12, 3, 4, 2, 13, 6, 14, 15], int = 7, int = 8) => [7, 8, 5, 10, 11, 1, 9, 3, 12, 4, 2, 13, 6, 14, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 8, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 12, 4, 2, 13, 6, 14, 15]) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 4) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 1, 9, 3, 12, 4, 2, 13, 6, 14, 15], int = 8, int = 9) => [7, 8, 5, 10, 11, 1, 9, 3, 4, 12, 2, 13, 6, 14, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 9, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 12, 2, 13, 6, 14, 15]) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 2) => true","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 12, 2, 13, 6, 14, 15], int = 9, int = 10) => [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 13, 6, 14, 15]","entryTime":1698867970594,"exitTime":1698867970594,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 10, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 13, 6, 14, 15]) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 13) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 11, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 13, 6, 14, 15]) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 13, int = 6) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 13, 6, 14, 15], int = 11, int = 12) => [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 11, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970595,"exitTime":1698867970596,"value":1,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 8) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 5) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"Swap.swap(int[] = [7, 8, 5, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15], int = 1, int = 2) => [7, 5, 8, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [7, 5, 8, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 10) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [7, 5, 8, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 11) => false","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [7, 5, 8, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 1) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 11, 1, 9, 3, 4, 2, 12, 6, 13, 14, 15], int = 4, int = 5) => [7, 5, 8, 10, 1, 11, 9, 3, 4, 2, 12, 6, 13, 14, 15]","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [7, 5, 8, 10, 1, 11, 9, 3, 4, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970595,"exitTime":1698867970596,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 9) => true","entryTime":1698867970595,"exitTime":1698867970595,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 1, 11, 9, 3, 4, 2, 12, 6, 13, 14, 15], int = 5, int = 6) => [7, 5, 8, 10, 1, 9, 11, 3, 4, 2, 12, 6, 13, 14, 15]","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [7, 5, 8, 10, 1, 9, 11, 3, 4, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 3) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 1, 9, 11, 3, 4, 2, 12, 6, 13, 14, 15], int = 6, int = 7) => [7, 5, 8, 10, 1, 9, 3, 11, 4, 2, 12, 6, 13, 14, 15]","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [7, 5, 8, 10, 1, 9, 3, 11, 4, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 4) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 1, 9, 3, 11, 4, 2, 12, 6, 13, 14, 15], int = 7, int = 8) => [7, 5, 8, 10, 1, 9, 3, 4, 11, 2, 12, 6, 13, 14, 15]","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 8, int[] = [7, 5, 8, 10, 1, 9, 3, 4, 11, 2, 12, 6, 13, 14, 15]) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 2) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 1, 9, 3, 4, 11, 2, 12, 6, 13, 14, 15], int = 8, int = 9) => [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 12, 6, 13, 14, 15]","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 9, int[] = [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 12, 6, 13, 14, 15]) => false","entryTime":1698867970596,"exitTime":1698867970596,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 12) => false","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 10, int[] = [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 12, 6, 13, 14, 15]) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 12, int = 6) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 12, 6, 13, 14, 15], int = 10, int = 11) => [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 10, int[] = [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970596,"exitTime":1698867970597,"value":1,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 5) => true","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
,{"name":"Swap.swap(int[] = [7, 5, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15], int = 0, int = 1) => [5, 7, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]","entryTime":1698867970596,"exitTime":1698867970596,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [5, 7, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 8) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [5, 7, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 10) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [5, 7, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 1) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 10, 1, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15], int = 3, int = 4) => [5, 7, 8, 1, 10, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [5, 7, 8, 1, 10, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 9) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 1, 10, 9, 3, 4, 2, 11, 6, 12, 13, 14, 15], int = 4, int = 5) => [5, 7, 8, 1, 9, 10, 3, 4, 2, 11, 6, 12, 13, 14, 15]","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [5, 7, 8, 1, 9, 10, 3, 4, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 3) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 1, 9, 10, 3, 4, 2, 11, 6, 12, 13, 14, 15], int = 5, int = 6) => [5, 7, 8, 1, 9, 3, 10, 4, 2, 11, 6, 12, 13, 14, 15]","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [5, 7, 8, 1, 9, 3, 10, 4, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 4) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 1, 9, 3, 10, 4, 2, 11, 6, 12, 13, 14, 15], int = 6, int = 7) => [5, 7, 8, 1, 9, 3, 4, 10, 2, 11, 6, 12, 13, 14, 15]","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [5, 7, 8, 1, 9, 3, 4, 10, 2, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 2) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 1, 9, 3, 4, 10, 2, 11, 6, 12, 13, 14, 15], int = 7, int = 8) => [5, 7, 8, 1, 9, 3, 4, 2, 10, 11, 6, 12, 13, 14, 15]","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 8, int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 11, 6, 12, 13, 14, 15]) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 11) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 9, int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 11, 6, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 11, int = 6) => true","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 11, 6, 12, 13, 14, 15], int = 9, int = 10) => [5, 7, 8, 1, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 9, int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970598,"value":1,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 7) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 8) => false","entryTime":1698867970597,"exitTime":1698867970597,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => true","entryTime":1698867970597,"exitTime":1698867970598,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 1) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 8, 1, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15], int = 2, int = 3) => [5, 7, 1, 8, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [5, 7, 1, 8, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => false","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 9) => false","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [5, 7, 1, 8, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 9, int = 3) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 1, 8, 9, 3, 4, 2, 10, 6, 11, 12, 13, 14, 15], int = 4, int = 5) => [5, 7, 1, 8, 3, 9, 4, 2, 10, 6, 11, 12, 13, 14, 15]","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [5, 7, 1, 8, 3, 9, 4, 2, 10, 6, 11, 12, 13, 14, 15]) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 9, int = 4) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 1, 8, 3, 9, 4, 2, 10, 6, 11, 12, 13, 14, 15], int = 5, int = 6) => [5, 7, 1, 8, 3, 4, 9, 2, 10, 6, 11, 12, 13, 14, 15]","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [5, 7, 1, 8, 3, 4, 9, 2, 10, 6, 11, 12, 13, 14, 15]) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 9, int = 2) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 1, 8, 3, 4, 9, 2, 10, 6, 11, 12, 13, 14, 15], int = 6, int = 7) => [5, 7, 1, 8, 3, 4, 2, 9, 10, 6, 11, 12, 13, 14, 15]","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [5, 7, 1, 8, 3, 4, 2, 9, 10, 6, 11, 12, 13, 14, 15]) => false","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 9, int = 10) => false","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 8, int[] = [5, 7, 1, 8, 3, 4, 2, 9, 10, 6, 11, 12, 13, 14, 15]) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 10, int = 6) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 1, 8, 3, 4, 2, 9, 10, 6, 11, 12, 13, 14, 15], int = 8, int = 9) => [5, 7, 1, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 8, int[] = [5, 7, 1, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970598,"exitTime":1698867970599,"value":1,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [5, 7, 1, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970598,"exitTime":1698867970598,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 7) => false","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [5, 7, 1, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970598,"exitTime":1698867970599,"value":1,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 1) => true","entryTime":1698867970598,"exitTime":1698867970598,"value":0}
,{"name":"Swap.swap(int[] = [5, 7, 1, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15], int = 1, int = 2) => [5, 1, 7, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]","entryTime":1698867970598,"exitTime":1698867970599,"value":1}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [5, 1, 7, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 8) => false","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [5, 1, 7, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 3) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [5, 1, 7, 8, 3, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15], int = 3, int = 4) => [5, 1, 7, 3, 8, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [5, 1, 7, 3, 8, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 4) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [5, 1, 7, 3, 8, 4, 2, 9, 6, 10, 11, 12, 13, 14, 15], int = 4, int = 5) => [5, 1, 7, 3, 4, 8, 2, 9, 6, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [5, 1, 7, 3, 4, 8, 2, 9, 6, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 2) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [5, 1, 7, 3, 4, 8, 2, 9, 6, 10, 11, 12, 13, 14, 15], int = 5, int = 6) => [5, 1, 7, 3, 4, 2, 8, 9, 6, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [5, 1, 7, 3, 4, 2, 8, 9, 6, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 9) => false","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 7, int[] = [5, 1, 7, 3, 4, 2, 8, 9, 6, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 9, int = 6) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [5, 1, 7, 3, 4, 2, 8, 9, 6, 10, 11, 12, 13, 14, 15], int = 7, int = 8) => [5, 1, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 7, int[] = [5, 1, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970600,"value":1,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [5, 1, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 1) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [5, 1, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15], int = 0, int = 1) => [1, 5, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [1, 5, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 7) => false","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [1, 5, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 3) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [1, 5, 7, 3, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15], int = 2, int = 3) => [1, 5, 3, 7, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [1, 5, 3, 7, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 4) => true","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
,{"name":"Swap.swap(int[] = [1, 5, 3, 7, 4, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15], int = 3, int = 4) => [1, 5, 3, 4, 7, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970599,"exitTime":1698867970599,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [1, 5, 3, 4, 7, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 2) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
,{"name":"Swap.swap(int[] = [1, 5, 3, 4, 7, 2, 8, 6, 9, 10, 11, 12, 13, 14, 15], int = 4, int = 5) => [1, 5, 3, 4, 2, 7, 8, 6, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [1, 5, 3, 4, 2, 7, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 8) => false","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 6, int[] = [1, 5, 3, 4, 2, 7, 8, 6, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 8, int = 6) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
,{"name":"Swap.swap(int[] = [1, 5, 3, 4, 2, 7, 8, 6, 9, 10, 11, 12, 13, 14, 15], int = 6, int = 7) => [1, 5, 3, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 6, int[] = [1, 5, 3, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [1, 5, 3, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 1, int = 5) => false","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [1, 5, 3, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 3) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
,{"name":"Swap.swap(int[] = [1, 5, 3, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15], int = 1, int = 2) => [1, 3, 5, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [1, 3, 5, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 4) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
,{"name":"Swap.swap(int[] = [1, 3, 5, 4, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15], int = 2, int = 3) => [1, 3, 4, 5, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [1, 3, 4, 5, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 2) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
,{"name":"Swap.swap(int[] = [1, 3, 4, 5, 2, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15], int = 3, int = 4) => [1, 3, 4, 2, 5, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [1, 3, 4, 2, 5, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 7) => false","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 5, int[] = [1, 3, 4, 2, 5, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 7, int = 6) => true","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
,{"name":"Swap.swap(int[] = [1, 3, 4, 2, 5, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15], int = 5, int = 6) => [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970600,"exitTime":1698867970600,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 5, int[] = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 1, int = 3) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 3, int = 4) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 4, int = 2) => true","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
,{"name":"Swap.swap(int[] = [1, 3, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15], int = 2, int = 3) => [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 4, int = 5) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 4, int[] = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 5, int = 6) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 4, int[] = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 1, int = 3) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => true","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 3, int = 2) => true","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
,{"name":"Swap.swap(int[] = [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15], int = 1, int = 2) => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 3, int = 4) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 3, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 4, int = 5) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
]}
,{"name":"BubbleSort.bubbleUp(int = 3, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.swapIfBiggerThanNext(int = 0, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 1, int = 2) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 1, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 2, int = 3) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
,{"name":"BubbleSort.swapIfBiggerThanNext(int = 2, int[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0,"children":[{"name":"BubbleSort.isABiggerThanB(int = 3, int = 4) => false","entryTime":1698867970601,"exitTime":1698867970601,"value":0}
]}
]}
]}
]}
},{"thread":"main","snapshotTime":1698867970675,"span":{"name":"mainRoot","entryTime":1698867970545,"exitTime":-1,"value":0,"children":[{"name":"App.main(Object[] = []) => null","entryTime":1698867970545,"exitTime":1698867970602,"value":57}
]}
}
],
];