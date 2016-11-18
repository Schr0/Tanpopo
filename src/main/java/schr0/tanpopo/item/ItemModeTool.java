package schr0.tanpopo.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoNBTTags;

public abstract class ItemModeTool extends ItemTool
{

	private static final Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet();

	protected ItemModeTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn)
	{
		super(attackDamageIn, attackSpeedIn, materialIn, EFFECTIVE_BLOCKS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);

		tooltip.add(this.getModeText(this.getModeName(), stack, this.isMode(stack)));
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		if (this.canHarvestBlock(state))
		{
			return this.efficiencyOnProperMaterial;
		}

		return super.getStrVsBlock(stack, state);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (this.canChageMode(playerIn))
		{
			if (!worldIn.isRemote)
			{
				boolean isMode = this.isMode(itemStackIn);
				TextComponentString textItem = new TextComponentString(itemStackIn.getDisplayName());

				textItem.getStyle().setItalic(true);

				playerIn.addChatComponentMessage(new TextComponentString(textItem.getFormattedText() + " -> " + this.getModeText(this.getModeName(), itemStackIn, !isMode)));

				this.setMode(itemStackIn, !isMode);
			}

			playerIn.swingArm(hand);

			worldIn.playSound(playerIn, new BlockPos(playerIn), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);

			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}

	// TODO /* ======================================== MOD START =====================================*/

	@SideOnly(Side.CLIENT)
	public abstract TextComponentTranslation getModeName();

	// TODO
	public abstract Item getModeAttachment();

	@SideOnly(Side.CLIENT)
	public String getModeText(TextComponentTranslation textModeName, ItemStack stack, boolean isMode)
	{
		TextComponentTranslation textMode = this.getModeName();
		TextComponentTranslation textCondition;

		if (isMode)
		{
			textCondition = new TextComponentTranslation("item.tool.mode_enabled", new Object[0]);
			textCondition.getStyle().setColor(TextFormatting.GREEN);
		}
		else
		{
			textCondition = new TextComponentTranslation("item.tool.mode_disabled", new Object[0]);
			textCondition.getStyle().setColor(TextFormatting.DARK_RED);
		}

		textMode.getStyle().setColor(TextFormatting.AQUA);
		textCondition.getStyle().setBold(true);

		return new TextComponentString(textMode.getFormattedText() + " : " + textCondition.getFormattedText()).getFormattedText();
	}

	public boolean isMode(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TanpopoNBTTags.MODE_TOOL_FLAG, 3))
		{
			int value = nbtStack.getInteger(TanpopoNBTTags.MODE_TOOL_FLAG);

			return (value == 1);
		}

		return false;
	}

	public void setMode(ItemStack stack, boolean isMode)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack == null)
		{
			nbtStack = new NBTTagCompound();
		}

		int value = isMode ? (1) : (0);

		nbtStack.setInteger(TanpopoNBTTags.MODE_TOOL_FLAG, value);

		stack.setTagCompound(nbtStack);
	}

	public boolean canChageMode(EntityLivingBase owner)
	{
		return owner.isSneaking();
	}

}
