import java.util.*;
public class Main {
    public int[] twoSum(int[] data, int value){
        
        Map<Integer, Integer> components = new HashMap<>();
        
        for(int i=0; i < data.length; i++){
            int component = value - data[i];
            if(components.containsKey(component)){
                return new int[]{components.get(component),i};
            }else{
               components.put(data[i], i);
            }
        }
        // throw new IllegalArguementException(" no two sum");
            
        return new int[]{};
            
        
    }
    public static void main(String[] args) {
        
        Main test = new Main();
        int[] res = test.twoSum(new int[]{1,2,3,4,5,6,7},7);
        System.out.println(Arrays.toString(res));
    }
}