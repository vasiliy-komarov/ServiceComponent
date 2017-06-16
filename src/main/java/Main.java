import service.FileEntry;
import service.WrongDirNameException;
import service.WrongKeyException;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            FileEntry entry = new FileEntry("testFile", "D:\\tempFiles\\");

            long start = System.currentTimeMillis();
            File fileIfExist = entry.getFileIfExist();
            long stop = System.currentTimeMillis();

            System.out.println("duration = " + (stop - start));

            if (fileIfExist != null) {
                System.out.println("file was found, path = " + fileIfExist.getPath());
            } else {
                System.out.println("File not found");
            }
        } catch (WrongKeyException | WrongDirNameException e) {
            e.printStackTrace();
        }
    }

}
