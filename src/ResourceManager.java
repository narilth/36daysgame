import java.util.*;

public final class ResourceManager {
	private static final EnumMap<BlockKey, Block> blocks = new EnumMap<BlockKey, Block>(BlockKey.class){{
			put(BlockKey.GROUND, new BlockGround());
			put(BlockKey.WATER, new BlockWater());
			put(BlockKey.POISON, new BlockPoison());
			put(BlockKey.TREE, new BlockTree());
			put(BlockKey.HOGWEED, new BlockHogweed());
		}};

	public static Block getBlock(BlockKey b) {
		return blocks.get(b);
	}

	private static final EnumMap<ItemKey, Item> items = new EnumMap<ItemKey, Item>(ItemKey.class){{
			put(ItemKey.KNIFE, new ItemKnife());
		}};

	public static Item getItem(ItemKey b) {
		return items.get(b);
	}
}
