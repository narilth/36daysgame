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
 * A pane that displays the logo fading in and out
 * 
 * Variables:
 * 
 * scene      -A Scene object that is a reference to the main scene
 * ft         -A FadeTransition object that contains the fade transition for the logo
 *
 * @author Harit Kapadia, Jack Farley
 */
public class SettingsMenu extends HBox {
        private Scene scene;
        FadeTransition ft;
        
        /**
         * The pane constructor.
         * The pane is initialized with an image and an animation that causes the image to fade in and out
         * @param scene The window on which the pane will be displayed.
         * @param stage The stage on which the scene is displayed
         */
        SettingsMenu (Scene scene, Stage stage) {
                setManaged(false);
                setPrefWidth(150);
                setSpacing(30);             
                
                getChildren().add(new ToggleButton(){{
                        setId("mutebutton");
                        setOnAction(e -> Main.mediaPlayer.setVolume(1-Main.mediaPlayer.getVolume()));
                }});
                
                getChildren().add(new Button(){{
                        setId("exitbutton");
                        setOnAction(e -> Main.setPane(scene, "Main Menu"));
                }});
                
                getStylesheets().add("stylesheet.css");
                relocate(scene.getWidth()-150,50);
                
                
        }
        
}
