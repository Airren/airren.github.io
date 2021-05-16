import java.io.File;

public class Demo{
    public static void main(String[] args) {
        File file = new File("./");
        listAllFiles(file);

    }
    public static void listAllFiles(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            System.out.println(dir.getName());
            return;
        }
        for (File file : dir.listFiles()) {
            listAllFiles(file);
        }
    }
}