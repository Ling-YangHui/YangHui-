package StageController;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageController extends Application {

    protected static Stage mainStage;

    private void initStage() {
        try {
            mainStage = new Stage();
            File mainStageFile = new File("./fxml/Mainwin.fxml");
            Parent mainStageRoot = FXMLLoader.load(mainStageFile.toURI().toURL());
            mainStage.setTitle("YangHui的坏坏聊天查找器 version-1.0");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(mainStageRoot));
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
}
