package schr0.tanpopo;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHandlerItemEssenceGlassBottle implements IFluidHandler, ICapabilityProvider
{

	private static final int CAPACITY = TanpopoFluids.BOTTLE_VOLUME;
	private final ItemStack container;

	public FluidHandlerItemEssenceGlassBottle(ItemStack container)
	{
		this.container = container;
	}

	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return new FluidTankProperties[]
		{
				new FluidTankProperties(this.getFluid(), CAPACITY)
		};
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		FluidStack fluidStack = this.getFluid();

		if (fluidStack != null)
		{
			return 0;
		}
		else
		{
			if (doFill)
			{
				this.setFill();
			}

			return CAPACITY;
		}
	}

	@Override
	@Nullable
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		FluidStack fluidStack = this.getFluid();

		if ((fluidStack != null && resource != null) && fluidStack.isFluidEqual(resource))
		{
			return this.drain(resource.amount, doDrain);
		}
		else
		{
			return null;
		}
	}

	@Override
	@Nullable
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		FluidStack fluidStack = this.getFluid();

		if (fluidStack != null)
		{
			if (doDrain)
			{
				this.setEmpty();
			}

			return fluidStack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}

		return null;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private FluidStack getFluid()
	{
		if (ItemEssenceGlassBottle.isFill(this.container))
		{
			return new FluidStack(TanpopoFluids.ESSENCE, CAPACITY);
		}
		else
		{
			return null;
		}
	}

	private void setFill()
	{
		this.container.deserializeNBT(new ItemStack(TanpopoItems.ESSENCE_GLASS_BOTTLE).serializeNBT());
	}

	private void setEmpty()
	{
		this.container.deserializeNBT(new ItemStack(Items.GLASS_BOTTLE).serializeNBT());
	}

}
