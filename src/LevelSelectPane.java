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
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.charset.*;

/**
 * A pane allowing the selection of the playing of three levels.
 *
 * @param Harit Kapadia, Jack Farley
 */
public class LevelSelectPane extends VBox {
	private Scene scene;

	/**
	 * The pane constructor.
	 * The pane is initialised with the level description and entry for each level.
	 *
	 * @param scene The window on which the pane will be displayed.
	 * @param stage The stage on which the scene is displayed
	 */
	LevelSelectPane(Scene scene, Stage stage) {
		this.scene = scene;
		getChildren().add(new Label("Level Select"){{
			setId("title");
		}});
		getChildren().add(new HBox(){{
			getChildren().add(new LevelPane(scene, "Deficiency", "Panic", "Knowledge is wealth.", true, stage){{setAlignment(Pos.TOP_CENTER);}});
			getChildren().add(new LevelPane(scene, "Panic", "Escape", "Stop and smell the roses.", true, stage){{setAlignment(Pos.TOP_CENTER);}});
			getChildren().add(new LevelPane(scene, "Escape", null, "Stay for a while.", true, stage){{setAlignment(Pos.TOP_CENTER);}});
			setAlignment(Pos.CENTER);
			setPadding(new Insets(0, 0, 0, -50));
			setSpacing(50);
		}});
		getChildren().add(new Button("Return"){{
			setOnAction(e -> Main.setPane(scene, "Main Menu"));
		}});

		getStylesheets().add("stylesheet.css");

		setAlignment(Pos.CENTER);
		setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

		setPadding(new Insets(-50, 50, 50, 50));
		setSpacing(30);

	}
}

/**
 * A class representing a level selection pane.
 *
 * @author Harit Kapadia, Jack Farley
 * @see LevelSelectPane
 */
class LevelPane extends VBox {
	private Scene scene;

	/**
	 * The class constructor.
	 * The pane is initialised with a level name, description, and a button to play the level.
	 *
	 * @param scene The window on which the pane will be displayed.
	 * @param name The name of the level.
	 * @param description The description of the level.
	 * @param playable The state of playability of the level.
	 */
	LevelPane(Scene scene, String name, String nextName, String description, boolean playable, Stage stage) {
		this.scene = scene;
		getChildren().add(new Label(name){{setId("heading");}});
		getChildren().add(new Label(description));
		setSpacing(10);
		if(playable)
			getChildren().add(new Button("Enter"){{
				setOnAction(e -> {
						Main.setPane(scene, new Game(scene, Paths.get("worlds", "128", name), () -> {
									try {
										if(nextName != null)
											Runtime.getRuntime().exec("robocopy world\\128\\" + name + " world\\128\\" + nextName + "/s /e");
									}
									catch (Throwable qwer) {
										System.out.println("Error " + qwer.getMessage());
										qwer.printStackTrace();
									}

						}){{
							start();
						}}.getPane());
					});
			}});
		else
			getChildren().add(new Label("Locked"){{setPadding(new Insets(5,0,0,0));}});
	}
}
