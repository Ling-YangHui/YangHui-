package StageController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageController extends Application {

    public static Stage mainStage;
    public static Stage inputStage;
    public static Map<String, Object> WinControllerList;
    public static Map<String, Stage> StageControllerList;
    public static String mainTitle = "YangHui的坏坏聊天查找器 version-1.0";

    private void initStage() {
        
        WinControllerList = new HashMap<String, Object>();
        StageControllerList = new HashMap<String, Stage>();
        try {
            mainStage = new Stage();
            File mainStageFile = new File("./fxml/Mainwin.fxml");
            Parent mainStageRoot = FXMLLoader.load(mainStageFile.toURI().toURL());
            mainStage.setTitle("YangHui的坏坏聊天查找器 version-1.0");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(mainStageRoot));

            inputStage = new Stage();
            File inputStageFile = new File("./fxml/GetPicture.fxml");
            Parent inputStageRoot = FXMLLoader.load(inputStageFile.toURI().toURL());
            inputStage.setResizable(false);
            inputStage.setScene(new Scene(inputStageRoot));
            // inputStage.setAlwaysOnTop(true);

            StageControllerList.put("Mainwin", mainStage);
            StageControllerList.put("Inputwin", inputStage);
            inputStage.initOwner(mainStage);
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }

    @Override
    public void start(Stage arg0) throws Exception {
        initStage();
        mainStage.show();
    }

    /* 以下为界面管理窗口 */
    public static void closeWin() {
        mainStage.close();
    }

    public static void reshowWin() {
        mainStage.show();
    }

    public static void hideWin() {
        mainStage.hide();
    }

    public static void closeInputWin() {
        inputStage.close();
    }

    public static void showInputWin() {
        inputStage.show();
    }
}
