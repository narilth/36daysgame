package daysgame;

/**
 * Harit Kapadia, Jack Farley
 * Ms. Krasteva
 * 2019/May/21
 */

import java.util.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.geometry.Pos;
import java.util.*;
import javafx.scene.image.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.event.*;

/**
 * A pane that displays the end screen once the player has either won or lost
 *
 * Variables:
 *
 * scene      -A Scene object that is a reference to the main scene
 *
 * @author Harit Kapadia, Jack Farley
 */
public class EndScreen extends VBox {
        private Scene scene;
        
        /**
         * The pane constructor.
         * The pane is initialized with text and an exit button
         * @param scene The window on which the pane will be displayed.
         * @param stage The stage on which the scene is displayed
         * @param game The Game object that the pause menu pauses
         * @param win True if the user won, false if they lost
         */
        EndScreen (Scene scene, Game game, boolean win) {
                setAlignment(Pos.CENTER);
                setSpacing(30);
                
                setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
                if (win == false){
                        getChildren().add(new Label("GAME OVER"){{
                                setStyle("-fx-font-size: 50px;");
                        }});
                        getChildren().add(new Label("YOU DIED :("){{
                                setStyle("-fx-font-size: 30px;");
                        }});
                        getChildren().add(new Button("Exit"){{
                                setOnAction(e -> {
                                        Main.setPane(scene, "Main Menu");
                                        Main.settingsMenu.removeGameButtons();
                                });
                        }});
                } else {
                        getChildren().add(new Label("YOU PASSED THE LEVEL!"){{
                                setStyle("-fx-font-size: 50px;");
                        }});
                        getChildren().add(new Button("Exit"){{
                                setOnAction(e -> {
                                        game.killQuests();
                                        game.stop();
                                        game.getWorld().write();
                                        Main.setPane(game.getScene(), "Main Menu");
                                        Main.settingsMenu.removeGameButtons();
                                });
                        }});
                }
                
                getStylesheets().add(EndScreen.class.getResource("/stylesheet.css").toExternalForm());
        }
}
