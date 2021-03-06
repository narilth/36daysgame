package daysgame;

/**
 * Harit Kapadia, Jack Farley
 * Ms. Krasteva
 * 2019/June/02
 */

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.concurrent.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

/**
 * The QuestManager class is used to manage all of the quests, it includes methods for adding quests, removing quests, starting quests, etc.
 *
 * Variables:
 *
 * quests     -A List of Quest objects
 * world      -A reference to the game World object that the player is in
 * ui         -Stores the user interface for the quest
 *
 */
public class QuestManager {
        private List<Quest> quests = new LinkedList<Quest>();
        private World world;
        private VBox ui;
        private Path worldPath;
        
        /**
         * Class constructor, initializes the variables and creates files in which to save the progress
         *
         * @param world is the game World where everything is
         * @param ui is the Quest user interface
         *
         */
        public QuestManager(World world, VBox ui, Path worldPath) {
                this.ui = ui;
                this.world = world;
                this.worldPath = worldPath;
                try {
                        Paths.get(worldPath.toString(), "/questlog").toFile().createNewFile();
                        Paths.get(worldPath.toString(), "/currentquest").toFile().createNewFile();
                }
                catch (Throwable e) {
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
                }
                
        }
        
        /**
         * Adds a quest to the quests List
         * @param q is the Quest to be added
         */
        public void addQuest(Quest q) {
                quests.add(q);
        }
        
        /**
         * Removes all quests
         */
        public void clearQuests(){
                quests.clear();
        }
        
        /**
         * Removes a quest from the user interface and the quests List as well as altering the quest save files
         * @param q is the Quest to be removed
         */
        public void removeQuest(Quest q) {
                ui.getChildren().remove(q.getQuestPane());
                quests.remove(q);
                try {
                        Files.write(Paths.get(worldPath.toString(), "/questlog"), (q.getQuestName() + "\n").getBytes("UTF-8"), StandardOpenOption.APPEND);
                }
                catch (Throwable e) {
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
                }
                
        }
        
        /**
         * Updates the file to store the progress of the current quest
         * @param q is the current Quest
         */
        public void updateCurrentQuest(Quest q) {
                try {
                        Files.write(Paths.get(worldPath.toString(), "/currentquest"), Arrays.asList(new String[]{q.getQuestName(), q.getStepsTaken() + ""}), Charset.forName("UTF-8"));
                        System.out.println("Current Quest written: " + q.getQuestName());
                        
                }
                catch (Throwable e) {
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
                }
        }
        
        /**
         * Unpauses the quests after the game has been paused or exited
         *
         * Variables:
         *
         * currQuest    -A List that stores all of the information contained in the Quest save files
         * r            -A BufferedReader object used to read the information stored in the Quest save files
         * line         -Used to read each line in the Quest save files
         * _quests      -A list of all the quests
         */
        public void resumeQuests() {
                try {
                        String out = "";
                        List<String> currQuest = Files.readAllLines(Paths.get(worldPath.toString(), "/currentquest"), Charset.forName("UTF-8"));
                        BufferedReader r = Files.newBufferedReader(Paths.get(worldPath.toString(), "/questlog"), Charset.forName("UTF-8"));
                        String line = null;
                        while((line = r.readLine()) != null) {
                                List<Quest> _quests = new ArrayList<Quest>(quests);
                                for(Quest v : _quests) {
                                        if(v.getQuestName().equals(line)) {
                                                System.out.println("Completed " + line);
                                                v.completeQuest();
                                                v.join();
                                                System.out.println("joined");
                                                out += line + "\n";
                                        }
                                }
                        }
                        r.close();
                        // System.out.println("questlog:" + out);
                        // for(Quest v : quests)
                        //     System.out.print(v.getQuestName() + " ");
                        // System.out.println();
                        Files.write(Paths.get(worldPath.toString(), "/questlog"), out.getBytes("UTF-8"));
                        System.out.println("lines: " + currQuest.size());
                        if(currQuest.size() != 0) {
                                System.out.println("currQuest found");
                                
                                for(Quest v : quests) {
                                        if(v.getQuestName().equals(currQuest.get(0))) {
                                                System.out.println("Set steps: " + v.getQuestName());
                                                
                                                v.setStepsTaken(Integer.parseInt(currQuest.get(1)));
                                                break;
                                        }
                                }
                        }
                }
                catch (Throwable e) {
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
                }
        }
        
        /**
         * Stops all the quests
         */
        public void killQuests() {
            for(Quest q : quests) {
                        q.stop();
                        try {
                        q.join();
                        } catch(InterruptedException e) {
                            System.out.println("Error " + e.getMessage());
                            e.printStackTrace();
                        }
            }
            try {
                BufferedReader b = new BufferedReader(new FileReader(Paths.get(worldPath.toString(), "/currentquest").toString()));
                String out = b.readLine() + "\n";
                out += Integer.toString(Integer.parseInt(b.readLine()) - 1);
                b.close();
                Files.write(Paths.get(worldPath.toString(), "/currentquest"), out.getBytes("UTF-8"));
            } catch(Throwable e) {                
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
            }
        }
        
        /**
         * @returns the list of quests
         */
        public List<Quest> getQuests() {
                return quests;
        }
        
        /**
         * Launches a quest
         * @param q is the Quest to be launched
         */
        public void startQuest(Quest q) {
                Platform.runLater(() -> {
                        ui.getChildren().add(q.getQuestPane());
                        if(q.getInstr() != null) {
                                q.getHelpMenu().setInstr(q.getInstr());
                        }
                });
                q.start();
        }
}
