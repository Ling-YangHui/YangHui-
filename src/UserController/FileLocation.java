package UserController;

import java.io.File;

public class FileLocation {
    
    private static File dir = null;

    public static void setFile(File file){
        dir = file;
    }

    public static File getFile() {
        return dir;
    }
}
