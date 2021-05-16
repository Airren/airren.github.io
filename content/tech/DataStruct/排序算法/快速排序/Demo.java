import java.util.Arrays;


public class Demo{
    public static void main(String[] args) {
        int[] arr = {7,6,5,4,3,2,1};
        quicksort(arr);
        System.out.println(Arrays.toString(arr));

    }
    public static void quicksort(int[] arr){
        if(arr.length == 0) return;
        quicksort(arr, 0, arr.length-1);
    }

    public static void quicksort(int[] arr, int low, int height){
        if(low>height) return;
        int i, j, temp, t;
        i = low;
        j = height;
        temp = arr[low];

        while(i<j){
            while(arr[j]>=temp && i<j){
                j--;
            }
            while(arr[i]<=temp && i<j){
                i++;
            }

            if(i<j){
                t = arr[i];
                arr[i] = arr[j];
                arr[j] = arr[i];
            }
        }

       
        arr[low] = arr[i];
        arr[i] = temp;

        quicksort(arr, low, i-1);
        quicksort(arr, i+1, height);


    }
}