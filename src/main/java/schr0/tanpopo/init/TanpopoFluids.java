package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
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
		registerFluid(ESSENCE);

		FluidContainerRegistry.registerFluidContainer(new FluidStack(ESSENCE, BOTTLE_VOLUME), new ItemStack(TanpopoItems.ESSENCE_GLASS_BOTTLE), new ItemStack(Items.GLASS_BOTTLE));
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void registerFluid(Fluid fluid)
	{
		FluidRegistry.registerFluid(fluid);

		FluidRegistry.addBucketForFluid(fluid);

		if (UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid) != null)
		{
			ItemStack stack = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);

			((UniversalBucket) stack.getItem()).setContainerItem(Items.BUCKET);

			FluidContainerRegistry.registerFluidContainer(fluid, stack, new ItemStack(Items.BUCKET));
		}
	}

}
