package schr0.tanpopo.capabilities;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidContainerRegistryWrapper;
import schr0.tanpopo.init.TanpopoFluids;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.item.ItemEssenceGlassBottle;

public class FluidHandlerItemEssenceGlassBottle implements IFluidHandler, ICapabilityProvider
{

	private static final int CAPACITY = TanpopoFluids.BOTTLE_VOLUME;
	private final ItemStack container;

	/**
	 * @author defeatedcrow 2017.5.26
	 */

	 /**
	 * 他modがボトルを容器登録している場合に備えて、FluidContainerRegistryWrapper を用意している<br>
	 * AttachCapabilitiesEventが競合した場合の策は現状なし
	 */
	private final FluidContainerRegistryWrapper wrapper;

	public FluidHandlerItemEssenceGlassBottle(ItemStack container)
	{
		this.container = container;
		wrapper = new FluidContainerRegistryWrapper(container);
	}

	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		// 場合分け
		if(ItemEssenceGlassBottle.isFill(container))
		{
			// タンポポエッセンスの場合
			return new FluidTankProperties[]{new FluidTankProperties(this.getFluid(), CAPACITY)};
		}
		else if(FluidContainerRegistry.isFilledContainer(container))
		{
			// その他の液体が入っている
			return wrapper.getTankProperties();
		}
		else
		{
			return new FluidTankProperties[] {};
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		// 液体種のチェック & Nullチェック
		if(matchFluid(resource))
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
		else
		{
			// エッセンス以外の場合はwrapperの処理を返す
			return wrapper.fill(resource, doFill);
		}

	}

	@Override
	@Nullable
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		// 液体種のチェック & Nullチェック
		if(matchFluid(resource))
		{
			FluidStack fluidStack = this.getFluid();

			if (fluidStack != null)
			{
				return this.drain(resource.amount, doDrain);
			}
			else
			{
				return null;
			}
		}
		else
		{
			// エッセンス以外の場合はwrapperの処理を返す
			return wrapper.drain(resource, doDrain);
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
			return wrapper.drain(maxDrain, doDrain);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY))
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
			return (FluidStack) null;
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

	/**
	 * @author defeatedcrow
	 */
	private boolean matchFluid(FluidStack fluidstack)
	{
		if(fluidstack != null && fluidstack.getFluid() != null)
		{
			return fluidstack.getFluid() == TanpopoFluids.ESSENCE;
		}
		else
		{
			return false;
		}
	}

}
