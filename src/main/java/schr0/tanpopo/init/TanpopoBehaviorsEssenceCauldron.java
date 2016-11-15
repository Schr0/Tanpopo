package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.item.ItemModeToolAttachment;

public class TanpopoBehaviorsEssenceCauldron
{

	private static final EssenceCauldronCraft RECIPE_ITEM_COAL = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return Items.COAL;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if (stackKeyItem.getItemDamage() == 1)
			{
				return new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL);
			}

			return (ItemStack) null;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (5 * 20);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ITEM_IRON_INGOT = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return Items.IRON_INGOT;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			return new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT);
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (25 * 20);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ITEM_ATTACHMENT_MATTOCK = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_MATTOCK;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if ((stackKeyItem.getItem() instanceof ItemModeToolAttachment) && ((ItemModeToolAttachment) stackKeyItem.getItem()).isBroken(stackKeyItem))
			{
				stackKeyItem.setItemDamage(0);

				return stackKeyItem;
			}

			return (ItemStack) null;
		}

		@Override
		public int getEssenceCost(ItemStack stackKeyItem)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (50 * 20);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ITEM_ATTACHMENT_FELLING_AXE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_FELLING_AXE;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if ((stackKeyItem.getItem() instanceof ItemModeToolAttachment) && ((ItemModeToolAttachment) stackKeyItem.getItem()).isBroken(stackKeyItem))
			{
				stackKeyItem.setItemDamage(0);

				return stackKeyItem;
			}

			return (ItemStack) null;
		}

		@Override
		public int getEssenceCost(ItemStack stackKeyItem)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (50 * 20);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ITEM_ATTACHMENT_MOWING_HOE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_MOWING_HOE;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if ((stackKeyItem.getItem() instanceof ItemModeToolAttachment) && ((ItemModeToolAttachment) stackKeyItem.getItem()).isBroken(stackKeyItem))
			{
				stackKeyItem.setItemDamage(0);

				return stackKeyItem;
			}

			return (ItemStack) null;
		}

		@Override
		public int getEssenceCost(ItemStack stackKeyItem)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (50 * 20);
		}

	};

	public void init()
	{
		register();
	}

	private static void register()
	{
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ITEM_COAL);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ITEM_IRON_INGOT);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ITEM_ATTACHMENT_MATTOCK);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ITEM_ATTACHMENT_FELLING_AXE);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ITEM_ATTACHMENT_MOWING_HOE);
	}

}
