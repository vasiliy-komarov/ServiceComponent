import service.FileEntry;
import service.WrongDirNameException;
import service.WrongKeyException;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            FileEntry entry = new FileEntry("testFile", "D:\\tempFiles\\");
            File fileIfExist = entry.getFileIfExist();
            if (fileIfExist != null) {
                System.out.println("file was found, path = " + fileIfExist.getPath());
            } else {
                System.out.println("File not found");
            }
        } catch (WrongKeyException e) {
            e.printStackTrace();
        } catch (WrongDirNameException wrongDirLocation) {
            wrongDirLocation.printStackTrace();
        }
    }

}
