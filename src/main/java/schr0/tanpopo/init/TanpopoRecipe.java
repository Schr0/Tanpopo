package schr0.tanpopo.init;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Lists;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import schr0.tanpopo.TanpopoVanillaHelper;

public class TanpopoRecipe
{

	public void init()
	{
		addRecipePlantFlower();
		addRecipeMaterial();
		addRecipeEssence();
		addRecipeModeTool();
		addSmeltingPlantFlower();
	}

	private static void addRecipePlantFlower()
	{
		for (int age = 0; age <= TanpopoBlocks.META_PLANT_FLOWER; age++)
		{
			ItemStack stackFlower = new ItemStack(TanpopoBlocks.PLANT_FLOWER, 1, age);

			if (1 <= age)
			{
				int amountRoots = (age + 1);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_ROOTS, amountRoots), new Object[]
				{
						"X  ",
						"   ",
						"   ",

						'X', stackFlower,
				}));
			}

			if (2 <= age)
			{
				int amountLeaf = 2;
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_LEAF, amountLeaf), new Object[]
				{
						" X ",
						"   ",
						"   ",

						'X', stackFlower,
				}));
			}

			if (5 <= age)
			{
				int amountStalk = 1;
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_STALK, amountStalk), new Object[]
				{
						"   ",
						"X  ",
						"   ",

						'X', stackFlower,
				}));
			}

			if (6 <= age && age <= 11)
			{
				int amountPetal = (age == 11) ? (8) : Math.min(((age - 2) * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_PETAL, amountPetal), new Object[]
				{
						"   ",
						" X ",
						"   ",

						'X', stackFlower,
				}));
			}

			if (12 <= age)
			{
				int amountFluff = Math.min(((age - 7) * 2), 16);
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoItems.MATERIAL_FLUFF, amountFluff), new Object[]
				{
						"   ",
						" X ",
						"   ",

						'X', stackFlower,
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
					"XXX",
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
		if (TanpopoVanillaHelper.isNotEmptyItemStack(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE)))
		// if (UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE) != null)
		{
			ItemStack universalBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE);

			GameRegistry.addRecipe(new ShapelessOreRecipe(universalBucket, new Object[]
			{
					Items.BUCKET, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE,
			}));
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoBlocks.ESSENCE_CHARCOAL_BLOCK), new Object[]
		{
				"XXX",
				"XXX",
				"XXX",

				'X', new ItemStack(TanpopoItems.ESSENCE_CHARCOAL),
		}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TanpopoItems.ESSENCE_CHARCOAL, 9), new Object[]
		{
				new ItemStack(TanpopoBlocks.ESSENCE_CHARCOAL_BLOCK),
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TanpopoBlocks.ESSENCE_IRON_INGOT_BLOCK), new Object[]
		{
				"XXX",
				"XXX",
				"XXX",

				'X', new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT),
		}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT, 9), new Object[]
		{
				new ItemStack(TanpopoBlocks.ESSENCE_IRON_INGOT_BLOCK),
		}));
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

		HashMap<Item, Item> tools = new HashMap<Item, Item>()
		{
			{
				put(TanpopoItems.TOOL_MATTOCK, TanpopoItems.ATTACHMENT_MATTOCK);
				put(TanpopoItems.TOOL_FELLING_AXE, TanpopoItems.ATTACHMENT_FELLING_AXE);
				put(TanpopoItems.TOOL_MOWING_HOE, TanpopoItems.ATTACHMENT_MOWING_HOE);
			}
		};

		for (Item tool : tools.keySet())
		{
			Item attachment = tools.get(tool);

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(tool), new Object[]
			{
					" X ",
					"Y  ",
					"   ",

					'X', new ItemStack(attachment),
					'Y', new ItemStack(TanpopoItems.MATERIAL_STALK),
			}));
		}
	}

	private static void addSmeltingPlantFlower()
	{
		for (int age = 0; age <= TanpopoBlocks.META_PLANT_FLOWER; age++)
		{
			int amount = (age + 1);

			GameRegistry.addSmelting(new ItemStack(TanpopoBlocks.PLANT_FLOWER, 1, age), new ItemStack(TanpopoItems.MATERIAL_MASS, amount), (0.2F * (float) amount));
		}

		ArrayList<Item> plantMaterialItems = Lists.newArrayList(Item.getItemFromBlock(TanpopoBlocks.PLANT_ROOTS), TanpopoItems.MATERIAL_LEAF, TanpopoItems.MATERIAL_STALK, TanpopoItems.MATERIAL_ROOTS);

		for (Item item : plantMaterialItems)
		{
			GameRegistry.addSmelting(new ItemStack(item), new ItemStack(TanpopoItems.MATERIAL_MASS), 0.2F);
		}
	}

}
