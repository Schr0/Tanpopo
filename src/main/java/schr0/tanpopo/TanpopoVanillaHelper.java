package schr0.tanpopo;

import net.minecraft.item.ItemStack;

public class TanpopoVanillaHelper
{

	public static boolean isNotEmptyItemStack(ItemStack stack)
	{
		return (stack != null);
	}

	public static ItemStack getEmptyItemStack()
	{
		return (ItemStack) null;
	}

	public static void setEmptyItemStack(ItemStack stack)
	{
		stack = null;
	}

	/*
		public static void setItemStackStackSize(ItemStack stack, int newStackSize)
		{
			stack.stackSize = newStackSize;
		}
	//*/

}
