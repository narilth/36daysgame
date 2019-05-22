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

/**
 * The main class, creating the window, scenes, and starting the program.
 *
 * @author Harit Kapadia, Jack Farley
 */
public class Main extends Application {
	public static HashMap<String, Scene> scenes;
	public World world = new World();

	/**
	 * Initialises the scene map in preparation of the full program.
	 */
	@Override
	public void init() {
		scenes = new HashMap<String, Scene>();
	}

	/**
	 * Starts the program.
	 *
	 * @param primaryStage The window on which the program will run.
	 */
	@Override
	public void start(Stage primaryStage) {
		scenes.put("Main Menu", new Scene(new MainMenuPane(primaryStage)));
		scenes.put("Level Select", new Scene(new LevelSelectPane(primaryStage)));
		scenes.put("High Scores", new Scene(new HighScoresPane(primaryStage)));
		scenes.put("About", new Scene(new AboutPane(primaryStage)));
		scenes.put("Survival Guide", new Scene(new SurvivalGuidePane(primaryStage)));
		primaryStage.setTitle("36 Days - Wilderness Survival Game");
		primaryStage.setScene(scenes.get("Main Menu"));
		primaryStage.show();
	}

	/**
	 * The main method.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		launch();
	}

}
