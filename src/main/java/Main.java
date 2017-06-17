import service.FileEntry;
import service.ServiceStorage;
import service.WrongDirNameException;
import service.WrongKeyException;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //            FileEntry entry = new FileEntry("testFile1", "D:\\tempFiles\\");
//
//            long start = System.currentTimeMillis();
//            File fileIfExist = entry.getFileIfExist();
//            long stop = System.currentTimeMillis();
//
//            System.out.println("duration = " + (stop - start));

//            if (fileIfExist != null) {
//                System.out.println("file was found, path = " + fileIfExist.getPath());
//            } else {
//                System.out.println("File not found");
//            }
        ServiceStorage service = new ServiceStorage();
//
//        Thread t1 = new TestThreads("testFile1", service);
//        Thread t2 = new TestThreads("testFile1", service);
        Thread t3 = new TestThreads("testFile", service);
        Thread t4 = new TestThreads("testFile", service);
//        Thread t5 = new TestThreads("testFile1", service);
        Thread t6 = new TestThreads("testFile", service);
//        Thread t7 = new TestThreads("testFile1", service);
//        Thread t8 = new TestThreads("testFile", service);
//        Thread t9 = new TestThreads("testFile1", service);
//
//        t1.start();
//        t2.start();
        t3.start();
        t4.start();
//        t5.start();
        t6.start();
//        t7.start();
//        t8.start();
//        t9.start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("hello, i'm = " + Thread.currentThread().getName());
//            }
//        }).start();

        String hello = "hellogdsafdsjfhsfjkhsdfkjshfkjs123456789056657";
        try {
            service.put("testFile", hello.getBytes());
            byte[] testFiles = service.get("testFile");
            String s = new String(testFiles, "UTF-8");
            System.out.println("s = " + s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
