package daysgame;

/**
 * Harit Kapadia, Jack Farley
 * Ms. Krasteva
 * 2019/June/02
 */

/**
 * Creates a tree block
 */
public class BlockTree extends Block implements Destroyable {
        
        /**
         * Class constructor, calls the block constructor
         */
        BlockTree() {
                super("img/tree_small.png");
        }
        
        /**
         * @returns true if the block has a transparent background
         */
        public boolean isTransparent() {
                return true;
        }
        
        /**
         * Overrides the onDestroy method of the Destroyable class
         * Splits the tree into 4 wood objects
         * @param world is a reference to the game world object
         * @param x is the tree's x coordinate
         * @param y is the tree's y coordinate
         * @param z is the tree's z coordinate
         */
        public void onDestroy(World world, int x, int y, int z) {
                world.setBlock(x - 1, y, z, BlockKey.WOOD);
                world.setBlock(x + 1, y, z, BlockKey.WOOD);
                world.setBlock(x, y - 1, z, BlockKey.WOOD);
                world.setBlock(x, y + 1, z, BlockKey.WOOD);
                synchronized(this) {
                        notifyAll();
                }
        }
        
        /**
         * @returns true if the object can't be walked through
         */
        public boolean isSolid() {
                return true;
        }
}
