package ru.mirea.shinomontazh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShinomontazhApplication extends Application {

        @Override
        public void start(Stage stage) throws IOException {

                FXMLLoader loader = new FXMLLoader(
                                ShinomontazhApplication.class.getResource(
                                                "/fxml/main-view.fxml"));

                Scene scene = new Scene(
                                loader.load(),
                                1100,
                                700);

                scene.getStylesheets().add(
                                ShinomontazhApplication.class
                                                .getResource("/css/style.css")
                                                .toExternalForm());

                stage.setTitle(
                                "Информационная система «Шиномонтаж»");

                stage.setMinWidth(900);
                stage.setMinHeight(600);

                stage.setScene(scene);
                stage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}