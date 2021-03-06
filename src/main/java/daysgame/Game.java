package daysgame;

/**
 * Harit Kapadia, Jack Farley
 * Ms. Krasteva
 * 2019/June/02
 */

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

/**
 * This class sets up and runs the game
 *
 * Variables:
 *
 * scene     -The Scene on which the game is displayed
 * world     -The game world that the game uses
 * camera    -Controls the amount of the world that is visible to the user
 * gamePane  -The StackPane on which everything is displayed
 * canvas    -The Canvas on which the game graphics are displayed
 * questUI   -The user interface for the quests
 * toolbar   -The user's inventory that they have immmediate access to
 * inventoryPane -The rest of the user's inventory
 * player    -Stores the player's stats
 * health    -Displays how much health the user has left
 * hunger    -Displays a representation of how much time the player has before they die of starvation
 * exposure  -Displays a representation of how much time the player has before they die of exposure
 * thirst    -Displays a representation of how much time the player has before they die of thirst
 * i         -Manages the user input
 * questManager  -Manages the quests
 * prevPosition  -The player's previous position
 * helpMenu  -The on screen instructions during the game
 * gameSurvivalGuide  -The Survival Guide object that users can refer to without exiting the game
 * prevTime   -The previous time
 */
public abstract class Game extends AnimationTimer {
 protected Scene scene;
 protected World world;
 protected Camera camera;
 protected StackPane gamePane;
 protected Canvas canvas;
 protected VBox questUI = null;
 protected HBox toolbar;
 protected InventoryPane inventoryPane = null;
 protected Player player;
 protected ProgressBar health;
 protected ProgressBar hunger;
 protected ProgressBar exposure;
 protected ProgressBar thirst;
    protected Label days;
 protected InputManager i;
 protected QuestManager questManager;
 protected Point2D prevPosition;
 protected HelpMenu helpMenu;
 public SurvivalGuidePane gameSurvivalGuide;
 protected long prevTime = -1;
 protected Runnable onWin;
 protected Path worldPath;
 protected boolean winDisplayed;

