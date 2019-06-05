/**
 * Harit Kapadia, Jack Farley
 * Ms. Krasteva
 * 2019/June/02
 */

import javafx.scene.image.Image;

public class PortableBlock extends Block implements Interactable {
	private ItemKey equivalentItem;

	public PortableBlock(String file, ItemKey equivalentItem){
		super (file);
		this.equivalentItem = equivalentItem;
	}

	public Image getImage() {
		return ResourceManager.getItem(equivalentItem).getImage();
	}

	public boolean isTransparent() {
		return true;
	}

	public boolean isSolid() {
		return false;
	}

	public void onInteract(Entity e, World world, int x, int y, int z) {
		for (int i = 0; i < e.getInventory().length; i++){
			if(e.getInventory(i) == null) {
				e.setInventory(i, equivalentItem);
				if(e instanceof Player)
					((Player)e).updateInventoryPaneSlot(i);
				world.setBlockUnsafe(x, y, z, null);
				synchronized(this) {
					notifyAll();
				}
				return;
			}
		}
	}

	public double getInteractRadius() {
		return 2;
	}
}
