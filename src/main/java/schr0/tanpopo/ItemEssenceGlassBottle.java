package schr0.tanpopo;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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
		return (stack != null) && (stack.getItem() == Items.GLASS_BOTTLE);
	}

	public static boolean isFill(ItemStack stack)
	{
		return (stack != null) && (stack.getItem() == TanpopoItems.ESSENCE_GLASS_BOTTLE);
	}

}