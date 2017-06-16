import service.*;

import java.io.File;
import java.io.IOException;

public class TestThreads extends Thread {
    String name = "";
    ServiceStorage service;
    TestThreads(String file) {
        name = file;
        service = new ServiceStorage();
    }

    @Override
    public void run() {
//        FileEntry entry = null;
//        try {
//            entry = new FileEntry(name, "D:\\tempFiles\\");
//        } catch (WrongKeyException e) {
//            e.printStackTrace();
//        } catch (WrongDirNameException e) {
//            e.printStackTrace();
//        }

//        File fileIfExist = entry.getFileIfExist();
        try {
            long start = System.currentTimeMillis();
            byte[] bytes = service.get(name);
            long stop = System.currentTimeMillis();
            System.out.println("thread name = " + Thread.currentThread().getId());
            System.out.println("duration = " + (stop - start));
        } catch (WrongKeyException e) {
            e.printStackTrace();
        } catch (WrongDirNameException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}