public class BinarySearch{
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8};
        int k = 8;
        System.out.println(binarySerach(arr, k));
    }

    public static int binarySerach(int[] arr, int k){
        int a = 0;
        int b = arr.length;

        while(a < b){
            int mid = a+(b-a)/2;
            if(k < arr[mid]){
                b = mid;
            }else if(k > arr[mid]){
                a = mid + 1;
            }else{
                return mid;
            }
        }
        return -1;

    }
}