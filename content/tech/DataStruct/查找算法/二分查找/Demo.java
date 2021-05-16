public class Demo{
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6};
        System.out.println(binarySearch(arr, 5));
    }

    public static int binarySearch(int[] arr, int k) {
        int a = 0;
        int b = arr.length;
        
        while (a<b) {
            int mid = a+(b-a)/2;
            if (k< arr[mid]) {
                b = mid;
            }else if (k > arr[mid]) {
                a = mid +1;
            }else{
                return mid;
            }
        }
        return 0;
    }
}