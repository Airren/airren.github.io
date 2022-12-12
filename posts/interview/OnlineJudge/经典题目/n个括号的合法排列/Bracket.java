import java.util.Scanner;
import java.util.Stack;
public class Bracket {
    public static void main(String []args){
        Scanner sc =new Scanner(System.in);
        while(sc.hasNext()){
            int m =sc.nextInt();
            Stack<String> s  =new Stack<String>();
            String n ="";
            generate(m , m, n);
        }
    }          
    public static void generate(int leftNum,int rightNum,String s)  
    {  
        if(leftNum==0&&rightNum==0)  
        {  
            System.out.println(s);  
        }  
        if(leftNum>0)  
        {  
            generate(leftNum-1,rightNum,s+'(');  
        }  
        if(rightNum>0&&leftNum<rightNum)  
        {  
            generate(leftNum,rightNum-1,s+')');  
        }  
    }  
}

