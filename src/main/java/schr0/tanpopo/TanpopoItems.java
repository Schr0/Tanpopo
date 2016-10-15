package schr0.tanpopo;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TanpopoItems
{

	public static final Item MATERIAL_ROOTS;
	public static final Item MATERIAL_LEAF;
	public static final Item MATERIAL_STALK;
	public static final Item MATERIAL_PETAL;
	public static final Item MATERIAL_FLUFF;
	public static final Item MATERIAL_MASS;
	public static final Item ESSENCE_GLASS_BOTTLE;
	public static final Item ESSENCE_SOLID_FUEL;
	public static final Item TOOL_MATTOCK;
	public static final Item TOOL_FELLING_AXE;
	public static final Item TOOL_MOWING_HOE;

	public static final String NAME_MATERIAL_ROOTS = "material_roots";
	public static final String NAME_MATERIAL_LEAF = "material_leaf";
	public static final String NAME_MATERIAL_STALK = "material_stalk";
	public static final String NAME_MATERIAL_PETAL = "material_petal";
	public static final String NAME_MATERIAL_FLUFF = "material_fluff";
	public static final String NAME_MATERIAL_MASS = "material_mass";
	public static final String NAME_ESSENCE_GLASS_BOTTLE = "essence_glass_bottle";
	public static final String NAME_ESSENCE_SOLID_FUEL = "essence_solid_fuel";
	public static final String NAME_TOOL_MATTOCK = "tool_mattock";
	public static final String NAME_TOOL_FELLING_AXE = "tool_felling_axe";
	public static final String NAME_TOOL_MOWING_HOE = "tool_mowing_hoe";

	public static final int META_MATERIAL_ROOTS = 0;
	public static final int META_MATERIAL_LEAF = 0;
	public static final int META_MATERIAL_STALK = 0;
	public static final int META_MATERIAL_PETAL = 0;
	public static final int META_MATERIAL_FLUFF = 0;
	public static final int META_MATERIAL_MASS = 1;
	public static final int META_ESSENCE_GLASS_BOTTLE = 0;
	public static final int META_ESSENCE_SOLID_FUEL = 0;
	public static final int META_TOOL_MATTOCK = 0;
	public static final int META_TOOL_FELLING_AXE = 0;
	public static final int META_TOOL_MOWING_HOE = 0;

	static
	{
		MATERIAL_ROOTS = new ItemMaterialRoots().setUnlocalizedName(NAME_MATERIAL_ROOTS).setCreativeTab(TanpopoCreativeTabs.ITEM);
		MATERIAL_LEAF = new ItemMaterialLeaf().setUnlocalizedName(NAME_MATERIAL_LEAF).setCreativeTab(TanpopoCreativeTabs.ITEM);
		MATERIAL_STALK = new ItemMaterialStalk().setUnlocalizedName(NAME_MATERIAL_STALK).setCreativeTab(TanpopoCreativeTabs.ITEM);
		MATERIAL_PETAL = new ItemMaterialPetal().setUnlocalizedName(NAME_MATERIAL_PETAL).setCreativeTab(TanpopoCreativeTabs.ITEM);
		MATERIAL_FLUFF = new ItemMaterialFluff().setUnlocalizedName(NAME_MATERIAL_FLUFF).setCreativeTab(TanpopoCreativeTabs.ITEM);
		MATERIAL_MASS = new ItemMaterialMass().setUnlocalizedName(NAME_MATERIAL_MASS).setCreativeTab(TanpopoCreativeTabs.ITEM);
		ESSENCE_GLASS_BOTTLE = new ItemEssenceGlassBottle().setUnlocalizedName(NAME_ESSENCE_GLASS_BOTTLE).setCreativeTab(TanpopoCreativeTabs.ITEM);
		ESSENCE_SOLID_FUEL = new ItemEssenceSolidFuel().setUnlocalizedName(NAME_ESSENCE_SOLID_FUEL).setCreativeTab(TanpopoCreativeTabs.ITEM);
		TOOL_MATTOCK = new ItemToolMattock().setUnlocalizedName(NAME_TOOL_MATTOCK).setCreativeTab(TanpopoCreativeTabs.ITEM);
		TOOL_FELLING_AXE = new ItemToolFellingAxe().setUnlocalizedName(NAME_TOOL_FELLING_AXE).setCreativeTab(TanpopoCreativeTabs.ITEM);
		TOOL_MOWING_HOE = new ItemToolMowingHoe().setUnlocalizedName(NAME_TOOL_MOWING_HOE).setCreativeTab(TanpopoCreativeTabs.ITEM);
	}

	public void init()
	{
		register();
	}

	/**
	 * Itemの登録.
	 */
	private static void register()
	{
		TanpopoForgeRegistry.registerItem(MATERIAL_ROOTS, NAME_MATERIAL_ROOTS, META_MATERIAL_ROOTS, new String[]
		{
				"string"
		});
		TanpopoForgeRegistry.registerItem(MATERIAL_LEAF, NAME_MATERIAL_LEAF, META_MATERIAL_LEAF, new String[]
		{
				"feather"
		});
		TanpopoForgeRegistry.registerItem(MATERIAL_STALK, NAME_MATERIAL_STALK, META_MATERIAL_STALK, new String[]
		{
				"stickWood"
		});
		TanpopoForgeRegistry.registerItem(MATERIAL_PETAL, NAME_MATERIAL_PETAL, META_MATERIAL_PETAL);
		TanpopoForgeRegistry.registerItem(MATERIAL_FLUFF, NAME_MATERIAL_FLUFF, META_MATERIAL_FLUFF);
		TanpopoForgeRegistry.registerItem(MATERIAL_MASS, NAME_MATERIAL_MASS, META_MATERIAL_MASS);
		TanpopoForgeRegistry.registerItem(ESSENCE_GLASS_BOTTLE, NAME_ESSENCE_GLASS_BOTTLE, META_ESSENCE_GLASS_BOTTLE);
		TanpopoForgeRegistry.registerItem(ESSENCE_SOLID_FUEL, NAME_ESSENCE_SOLID_FUEL, META_ESSENCE_SOLID_FUEL);
		TanpopoForgeRegistry.registerItem(TOOL_MATTOCK, NAME_TOOL_MATTOCK, META_TOOL_MATTOCK);
		TanpopoForgeRegistry.registerItem(TOOL_FELLING_AXE, NAME_TOOL_FELLING_AXE, META_TOOL_FELLING_AXE);
		TanpopoForgeRegistry.registerItem(TOOL_MOWING_HOE, NAME_TOOL_MOWING_HOE, META_TOOL_MOWING_HOE);
	}

	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		registerModel();
	}

	@SideOnly(Side.CLIENT)
	private static void registerModel()
	{
		TanpopoModelLoader.registerModel(MATERIAL_ROOTS, META_MATERIAL_ROOTS);
		TanpopoModelLoader.registerModel(MATERIAL_LEAF, META_MATERIAL_LEAF);
		TanpopoModelLoader.registerModel(MATERIAL_STALK, META_MATERIAL_STALK);
		TanpopoModelLoader.registerModel(MATERIAL_PETAL, META_MATERIAL_PETAL);
		TanpopoModelLoader.registerModel(MATERIAL_FLUFF, META_MATERIAL_FLUFF);
		TanpopoModelLoader.registerModel(MATERIAL_MASS, META_MATERIAL_MASS);
		TanpopoModelLoader.registerModel(ESSENCE_GLASS_BOTTLE, META_ESSENCE_GLASS_BOTTLE);
		TanpopoModelLoader.registerModel(ESSENCE_SOLID_FUEL, META_ESSENCE_SOLID_FUEL);
		TanpopoModelLoader.registerModel(TOOL_MATTOCK, META_TOOL_MATTOCK);
		TanpopoModelLoader.registerModel(TOOL_FELLING_AXE, META_TOOL_FELLING_AXE);
		TanpopoModelLoader.registerModel(TOOL_MOWING_HOE, META_TOOL_MOWING_HOE);
	}

}