 /**
  * The game constructor, sets up the graphics and starts the game
  *
  * @param scene is the Scene on which the game is displayed
  *
  * Variables:
  *
  * tb     -Displays the in game instructions when clicked
  *
  */
 Game(Scene scene, Path worldPath, Runnable onWin) {
  this.scene = scene;
  this.onWin = onWin;
  this.worldPath = worldPath;
  ToggleButton tb;
  questUI = new VBox();
  gameSurvivalGuide = new SurvivalGuidePane(scene, this);
  helpMenu = new HelpMenu();
  String seed = "0";
  try {
   seed = worldPath.getParent().getFileName().toString();
   System.out.println("seed: " + worldPath.getParent().getFileName());
  }
  catch (Throwable e) {
   System.out.println("Error " + e.getMessage());
   e.printStackTrace();
  }

  world = new World(Long.parseLong(seed), worldPath);
  if(world.getEntities().size() == 0) {
   world.setPlayer(new Player(world, "player"));
   world.getEntities().add(world.getPlayer());
  }
  player = world.getPlayer();
  player.setCycleCount(Animation.INDEFINITE);
  player.setCycle(new Duration(300));
  camera = new Camera(scene, world, player.getPosition());

  gamePane = new StackPane();
  canvas = new Canvas((int)scene.getWidth(), (int)scene.getHeight());
  health = new ProgressBar((double)player.getHealth() / player.getMaximumHealth());
  hunger = new ProgressBar ((double)player.getHunger()/player.getMaxHunger());
  exposure = new ProgressBar ((double)player.getExposure()/player.getMaxExposure());
  thirst = new ProgressBar((double)player.getThirst()/player.getMaxThirst());
  days = new Label("0");
  gamePane.getChildren().add(canvas);

  inventoryPane = new InventoryPane(scene, this, player){{
   setMaxWidth(scene.getWidth() / 3);
   setAlignment(Pos.BOTTOM_CENTER);
  }};


  gamePane.getChildren().add(new VBox(){{
   getChildren().add(questUI);
   getChildren().add(new VBox(){{
    setPadding(new Insets(50,30,30,30));
    setSpacing(10);
    getChildren().add(new HBox(){{
     getChildren().add(new Label("Health:\t\t"));
     setId("redbar");
     getChildren().add(health);
    }});
    getChildren().add(new HBox(){{
     getChildren().add(new Label("Hunger:\t\t"));
     setId("greenbar");
     getChildren().add(hunger);
    }});
    getChildren().add(new HBox(){{
     getChildren().add(new Label("Exposure:\t"));
     setId("bluebar");
     getChildren().add(exposure);
    }});
    getChildren().add(new HBox(){{
     getChildren().add(new Label("Thirst:\t\t"));
     setId("thirstbar");
     getChildren().add(thirst);
    }});
    getChildren().add(new HBox(){{
     getChildren().add(new Label("Days:\t\t"));
     setId("daysbar");
     getChildren().add(days);
    }});
   }});
  }});

  gamePane.getChildren().add(inventoryPane);
  player.setInventoryPane(inventoryPane);

  gamePane.getChildren().add(helpMenu);
  gamePane.setAlignment(helpMenu, Pos.BOTTOM_RIGHT);
  gamePane.setMargin(helpMenu, new Insets(0,25,65,0));

  tb = new ToggleButton("");
  tb.setId("helpbutton");
  tb.setOnAction(e -> helpMenu.setVisible(!helpMenu.isVisible()));
  gamePane.setMargin(tb, new Insets(0, 50, 20, 0));
  gamePane.getChildren().add(tb);
  gamePane.setAlignment(tb, Pos.BOTTOM_RIGHT);

  i = new InputManager(this, world, player);
  scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> i.keyPressed(e));
  scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> i.keyReleased(e));
  scene.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> i.mouseClicked(e));
  scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> i.mouseMoved(e));

  gamePane.getChildren().add(Main.settingsMenu);

  Main.settingsMenu.addGameButtons(gamePane, this);
  Main.settingsMenu.relocate(scene.getWidth()-SettingsMenu.getMenuWidth(4),50);

  gamePane.getChildren().add(gameSurvivalGuide);
  gameSurvivalGuide.setVisible(false);


  gamePane.getStylesheets().add(Game.class.getResource("/gamestylesheet.css").toExternalForm());



  questManager = new QuestManager(world, questUI, worldPath);
  initialiseQuests();
  questManager.resumeQuests();


  winDisplayed = Paths.get(worldPath.toString(), "won").toFile().exists();
 }

 /**
  * @returns the StackPane that is used to display the game
  */
 public StackPane getPane() {
  return gamePane;
 }

 /**
  * Processes user input
  * @param time the current in game time
  *
  * Variables:
  *
  * dt    -Stores the amout of time that has elapsed
  * displacement -Expresses the desired displacement as a 2D vector
  * p     -The coordinates of the block that the user clicked
  * c     -The identity of the block that the user clicked
  * b     -The identity of the block where the player is
  *
  */
 private void processInput(long time) {
  if(prevTime == -1) {
   prevTime = time;
   prevPosition = Main.point2d(player.getPosition());
   return;
  }
  long dt = time - prevTime;
  Point2D displacement = i.getDesiredDisplacement();
  if(displacement.magnitude() > player.getSpeed() * dt / 1e9)
   displacement = displacement.normalize().multiply(player.getSpeed() * dt / 1e9);
  player.move(displacement);

  Point p = World.blockCoordinate(i.getClickPosition());
  BlockKey c = world.getBlock((int)p.getX(), (int)p.getY(), 1);
  if(i.getClickButton() == MouseButton.PRIMARY &&
     c != null &&
     ResourceManager.getBlock(c) instanceof Destroyable &&
     i.getClickPosition().subtract(player.getPosition()).magnitude() <= 2) {
   player.destroy((int)p.getX(), (int)p.getY(), 1);
   player.setPosition(prevPosition);
   i.resetClick();
  } else if(i.getClickButton() == MouseButton.SECONDARY
      && c != null &&
     ResourceManager.getBlock(c) instanceof Interactable &&
     i.getClickPosition().subtract(player.getPosition()).magnitude() <= ((Interactable)ResourceManager.getBlock(c)).getInteractRadius()) {
   player.interact((int)p.getX(), (int)p.getY(), 1);
   player.setPosition(prevPosition);
   i.resetClick();
  }


  p = World.blockCoordinate(player.getPosition());
  BlockKey b = world.getBlock((int)p.getX(), (int)p.getY(), 1);
  if(b != null && ResourceManager.getBlock(b).isSolid()) {
   Point pp = World.blockCoordinate(prevPosition);
   // player.setPosition(new Point2D(pp.getX() + 0.5, pp.getY() + 0.5));
   player.setPosition(prevPosition);
   i.resetClick();
  }

  if(displacement.getX() == 0 && displacement.getY() == 0)
   player.stop();
  else
   player.play();
  camera.setPosition(player.getPosition());
  prevTime = time;
  prevPosition = Main.point2d(player.getPosition());
 }

 /**
  * Starts the stopwatch and the animation timer
  */
 public void start() {
  world.getStopwatch().start();
  System.out.println("Stopwatch started, " + world.getStopwatch().getElapsed() + " elapsed.");
  super.start();
 }

 /**
  * Pauses the stopwatch and stops the animation timer
  */
 public void pause(){
  world.getStopwatch().getElapsed();
  world.getStopwatch().pause();
  super.stop();
 }

 /**
  * Unpauses the stopwatch, starts the animation timer, and hides the pause menu
  * @param pauseMenu is a reference to the Pause Menu VBox
  */
 public void resume(PauseMenu pauseMenu){
  world.getStopwatch().getElapsed();
  world.getStopwatch().unpause();
  super.start();
  gamePane.getChildren().remove(pauseMenu);
 }

 /**
  * Unpauses the stopwatch and starts the animation timer
  */
 public void resume(){
  world.getStopwatch().getElapsed();
  world.getStopwatch().unpause();
  super.start();
 }

 /**
  * Closes the survival guide when it is on screen.
  */
 public void closeGuide(){
  gamePane.getChildren().remove(scene.lookup("#ingameguide"));
  resume();
 }

 /**
  * Updates the game every cycle
  * Redraws the screen, alters the player's stats, modifies the Fire objects
  */
 public void handle(long time) {
  processInput(time);
  List<Entity> _entities = new ArrayList<Entity>(world.getEntities());
  for(Entity e : _entities)
   e.exist();
  for(ListIterator<Entity> i = world.getEntities().listIterator(); i.hasNext(); ) {
   Entity e = i.next();
   if(e.getHealth() <= 0)
    i.remove();
  }
  if (player.getHealth() <= 0){
   gamePane.getChildren().add(new EndScreen(scene, this, false));
   pause();
  } else if(!winDisplayed && questManager.getQuests().size() == 0) {
   world.write();
   onWin.run();
   try {
    Files.write(Paths.get(worldPath.toString(), "won"), "nice".getBytes("UTF-8"));
   } catch (Throwable e) {
    System.out.println("Error " + e.getMessage());
    e.printStackTrace();
   }
   gamePane.getChildren().add(new EndScreen(scene, this, true));
   pause();
  }
  health.setProgress((double)player.getHealth() / player.getMaximumHealth());
  hunger.setProgress((double)player.getHunger() / player.getMaxHunger());
  exposure.setProgress((double)player.getExposure() / player.getMaxExposure());
  thirst.setProgress((double)player.getThirst() / player.getMaxThirst());
  this.days.setText(Long.toString(world.getStopwatch().getElapsed() / 1000000000 / 600));
  drawScreen();
 }

 /**
  * Draws the screen using the Camera object
  *
  * Variables:
  *
  * g                              -A GraphicsContext object, used to draw images
  * r                              -A rectangle2D object that determines what parts of the world are visible
  * maxRatio                       -The ratio of the rectangle side lengths
  * maxS, minS                     -Used to calculate screen bounds
  * sw, se, nw                     -The block coordinates of the South West, South East, and North West blocks respectively
  * m                              -The block coordinate of the mouse
  * screen X, screenY, screenL     -The screen coordinates
  * playerX, playerY               -The player coordinates
  * c                              -The location fo the mouse on the screen
  * clickX, clickY                 -The scene coordinates of where the user clicked
  */
 public void drawScreen() {
  GraphicsContext g = canvas.getGraphicsContext2D();
  Rectangle2D r = camera.getViewBounds(); // works as intended
  double maxRatio = Math.max(scene.getWidth(), scene.getHeight()) / Math.max(r.getWidth(), r.getHeight());
  double maxS = Math.max(scene.getWidth(), scene.getHeight());
  double minS = Math.min(scene.getWidth(), scene.getHeight());
  Point sw = World.blockCoordinate(new Point2D(r.getMinX(), r.getMinY())); // These work
  Point se = World.blockCoordinate(new Point2D(r.getMaxX(), r.getMinY())); // These work
  Point nw = World.blockCoordinate(new Point2D(r.getMinX(), r.getMaxY())); // These work

  for(int z = 0; z < 2; z++) {
   for(int i = (int)sw.getX(); i <= (int)se.getX(); i++) {
    for(int j = (int)sw.getY(); j <= (int)nw.getY(); j++) {
     double screenX = (i - camera.getX()) * maxRatio + scene.getWidth()/2;
     double screenY = scene.getHeight() - ((j - camera.getY()) * maxRatio + scene.getHeight() / 2);
     double screenL = 1 / camera.getBlockFactor() * maxS;
     BlockKey b = world.getBlock(i, j, z);
     try{
      if(b != null){
       g.drawImage(ResourceManager.getBlock(b).getImage(),
            screenX,
            screenY - screenL,
            screenL,
            screenL);
      }
     }catch(NullPointerException e){}
    }
   }
  }

  for(Entity e : world.getEntities()) {
   double screenX = (e.getX() - e.getRadius() - camera.getX()) * maxRatio + scene.getWidth()/2;
   double screenY = scene.getHeight() - ((e.getY() - e.getRadius() - camera.getY()) * maxRatio + scene.getHeight() / 2);
   double screenL = 2 * e.getRadius() / camera.getBlockFactor() * maxS;
   g.drawImage(e.getImage(), screenX, screenY - screenL, screenL, screenL);
  }

  Point m = World.blockCoordinate(i.getWorldMouseCoordinates());
  double screenX = (m.getX() - camera.getX()) * maxRatio + scene.getWidth()/2;
  double screenY = scene.getHeight() - ((m.getY() - camera.getY()) * maxRatio + scene.getHeight() / 2);
  double screenL = 1 / camera.getBlockFactor() * maxS;
  g.strokeRect(screenX, screenY - screenL, screenL, screenL);
  double playerX = (player.getX() - camera.getX()) * maxRatio + scene.getWidth()/2;
  double playerY = scene.getHeight() - ((player.getY() - camera.getY()) * maxRatio + scene.getHeight() / 2);
  Point2D c = i.getClickPosition();
  double clickX = (c.getX() - camera.getX()) * maxRatio + scene.getWidth()/2;

  double clickY = scene.getHeight() - ((c.getY() - camera.getY()) * maxRatio + scene.getHeight() / 2);
  g.strokeLine(playerX, playerY, clickX, clickY);



  g.setFill(new Color(0, 0, 0.2, 1 - world.getLightLevel()));
  g.fillRect(0, 0, scene.getWidth(), scene.getHeight());
 }

 /**
  * Stops all quests
  */
 public void killQuests() {
  questManager.killQuests();
 }

 /**
  * Initializes the quest objects and starts the first quest
  */
 public abstract void initialiseQuests();

 public World getWorld() {
  return world;
 }

 /**
  * @returns the Scene on which everything is drawn
  */
 public Scene getScene() {
  return scene;
 }

 /**
  * @returns the Camera object used to calculate what is and isn't on screen
  */
 public Camera getCamera() {
  return camera;
 }

 /**
  * @returns the QuestManager that is used to manage all of the quests
  */
 public QuestManager getQuestManager() {
  return questManager;
 }

 /**
  * @return The player of the game.
  */
 public Player getPlayer() {
  return player;
 }
}
