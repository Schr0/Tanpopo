package schr0.tanpopo.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoNBTTags;

public abstract class ItemModeToolAttachment extends Item
{

	public ItemModeToolAttachment()
	{
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		ItemStack stackFinished = this.getContainerModeTool(stack);

		if (stackFinished == null)
		{
			tooltip.add("NONE");
		}
		else
		{
			tooltip.add(stackFinished.getDisplayName());
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "_" + stack.getMetadata();
	}

	// TODO /* ======================================== MOD START =====================================*/

	public abstract Item getDefaultModeTool();

	public static boolean isBroken(ItemStack stack)
	{
		return (stack != null) && (stack.getItem() instanceof ItemModeToolAttachment) && (stack.getItemDamage() == 1);
	}

	public ItemStack getContainerModeTool(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TanpopoNBTTags.MODE_TOOL_CONTAINER, 10))
		{
			return ItemStack.loadItemStackFromNBT(nbtStack.getCompoundTag(TanpopoNBTTags.MODE_TOOL_CONTAINER));
		}

		return new ItemStack(this.getDefaultModeTool());
	}

	public void setContainerModeTool(ItemStack stack, ItemStack stackContainer)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack == null)
		{
			nbtStack = new NBTTagCompound();
		}

		NBTTagCompound nbtContainer = new NBTTagCompound();

		if (stackContainer.getItem() instanceof ItemModeTool)
		{
			stackContainer.stackSize = 1;

			stackContainer.setItemDamage(0);

			((ItemModeTool) stackContainer.getItem()).setMode(stackContainer, false);

			stackContainer.writeToNBT(nbtContainer);

			nbtStack.setTag(TanpopoNBTTags.MODE_TOOL_CONTAINER, nbtContainer);

			stack.setTagCompound(nbtStack);
		}

	}

}
