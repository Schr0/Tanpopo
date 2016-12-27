package schr0.tanpopo.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import schr0.tanpopo.Tanpopo;
import schr0.tanpopo.block.BlockEssence;
import schr0.tanpopo.block.BlockEssenceCauldron;
import schr0.tanpopo.block.BlockEssenceCharcoalBlock;
import schr0.tanpopo.block.BlockEssenceIronIngotBlock;
import schr0.tanpopo.block.BlockFluffCushion;
import schr0.tanpopo.block.BlockMassPlant;
import schr0.tanpopo.block.BlockPlantFlower;
import schr0.tanpopo.block.BlockPlantRoots;
import schr0.tanpopo.block.itemblock.ItemBlockEssence;
import schr0.tanpopo.block.itemblock.ItemBlockEssenceCauldron;
import schr0.tanpopo.block.itemblock.ItemBlockEssenceIronIngotBlock;
import schr0.tanpopo.block.itemblock.ItemBlockEssenceCharcoalBlock;
import schr0.tanpopo.block.itemblock.ItemBlockFluffCushion;
import schr0.tanpopo.block.itemblock.ItemBlockMassPlant;
import schr0.tanpopo.block.itemblock.ItemBlockPlantFlower;
import schr0.tanpopo.block.itemblock.ItemBlockPlantRoots;

public class TanpopoBlocks
{

	public static final Block PLANT_ROOTS;
	public static final Block PLANT_FLOWER;
	public static final Block MASS_PLANT;
	public static final Block ESSENCE;
	public static final Block ESSENCE_CAULDRON;
	public static final Block FLUFF_CUSHION;
	// ↓ ver1.1.0 ↓
	public static final Block ESSENCE_CHARCOAL_BLOCK;
	public static final Block ESSENCE_IRON_INGOT_BLOCK;

	public static final Material MATERIAL_LIQUID_ESSENCE = new MaterialLiquid(MapColor.YELLOW);

	public static final String NAME_PLANT_ROOTS = "plant_roots";
	public static final String NAME_PLANT_FLOWER = "plant_flower";
	public static final String NAME_MASS_PLANT = "mass_plant";
	public static final String NAME_ESSENCE = TanpopoFluids.NAME_ESSENCE;
	public static final String NAME_ESSENCE_CAULDRON = "essence_cauldron";
	public static final String NAME_FLUFF_CUSHION = "fluff_cushion";
	public static final String NAME_ESSENCE_CHARCOAL_BLOCK = "essence_charcoal_block";
	public static final String NAME_ESSENCE_IRON_INGOT_BLOCK = "essence_iron_ingot_block";

	public static final int META_PLANT_ROOTS = 0;
	public static final int META_PLANT_FLOWER = 15;
	public static final int META_MASS_PLANT = 4;
	public static final int META_ESSENCE = 0;
	public static final int META_ESSENCE_CAULDRON = 3;
	public static final int META_FLUFF_CUSHION = 15;
	public static final int META_ESSENCE_CHARCOAL_BLOCK = 0;
	public static final int META_ESSENCE_IRON_INGOT_BLOCK = 0;

	static
	{
		PLANT_ROOTS = new BlockPlantRoots().setUnlocalizedName(NAME_PLANT_ROOTS).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		PLANT_FLOWER = new BlockPlantFlower().setUnlocalizedName(NAME_PLANT_FLOWER).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		MASS_PLANT = new BlockMassPlant().setUnlocalizedName(NAME_MASS_PLANT).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		ESSENCE = new BlockEssence().setUnlocalizedName(NAME_ESSENCE).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		ESSENCE_CAULDRON = new BlockEssenceCauldron().setUnlocalizedName(NAME_ESSENCE_CAULDRON).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		FLUFF_CUSHION = new BlockFluffCushion().setUnlocalizedName(NAME_FLUFF_CUSHION).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		ESSENCE_CHARCOAL_BLOCK = new BlockEssenceCharcoalBlock().setUnlocalizedName(NAME_ESSENCE_CHARCOAL_BLOCK).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		ESSENCE_IRON_INGOT_BLOCK = new BlockEssenceIronIngotBlock().setUnlocalizedName(NAME_ESSENCE_IRON_INGOT_BLOCK).setCreativeTab(TanpopoCreativeTabs.BLOCK);
	}

	public void init()
	{
		registerBlock(PLANT_ROOTS, new ItemBlockPlantRoots(PLANT_ROOTS), NAME_PLANT_ROOTS, META_PLANT_ROOTS);
		registerBlock(PLANT_FLOWER, new ItemBlockPlantFlower(PLANT_FLOWER), NAME_PLANT_FLOWER, META_PLANT_FLOWER);
		registerBlock(MASS_PLANT, new ItemBlockMassPlant(MASS_PLANT), NAME_MASS_PLANT, META_MASS_PLANT);
		registerBlock(ESSENCE, new ItemBlockEssence(ESSENCE), NAME_ESSENCE, META_ESSENCE);
		registerBlock(ESSENCE_CAULDRON, new ItemBlockEssenceCauldron(ESSENCE_CAULDRON), NAME_ESSENCE_CAULDRON, META_ESSENCE_CAULDRON);
		registerBlock(FLUFF_CUSHION, new ItemBlockFluffCushion(FLUFF_CUSHION), NAME_FLUFF_CUSHION, META_FLUFF_CUSHION);
		registerBlock(ESSENCE_CHARCOAL_BLOCK, new ItemBlockEssenceCharcoalBlock(ESSENCE_CHARCOAL_BLOCK), NAME_ESSENCE_CHARCOAL_BLOCK, META_ESSENCE_CHARCOAL_BLOCK);
		registerBlock(ESSENCE_IRON_INGOT_BLOCK, new ItemBlockEssenceIronIngotBlock(ESSENCE_IRON_INGOT_BLOCK), NAME_ESSENCE_IRON_INGOT_BLOCK, META_ESSENCE_IRON_INGOT_BLOCK);
	}

	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(PLANT_ROOTS), META_PLANT_ROOTS);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(PLANT_FLOWER), META_PLANT_FLOWER);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(MASS_PLANT), META_MASS_PLANT);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(ESSENCE_CAULDRON), META_ESSENCE_CAULDRON);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(FLUFF_CUSHION), META_FLUFF_CUSHION);
		TanpopoModelLoader.registerModelFluid(ESSENCE, NAME_ESSENCE);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(ESSENCE_CHARCOAL_BLOCK), META_ESSENCE_CHARCOAL_BLOCK);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(ESSENCE_IRON_INGOT_BLOCK), META_ESSENCE_IRON_INGOT_BLOCK);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void registerBlock(Block block, ItemBlock itemBlock, String name, int meta)
	{
		GameRegistry.register(block, new ResourceLocation(Tanpopo.MOD_ID, name));
		GameRegistry.register(itemBlock, new ResourceLocation(Tanpopo.MOD_ID, name));

		if (meta == 0)
		{
			OreDictionary.registerOre(name, block);
		}
		else
		{
			for (int i = 0; i <= meta; i++)
			{
				OreDictionary.registerOre(name + "_" + i, new ItemStack(block, 1, i));
			}
		}
	}

	private static void registerBlock(Block block, ItemBlock itemBlock, String name, int meta, String[] oreNames)
	{
		registerBlock(block, itemBlock, name, meta);

		for (String ore : oreNames)
		{
			OreDictionary.registerOre(ore, block);
		}
	}

}
