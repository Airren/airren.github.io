public class ShellSort {
    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = Math.floorDiv(n, 2); gap > 0; gap = Math.floorDiv(gap, 2)) {
            // 注意：这里和动图演示的不一样，动图是分组执行，实际操作是多个分组交替执行
            for (int i = gap; i < n; i++) {
                int j = i;
                int current = arr[i];
                while (j - gap >= 0 && current < arr[j - gap]) {
                    arr[j] = arr[j - gap];
                    j = j - gap;
                }
                arr[j] = current;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        shellSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",, ");
        }
    }
}