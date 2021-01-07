import java.io.File;

import StageController.StageController;
import UserController.FileLocation;

public class App {
    public static void main(String[] args) throws Exception {
        FileLocation.setFile(new File("K:\\截图怪"));
        StageController.launch(StageController.class);       
    }
}
