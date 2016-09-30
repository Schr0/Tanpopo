package schr0.tanpopo;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class TanpopoForgeRegistry
{

	public static void registerItem(Item item, String name, int meta)
	{
		GameRegistry.register(item, new ResourceLocation(Tanpopo.MOD_ID, name));

		if (meta == 0)
		{
			OreDictionary.registerOre(name, item);
		}
		else
		{
			for (int i = 0; i <= meta; i++)
			{
				OreDictionary.registerOre(name + "_" + i, new ItemStack(item, 1, i));
			}
		}
	}

	public static void registerItem(Item item, String name, int meta, String[] oreNames)
	{
		registerItem(item, name, meta);

		for (String ore : oreNames)
		{
			OreDictionary.registerOre(ore, item);
		}
	}

	public static void registerBlock(Block block, ItemBlock itemBlock, String name, int meta)
	{
		GameRegistry.register(block, new ResourceLocation(Tanpopo.MOD_ID, name));
		GameRegistry.register(itemBlock, new ResourceLocation(Tanpopo.MOD_ID, name));

		if (meta == 0)
		{
			OreDictionary.registerOre(name, block);
			OreDictionary.registerOre(name, itemBlock);
		}
		else
		{
			for (int i = 0; i <= meta; i++)
			{
				OreDictionary.registerOre(name + "_" + i, new ItemStack(block, 1, i));
				OreDictionary.registerOre(name + "_" + i, new ItemStack(itemBlock, 1, i));
			}
		}
	}

	public static void registerBlock(Block block, ItemBlock itemBlock, String name, int meta, String[] oreNames)
	{
		registerBlock(block, itemBlock, name, meta);

		for (String ore : oreNames)
		{
			OreDictionary.registerOre(ore, block);
			OreDictionary.registerOre(ore, itemBlock);
		}
	}

	public static void registerUniversalBucket(Fluid fluid)
	{
		FluidRegistry.addBucketForFluid(fluid);

		if (UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid) != null)
		{
			ItemStack stack = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);
			UniversalBucket universalBucket = (UniversalBucket) stack.getItem();

			universalBucket.setContainerItem(Items.BUCKET);

			FluidContainerRegistry.registerFluidContainer(fluid, stack, new ItemStack(Items.BUCKET));
		}
	}

}
