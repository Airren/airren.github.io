public class InsertionSort {
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        int preIndex, current;
        for (int i = 1; i < n; i++) {
            preIndex = i - 1;
            current = arr[i];
            while (preIndex >= 0 && current < arr[preIndex]) {
                arr[preIndex + 1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex + 1] = current;
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        insertionSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}