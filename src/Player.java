/**
 * Harit Kapadia, Jack Farley
 * Ms. Krasteva
 * 2019/June/02
 */

import java.util.*;
import javafx.util.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;

public class Player extends Entity {
        ItemKey[] inventory;
        ItemKey[] toolbar;
        EnumMap<Direction, Image[]> sprites;
        Image image;
        
        Player(World world) {
                super(world);
                position = new Point2D(0, 0);
                toolbar = new ItemKey[5];
                toolbar[0] = ItemKey.KNIFE;
                inventory = new ItemKey[8];
                inventory[1] = ItemKey.KNIFE;
                
                try {
                        sprites = new EnumMap<Direction, Image[]>(Direction.class){{
                                for(Direction d : Direction.values()) {
                                        Image[] images = new Image[3];
                                        System.out.print(d + "\t");
                                        for(int i = 0; i < images.length; i++) {
                                                images[i] = new Image("Characters/sprite" + (i + 1 + d.ordinal() * 3) + ".png");
                                                System.out.print((i + 1 + d.ordinal() * 3) + " ");
                                                
                                        }
                                        System.out.println();
                                        
                                        put(d, images);
                                }
                        }};
                } catch(Exception e) {
                        e.printStackTrace();
                }
                
                image = sprites.get(Direction.DOWN)[2];
                
                setCycleCount(Animation.INDEFINITE);
                setCycleDuration(new Duration(300));
        }
        
        
        
        public void interpolate(double frac) {
                int frame = ((int)(frac * 3) + 1) % 3;
                image = sprites.get(facing)[frame];
        }
        
        
        public void stop() {
                image = sprites.get(facing)[1];
                super.stop();
        }
        
        public void destroy(int x, int y, int z) {
                world.destroyBlock(x, y, z);
        }
        
        
        
        
        public void activateItem(int inventoryIndex) {}
        
        public void move(Point2D displacement) {
                position = position.add(displacement);
        }
        
        public Image getImage() {
                return image;
        }
        
        public boolean isTransparent() {
                return true;
        }
        
        public ItemKey[] getToolbar() {
                return toolbar;
        }
        
        public void setToolbar(int i, ItemKey k) {
                toolbar[i] = k;
        }
        
        public ItemKey[] getInventory() {
                return inventory;
        }
}
