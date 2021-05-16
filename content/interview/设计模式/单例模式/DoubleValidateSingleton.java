public class DoubleValidataSingleton{

    public volatile static DoubleValidataSingleton singleton;
    private DoubleValidataSingleton(){}
    public static DoubleValidataSingleton getSingleton(){
        if(singleton == null){
            synchronized(DoubleValidataSingleton.class){
                if(singleton == null){
                    singleton = new DoubleValidataSingleton();
                }
            }
        }
        return singleton;
    }
}