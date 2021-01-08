import java.io.File;

import StageController.StageController;
import UserController.FileLocation;

public class App {
    public static void main(String[] args) throws Exception {
        FileLocation.setFile(new File("K:/截图怪"));
        if (! FileLocation.getFile().exists()) {
            System.err.println("文件路径错误");
            return;
        }
        StageController.launch(StageController.class);
    }
}
