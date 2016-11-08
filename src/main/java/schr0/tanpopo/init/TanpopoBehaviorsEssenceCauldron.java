package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.item.ItemModeToolAttachment;

public class TanpopoBehaviorsEssenceCauldron
{

	private static final EssenceCauldronCraft RECIPE_COAL = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return Items.COAL;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			return 5 * 20;
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.getItemDamage() == 1)
			{
				return new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL);
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ATTACHMENT_MATTOCK = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_MATTOCK;
		}

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			return 60 * 20;
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.getItem() instanceof ItemModeToolAttachment)
			{
				if (((ItemModeToolAttachment) stack.getItem()).isBroken(stack))
				{
					stack.setItemDamage(0);

					return stack;
				}
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ATTACHMENT_FELLING_AXE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_FELLING_AXE;
		}

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			return 60 * 20;
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.getItem() instanceof ItemModeToolAttachment)
			{
				if (((ItemModeToolAttachment) stack.getItem()).isBroken(stack))
				{
					stack.setItemDamage(0);

					return stack;
				}
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft RECIPE_ATTACHMENT_MOWING_HOE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_MOWING_HOE;
		}

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			return 60 * 20;
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.getItem() instanceof ItemModeToolAttachment)
			{
				if (((ItemModeToolAttachment) stack.getItem()).isBroken(stack))
				{
					stack.setItemDamage(0);

					return stack;
				}
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft RECIPE_TOOL_MATTOCK = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.TOOL_MATTOCK;
		}

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			int cost = stack.getItemDamage() / 100;

			cost = Math.max(1, cost);
			cost = Math.min(4, cost);

			return cost;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			int sec = stack.getItemDamage() / 10;

			sec = Math.max(10, sec);
			sec = Math.min(50, sec);

			return (sec * 20);
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.isItemDamaged())
			{
				stack.setItemDamage(0);

				return stack;
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft RECIPE_TOOL_FELLING_AXE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.TOOL_FELLING_AXE;
		}

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			int cost = stack.getItemDamage() / 100;

			cost = Math.max(1, cost);
			cost = Math.min(4, cost);

			return cost;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			int sec = stack.getItemDamage() / 10;

			sec = Math.max(10, sec);
			sec = Math.min(50, sec);

			return (sec * 20);
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.isItemDamaged())
			{
				stack.setItemDamage(0);

				return stack;
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft RECIPE_TOOL_MOWING_HOE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.TOOL_MOWING_HOE;
		}

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			int cost = stack.getItemDamage() / 100;

			cost = Math.max(1, cost);
			cost = Math.min(4, cost);

			return cost;
		}

		@Override
		public int getTickTime(ItemStack stack)
		{
			int sec = stack.getItemDamage() / 10;

			sec = Math.max(10, sec);
			sec = Math.min(50, sec);

			return (sec * 20);
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.isItemDamaged())
			{
				stack.setItemDamage(0);

				return stack;
			}

			return super.getResult(stack);
		}

	};

	public void init()
	{
		register();
	}

	private static void register()
	{
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_COAL);

		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ATTACHMENT_MATTOCK);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ATTACHMENT_FELLING_AXE);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_ATTACHMENT_MOWING_HOE);

		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_TOOL_MATTOCK);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_TOOL_FELLING_AXE);
		TanpopoRegistry.registerEssenceCauldronCraft(RECIPE_TOOL_MOWING_HOE);
	}

}
