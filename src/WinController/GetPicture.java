package WinController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import StageController.StageController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;

public class GetPicture {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField speaker;

    @FXML
    private TextField saying;

    @FXML
    private Button inputData;

    @FXML
    void storeData(ActionEvent event) {
        Mainwin mainwin = (Mainwin) StageController.WinControllerList.get("Mainwin");
        String activeGroup = mainwin.getActiveGroup();
        Vector<File> groupDirList = mainwin.getGroupDirList();
        Vector<Mainwin.MessageList> groupMessageLists = mainwin.getGroupMessageList();
        Stage inputStage;
        Stage mainStage = StageController.StageControllerList.get("Mainwin");

        if (speaker.getText() == "" || saying.getText() == "") {
            return;
        }
        String newPicturePath = "";
        for (File file : groupDirList) {
            if (file.getName().equals(activeGroup)) {
                newPicturePath = file.getPath();
            }
        }
        if (newPicturePath == "") {
            inputStage = (Stage) StageController.WinControllerList.get("Inputwin");
            inputStage.close();
            mainStage.setTitle(StageController.mainTitle + "    文件目录不存在");
            return;
        }
        newPicturePath += ("/" + speaker.getText() + "  " + saying.getText() + ".png");
        File newPicture = new File(newPicturePath);
        int i = 1;
        while (newPicture.exists()) {
            newPicturePath += ("/" + speaker.getText() + "  " + saying.getText() + Integer.toString(i) + ".png");
            newPicture = new File(newPicturePath);
            i++;
        }

        Clipboard sysClipboard = Clipboard.getSystemClipboard();
        try {
            if (sysClipboard.hasImage()) {
                Image newImage = sysClipboard.getImage();
                BufferedImage bfImage = SwingFXUtils.fromFXImage(newImage, null);
                ImageIO.write(bfImage, "png", newPicture);
            }
        } catch (Exception IOE) {
            mainStage.setTitle(StageController.mainTitle + "    写入错误");
            inputStage = (Stage) StageController.WinControllerList.get("Inputwin");
            inputStage.close();
            return;
        } 

        for (Mainwin.MessageList itemList : groupMessageLists) {
            if (itemList.getGroupName().equals(activeGroup)) {
                itemList.getMessageList()
                        .add(new Mainwin.MessageItems(speaker.getText(), saying.getText() + ".png", newPicture));
            }
        }
        StageController.closeInputWin();
    }

    @FXML
    void initialize() {
    }
}
