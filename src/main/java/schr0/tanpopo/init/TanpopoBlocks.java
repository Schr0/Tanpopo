package schr0.tanpopo.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.block.BlockEssence;
import schr0.tanpopo.block.BlockEssenceCauldron;
import schr0.tanpopo.block.BlockFluffCushion;
import schr0.tanpopo.block.BlockMassPlant;
import schr0.tanpopo.block.BlockPlantFlower;
import schr0.tanpopo.block.BlockPlantRoots;
import schr0.tanpopo.block.itemblock.ItemBlockEssence;
import schr0.tanpopo.block.itemblock.ItemBlockEssenceCauldron;
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

	public static final String NAME_PLANT_ROOTS = "plant_roots";
	public static final String NAME_PLANT_FLOWER = "plant_flower";
	public static final String NAME_MASS_PLANT = "mass_plant";
	public static final String NAME_ESSENCE = TanpopoFluids.NAME_ESSENCE;
	public static final String NAME_ESSENCE_CAULDRON = "essence_cauldron";
	public static final String NAME_FLUFF_CUSHION = "fluff_cushion";

	public static final int META_PLANT_ROOTS = 0;
	public static final int META_PLANT_FLOWER = 15;
	public static final int META_MASS_PLANT = 4;
	public static final int META_ESSENCE = 0;
	public static final int META_ESSENCE_CAULDRON = 3;
	public static final int META_FLUFF_CUSHION = 15;

	public static final Material MATERIAL_LIQUID_ESSENCE = new MaterialLiquid(MapColor.TNT);

	static
	{
		PLANT_ROOTS = new BlockPlantRoots().setUnlocalizedName(NAME_PLANT_ROOTS).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		PLANT_FLOWER = new BlockPlantFlower().setUnlocalizedName(NAME_PLANT_FLOWER).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		MASS_PLANT = new BlockMassPlant().setUnlocalizedName(NAME_MASS_PLANT).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		ESSENCE = new BlockEssence().setUnlocalizedName(NAME_ESSENCE).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		ESSENCE_CAULDRON = new BlockEssenceCauldron().setUnlocalizedName(NAME_ESSENCE_CAULDRON).setCreativeTab(TanpopoCreativeTabs.BLOCK);
		FLUFF_CUSHION = new BlockFluffCushion().setUnlocalizedName(NAME_FLUFF_CUSHION).setCreativeTab(TanpopoCreativeTabs.BLOCK);
	}

	public void init()
	{
		register();
	}

	private static void register()
	{
		TanpopoForgeRegistry.registerBlock(PLANT_ROOTS, new ItemBlockPlantRoots(PLANT_ROOTS), NAME_PLANT_ROOTS, META_PLANT_ROOTS);
		TanpopoForgeRegistry.registerBlock(PLANT_FLOWER, new ItemBlockPlantFlower(PLANT_FLOWER), NAME_PLANT_FLOWER, META_PLANT_FLOWER);
		TanpopoForgeRegistry.registerBlock(MASS_PLANT, new ItemBlockMassPlant(MASS_PLANT), NAME_MASS_PLANT, META_MASS_PLANT);
		TanpopoForgeRegistry.registerBlock(ESSENCE, new ItemBlockEssence(ESSENCE), NAME_ESSENCE, META_ESSENCE);
		TanpopoForgeRegistry.registerBlock(ESSENCE_CAULDRON, new ItemBlockEssenceCauldron(ESSENCE_CAULDRON), NAME_ESSENCE_CAULDRON, META_ESSENCE_CAULDRON);
		TanpopoForgeRegistry.registerBlock(FLUFF_CUSHION, new ItemBlockFluffCushion(FLUFF_CUSHION), NAME_FLUFF_CUSHION, META_FLUFF_CUSHION);
	}

	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		registerModel();
		registerModelFluid();
	}

	@SideOnly(Side.CLIENT)
	private static void registerModel()
	{
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(PLANT_ROOTS), META_PLANT_ROOTS);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(PLANT_FLOWER), META_PLANT_FLOWER);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(MASS_PLANT), META_MASS_PLANT);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(ESSENCE_CAULDRON), META_ESSENCE_CAULDRON);
		TanpopoModelLoader.registerModel(Item.getItemFromBlock(FLUFF_CUSHION), META_FLUFF_CUSHION);
	}

	@SideOnly(Side.CLIENT)
	private static void registerModelFluid()
	{
		TanpopoModelLoader.registerModelFluid(ESSENCE, NAME_ESSENCE);
	}

}
