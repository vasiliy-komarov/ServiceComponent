import service.*;

import java.io.File;
import java.io.IOException;

public class TestThreads extends Thread {
    String _name = "";
    ServiceStorage service;
    TestThreads(String file, ServiceStorage serviceStorage) {
        _name = file;
        service = serviceStorage;
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
        String name = Thread.currentThread().getName();
        try {
//            long start = System.currentTimeMillis();
            byte[] bytes = service.get(_name);
            System.out.println("file content = " + new String(bytes, "UTF-8"));
//            long stop = System.currentTimeMillis();
//            System.out.println("thread name = " + Thread.currentThread().getId());
//            System.out.println("duration = " + (stop - start));
        } catch (WrongKeyException e) {
            e.printStackTrace();
        } catch (WrongDirNameException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR, FNFE, thread = " + name);
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("error, thread name = " + name + ", cuz = " + e.getMessage());
        }


    }
}