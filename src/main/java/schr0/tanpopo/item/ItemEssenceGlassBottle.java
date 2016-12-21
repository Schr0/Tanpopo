package schr0.tanpopo.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import schr0.tanpopo.TanpopoVanillaHelper;
import schr0.tanpopo.capabilities.FluidHandlerItemEssenceGlassBottle;
import schr0.tanpopo.init.TanpopoItems;

public class ItemEssenceGlassBottle extends Item
{

	public ItemEssenceGlassBottle()
	{
		this.setMaxStackSize(1);
		this.setContainerItem(Items.GLASS_BOTTLE);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new FluidHandlerItemEssenceGlassBottle(stack);
	}

	// TODO /* ======================================== MOD START =====================================*/

	public static boolean isEmpty(ItemStack stack)
	{
		return TanpopoVanillaHelper.isNotEmptyItemStack(stack) && (stack.getItem() == Items.GLASS_BOTTLE);
		// return (stack != null) && (stack.getItem() == Items.GLASS_BOTTLE);
	}

	public static boolean isFill(ItemStack stack)
	{
		return TanpopoVanillaHelper.isNotEmptyItemStack(stack) && (stack.getItem() == TanpopoItems.ESSENCE_GLASS_BOTTLE);
		// return (stack != null) && (stack.getItem() == TanpopoItems.ESSENCE_GLASS_BOTTLE);
	}

}
