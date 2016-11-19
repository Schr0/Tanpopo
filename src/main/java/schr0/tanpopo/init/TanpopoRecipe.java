package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class TanpopoRecipe
{

	public void init()
	{
		addRecipePlantFlower();
		addRecipeMaterial();
		addRecipeEssence();
		addRecipeModeTool();
		addSmeltingRecipe();
	}

	private static void addRecipePlantFlower()
	{
		for (int age = 0; age <= TanpopoBlocks.META_PLANT_FLOWER; age++)
		{
			ItemStack plantFlower = new ItemStack(TanpopoBlocks.PLANT_FLOWER, 1, age);

			if (1 <= age)
			{
				int rootsAmount = Math.min((age * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_ROOTS, rootsAmount), new Object[]
				{
						"X  ",
						"   ",
						"   ",

						'X', plantFlower,
				}));
			}

			if (2 <= age)
			{
				int leafAmount = Math.min(((age - 1) * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_LEAF, leafAmount), new Object[]
				{
						" X ",
						"   ",
						"   ",

						'X', plantFlower,
				}));
			}

			if (4 <= age)
			{
				int stalkAmount = Math.min(((age - 3) * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_STALK, stalkAmount), new Object[]
				{
						"   ",
						"X  ",
						"   ",

						'X', plantFlower,
				}));
			}

			if (6 <= age && age <= 11)
			{
				int petalAmount = (age == 11) ? (8) : Math.min(((age - 2) * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_PETAL, petalAmount), new Object[]
				{
						"   ",
						" X ",
						"   ",

						'X', plantFlower,
				}));
			}

			if (12 <= age)
			{
				int fluffAmount = Math.min(((age - 7) * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_FLUFF, fluffAmount), new Object[]
				{
						"   ",
						" X ",
						"   ",

						'X', plantFlower,
				}));
			}
		}
	}

	private static void addRecipeMaterial()
	{
		for (int itemMeta = 0; itemMeta <= 1; itemMeta++)
		{
			int blockMeta = (itemMeta == 0) ? (0) : (4);

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoBlocks.MASS_PLANT, 1, blockMeta), new Object[]
			{
					"XXX",
					"X X",
					"XXX",

					'X', new ItemStack(TanpopoItems.MATERIAL_MASS, 1, itemMeta),
			}));
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.DYE, 4, EnumDyeColor.YELLOW.getDyeDamage()), new Object[]
		{
				TanpopoItems.MATERIAL_PETAL,
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoBlocks.FLUFF_CUSHION), new Object[]
		{
				"XX ",
				"XX ",
				"   ",

				'X', new ItemStack(TanpopoItems.MATERIAL_FLUFF, 1),
		}));

		for (int color = 0; color <= TanpopoBlocks.META_FLUFF_CUSHION; ++color)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TanpopoBlocks.FLUFF_CUSHION, 1, color), new Object[]
			{
					TanpopoBlocks.FLUFF_CUSHION, new ItemStack(Items.DYE, 1, EnumDyeColor.byMetadata(color).getDyeDamage()),
			}));
		}
	}

	private static void addRecipeEssence()
	{
		if (UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE) != null)
		{
			ItemStack universalBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE);

			GameRegistry.addRecipe(new ShapelessOreRecipe(universalBucket, new Object[]
			{
					Items.BUCKET, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE,
			}));
		}
	}

	private static void addRecipeModeTool()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.ATTACHMENT_MATTOCK), new Object[]
		{
				"XX ",
				"XXX",
				"  X",

				'X', new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT),
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.ATTACHMENT_FELLING_AXE), new Object[]
		{
				"XXX",
				"XXX",
				"   ",

				'X', new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT),
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.ATTACHMENT_MOWING_HOE), new Object[]
		{
				"XXX",
				"X  ",
				"   ",

				'X', new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT),
		}));

		Item[] tools = new Item[]
		{
				TanpopoItems.TOOL_MATTOCK, TanpopoItems.TOOL_FELLING_AXE, TanpopoItems.TOOL_MOWING_HOE
		};

		Item[] attachments = new Item[]
		{
				TanpopoItems.ATTACHMENT_MATTOCK, TanpopoItems.ATTACHMENT_FELLING_AXE, TanpopoItems.ATTACHMENT_MOWING_HOE
		};

		for (int num = 0; num < tools.length; num++)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(tools[num]), new Object[]
			{
					" X ",
					"Y  ",
					"   ",

					'X', new ItemStack(attachments[num]),
					'Y', new ItemStack(TanpopoItems.MATERIAL_STALK),
			}));
		}
	}

	private static void addSmeltingRecipe()
	{
		for (int age = 0; age <= TanpopoBlocks.META_PLANT_FLOWER; age++)
		{
			int stackSize = (age + 1);

			GameRegistry.addSmelting(new ItemStack(TanpopoBlocks.PLANT_FLOWER, 1, age), new ItemStack(TanpopoItems.MATERIAL_MASS, stackSize), (0.2F * (float) stackSize));
		}
	}

}
