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

	private static final String TAG_KEY = TanpopoNBTTags.ITEM_ATTACHMENT;

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

	public ItemStack getContainerModeTool(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TAG_KEY, 10))
		{
			return ItemStack.loadItemStackFromNBT(nbtStack.getCompoundTag(TAG_KEY));
		}

		return new ItemStack(this.getDefaultModeTool());
	}

	public void setContainerModeTool(ItemStack stack, ItemStack stackFinished)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack == null)
		{
			nbtStack = new NBTTagCompound();
		}

		NBTTagCompound nbtFnished = new NBTTagCompound();

		stackFinished.stackSize = 1;
		stackFinished.setItemDamage(0);
		stackFinished.writeToNBT(nbtFnished);

		nbtStack.setTag(TAG_KEY, nbtFnished);

		stack.setTagCompound(nbtStack);
	}

	public boolean isBroken(ItemStack stack)
	{
		return (stack.getItemDamage() == 1);
	}

}
