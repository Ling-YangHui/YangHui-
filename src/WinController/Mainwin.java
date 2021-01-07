package WinController;

import java.io.File;
import java.util.Vector;

import UserController.FileLocation;

import StageController.StageController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Mainwin {

    @FXML
    private SplitPane mainPane;

    @FXML
    private Accordion groupList;

    private ScrollPane searchResultHoldPane;

    private TableView<MessageItems> searchResultTable;

    private TitledPane searchTitledPane;

    @FXML
    private SplitPane rightPane;

    @FXML
    private SplitMenuButton funcShow;

    @FXML
    private MenuItem newFuncItem;

    @FXML
    private MenuItem orFuncItem;

    @FXML
    private MenuItem andFuncItem;

    @FXML
    private TextField searchKeywordText;

    @FXML
    private Button searchButton;

    @FXML
    private SplitPane rightBottomPane;

    @FXML
    private AnchorPane showPlat;

    @FXML
    private ImageView showImage;

    @FXML
    private Button storePictureButton;

    @FXML
    private Button closePictureButton;

    @FXML
    private Button exitProgramButton;

    private String nowChoiceMode;
    private ObservableList<MessageItems> messageInSearchCache;
    private Vector<File> subDirList;
    private String pastActiveGroup;

    @FXML
    void ClosePicture(ActionEvent event) {
        showImage.setImage(null);
    }

    @FXML
    void ExitProgram(ActionEvent event) {
        StageController.closeWin();
    }

    @FXML
    void SearchKeyword(ActionEvent event) {
        String keyWord = searchKeywordText.getText();
        if (keyWord == "")
            return;
        searchKeywordText.clear();
        if (nowChoiceMode == "NEW" || nowChoiceMode == "OR") {
            if (groupList.getExpandedPane() != null) {
                if (nowChoiceMode == "NEW")
                    messageInSearchCache.clear();
                String activeGroup = groupList.getExpandedPane().getText();
                if (activeGroup.indexOf("搜索结果") != -1)
                    activeGroup = pastActiveGroup;
                else
                    pastActiveGroup = activeGroup;
                for (File file : subDirList) {
                    String fileGroupString = file.getName();
                    if (fileGroupString.equals(activeGroup)) {
                        for (File file2 : file.listFiles()) {
                            try {
                                String[] fileName = file2.getName().split("  ", 2);
                                if (fileName[1].indexOf("png") == -1 && fileName[1].indexOf("jpg") == -1)
                                    continue;
                                if (fileName[0].indexOf(keyWord) != -1 || fileName[1].indexOf(keyWord) != -1) {
                                    messageInSearchCache.add(new MessageItems(fileName[0], fileName[1], file2));
                                }
                            } catch (IndexOutOfBoundsException IDOE) {

                            }
                        }
                        break;
                    }
                }
            }
            searchTitledPane.setText("搜索结果 (" + Integer.toString(messageInSearchCache.size()) + ")");
        }
        if (nowChoiceMode == "AND") {
            // Vector<MessageItems> messageInSearchCacheItems = new Vector<MessageItems>();
            Vector<Integer> removeList = new Vector<Integer>();
            // messageInSearchCacheItems.addAll(messageInSearchCache);
            for (int i = 0; i < messageInSearchCache.size(); i++) {
                MessageItems items = messageInSearchCache.get(i);
                if (items.getSaying().indexOf(keyWord) == -1 && items.getSpeaker().indexOf(keyWord) == -1)
                    removeList.add(i);
            }
            for (int i = 0; i < removeList.size(); i++) {
                messageInSearchCache.remove(removeList.get(i) - i);
            }
            searchTitledPane.setText("搜索结果 (" + Integer.toString(messageInSearchCache.size()) + ")");
            // messageInSearchCache.clear();
            // messageInSearchCache.addAll(messageInSearchCacheItems);
        }
    }

    @FXML
    void changeToAndSearch(ActionEvent event) {
        nowChoiceMode = "AND";
        funcShow.setText("与操作");
    }

    @FXML
    void changeToNewSearch(ActionEvent event) {
        nowChoiceMode = "NEW";
        funcShow.setText("新操作");
    }

    @FXML
    void changeToOrSearch(ActionEvent event) {
        nowChoiceMode = "OR";
        funcShow.setText("或操作");
    }

    @FXML
    void initialize() {
        initList();
        showImage.setFitWidth(530);
        showImage.setFitHeight(370);
    }

    @SuppressWarnings("unchecked")
    public void initList() {
        File rootFile = FileLocation.getFile();
        File[] subFile = rootFile.listFiles();
        messageInSearchCache = FXCollections.observableArrayList();
        subDirList = new Vector<File>();
        searchResultTable = new TableView<MessageItems>(messageInSearchCache);
        TableColumn<MessageItems, String> speakerResult = new TableColumn<MessageItems, String>("发言者");
        speakerResult.setCellValueFactory(new PropertyValueFactory<MessageItems, String>("speaker"));
        TableColumn<MessageItems, String> sayingResult = new TableColumn<MessageItems, String>("怪话");
        sayingResult.setCellValueFactory(new PropertyValueFactory<MessageItems, String>("saying"));
        speakerResult.setMaxWidth(70);
        speakerResult.setMinWidth(70);
        speakerResult.setPrefWidth(70);
        sayingResult.setPrefWidth(240 - 70);
        sayingResult.setMaxWidth(240 - 70);
        sayingResult.setMinWidth(240 - 70);
        searchResultTable.getColumns().addAll(speakerResult, sayingResult);
        searchResultHoldPane = new ScrollPane(searchResultTable);
        searchTitledPane = new TitledPane("搜索结果", searchResultHoldPane);
        groupList.getPanes().add(searchTitledPane);

        searchResultTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
                    if (searchResultTable.getSelectionModel().isEmpty() == false) {
                        MessageItems newItem = searchResultTable.getSelectionModel().getSelectedItem();
                        showImage.setImage(
                                new Image(newItem.imageFile.toURI().toURL().toString(), 530, 370, true, true));
                        Image img = showImage.getImage();
                        if (img != null) {
                            double h = img.getHeight();
                            double w = img.getWidth();
                            if (h == 370) {
                                showImage.setLayoutY(0);
                                showImage.setLayoutX((530 - w) / 2);
                            } else {
                                showImage.setLayoutX(0);
                                showImage.setLayoutY((370 - h) / 2);
                            }
                        }
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        for (int i = 0; i < subFile.length; i++) {
            if (subFile[i].isDirectory()) {
                subDirList.add(subFile[i]);
            }
        }
        for (int i = 0; i < subDirList.size(); i++) {
            File subDir = subDirList.get(i);

            ObservableList<MessageItems> messageList = FXCollections.observableArrayList();
            File[] pictureList = subDir.listFiles();

            for (int j = 0; j < pictureList.length; j++) {
                try {
                    String[] fileName = pictureList[j].getName().split("  ");
                    if (fileName[1].indexOf("png") == -1 && fileName[1].indexOf("jpg") == -1)
                        continue;
                    messageList.add(new MessageItems(fileName[0], fileName[1], pictureList[j]));
                } catch (IndexOutOfBoundsException IDOE) {

                }
            }

            TableView<MessageItems> groupTableView = new TableView<MessageItems>(messageList);

            TableColumn<MessageItems, String> speaker = new TableColumn<MessageItems, String>("发言者");
            speaker.setCellValueFactory(new PropertyValueFactory<>("speaker"));
            speaker.setPrefWidth(70);
            speaker.setMaxWidth(70);
            speaker.setMinWidth(70);
            TableColumn<MessageItems, String> saying = new TableColumn<MessageItems, String>("怪话");
            saying.setCellValueFactory(new PropertyValueFactory<>("saying"));
            saying.setMaxWidth(240 - 70);
            saying.setMinWidth(240 - 70);
            saying.setPrefWidth(240 - 70);
            groupTableView.getColumns().addAll(speaker, saying);

            ScrollPane newScrollPane = new ScrollPane(groupTableView);
            TitledPane newTitlePane = new TitledPane(subDir.getName(), newScrollPane);
            groupList.getPanes().add(newTitlePane);

            groupTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    try {
                        if (groupTableView.getSelectionModel().isEmpty() == false) {
                            MessageItems newItem = groupTableView.getSelectionModel().getSelectedItem();
                            showImage.setImage(
                                    new Image(newItem.imageFile.toURI().toURL().toString(), 530, 370, true, true));
                            Image img = showImage.getImage();
                            if (img != null) {
                                double h = img.getHeight();
                                double w = img.getWidth();
                                if (h == 370) {
                                    showImage.setLayoutY(0);
                                    showImage.setLayoutX((530 - w) / 2);
                                } else {
                                    showImage.setLayoutX(0);
                                    showImage.setLayoutY((370 - h) / 2);
                                }
                            }
                        }
                    } catch (Exception E) {
                        E.printStackTrace();
                    }
                }
            });
        }
    }

    public static class MessageItems {
        private SimpleStringProperty speaker;
        private SimpleStringProperty saying;
        private File imageFile;

        MessageItems(String speaker, String saying, File imageFile) {
            this.saying = new SimpleStringProperty(saying);
            this.speaker = new SimpleStringProperty(speaker);
            this.imageFile = imageFile;
        }

        public String getSaying() {
            return saying.get();
        }

        public String getSpeaker() {
            return speaker.get();
        }

        public void setSaying(String saying) {
            this.saying.set(saying);
        }

        public void setSpeaker(String speaker) {
            this.saying.set(speaker);
        }
    }
}