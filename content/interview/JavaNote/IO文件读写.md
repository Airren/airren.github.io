

`RandomAccessFile` 不同于`FileInputStream`和`FileOutputStream`,不是他们的子类 
当我们想对一个文件进行读写操作的时候，创建一个指向该文件的`RandomAccessFile`流就可以了 ；
但是对于`OutputStream`和`DataOutputStream`，我们在使用的时候都是通过他们的构造方法来附加或更新文件，即在构造方法中new FileOutputStream；

