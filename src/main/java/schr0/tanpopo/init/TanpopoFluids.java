package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import schr0.tanpopo.Tanpopo;

public class TanpopoFluids
{

	public static final Fluid ESSENCE;

	public static final String NAME_ESSENCE = "fluid_essence";

	public static final int BOTTLE_VOLUME = (Fluid.BUCKET_VOLUME / 4);

	static
	{
		ESSENCE = new Fluid(NAME_ESSENCE, new ResourceLocation(Tanpopo.MOD_DOMAIN + "blocks/fluid_essence_still"), new ResourceLocation(Tanpopo.MOD_DOMAIN + "blocks/fluid_essence_flow")).setUnlocalizedName(NAME_ESSENCE);
	}

	public void init()
	{
		register();
		registerContenerItem();
	}

	private static void register()
	{
		FluidRegistry.registerFluid(ESSENCE);
	}

	private static void registerContenerItem()
	{
		TanpopoForgeRegistry.registerUniversalBucket(ESSENCE);

		FluidContainerRegistry.registerFluidContainer(new FluidStack(ESSENCE, BOTTLE_VOLUME), new ItemStack(TanpopoItems.ESSENCE_GLASS_BOTTLE), new ItemStack(Items.GLASS_BOTTLE));
	}

}
