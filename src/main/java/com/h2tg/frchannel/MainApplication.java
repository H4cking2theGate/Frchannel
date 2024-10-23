package com.h2tg.frchannel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        Parent page = FXMLLoader.load(MainApplication.class.getResource("MainView.fxml"));
        Scene scene = new Scene(page);
        stage.setTitle("Frchannel by A.R.");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}