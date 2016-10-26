package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
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
		craftingPlantFlower();
		craftingMaterial();
		craftingEssence();
		smeltingPlantFlower();
	}

	private static void craftingPlantFlower()
	{
		int maxAge = TanpopoBlocks.META_PLANT_FLOWER;

		for (int age = 0; age <= maxAge; age++)
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

	private static void craftingMaterial()
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
		/*
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.DYE, 4, EnumDyeColor.YELLOW.getDyeDamage()), new Object[]
				{
						TanpopoItems.MATERIAL_PETAL,
				}));
		//*/
		GameRegistry.addRecipe(new ShapedOreRecipe(TanpopoBlocks.FLUFF_CUSHION, new Object[]
		{
				"XX ",
				"XX ",
				"   ",

				'X', new ItemStack(TanpopoItems.MATERIAL_FLUFF, 1),
		}));

		for (int color = 0; color <= 15; ++color)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TanpopoBlocks.FLUFF_CUSHION, 1, color), new Object[]
			{
					TanpopoBlocks.FLUFF_CUSHION, new ItemStack(Items.DYE, 1, EnumDyeColor.byMetadata(color).getDyeDamage()),
			}));
		}
	}

	private static void craftingEssence()
	{
		if (UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE) != null)
		{
			ItemStack universalBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE);

			GameRegistry.addRecipe(new ShapelessOreRecipe(universalBucket, new Object[]
			{
					Items.BUCKET, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE, TanpopoItems.ESSENCE_GLASS_BOTTLE,
			}));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL, 4), new Object[]
			{
					universalBucket, new ItemStack(Items.COAL, 1, 1), new ItemStack(Items.COAL, 1, 1), new ItemStack(Items.COAL, 1, 1), new ItemStack(Items.COAL, 1, 1),
			}));
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL), new Object[]
		{
				TanpopoItems.ESSENCE_GLASS_BOTTLE, new ItemStack(Items.COAL, 1, 1),
		}));
	}

	private static void smeltingPlantFlower()
	{
		int maxAge = TanpopoBlocks.META_PLANT_FLOWER;

		for (int age = 0; age <= maxAge; age++)
		{
			int stackSize = (age + 1);

			GameRegistry.addSmelting(new ItemStack(TanpopoBlocks.PLANT_FLOWER, 1, age), new ItemStack(TanpopoItems.MATERIAL_MASS, stackSize), (0.2F * (float) stackSize));
		}
	}

}
