package schr0.tanpopo.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.init.TanpopoToolMaterials;

public class ItemToolMattock extends ItemModeTool
{

	public ItemToolMattock()
	{
		super(1.0F, -2.8F, TanpopoToolMaterials.TIER_IRON);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);

		tooltip.add(TextFormatting.GOLD + "SetBlock");

		int num = 0;

		for (int slot = 0; slot < playerIn.inventory.getHotbarSize(); ++slot)
		{
			if (playerIn.inventory.getStackInSlot(slot) != null && playerIn.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock)
			{
				ItemStack stackInv = (ItemStack) playerIn.inventory.getStackInSlot(slot);
				ItemBlock itemBlockInv = (ItemBlock) stackInv.getItem();

				++num;

				tooltip.add(TextFormatting.GOLD + String.valueOf(num) + TextFormatting.WHITE + " : " + itemBlockInv.getItemStackDisplayName(stackInv) + " x " + stackInv.stackSize);
			}
		}

		if (num <= 0)
		{
			tooltip.add(TextFormatting.DARK_RED + String.valueOf(num) + TextFormatting.WHITE + " : " + "NONE");
		}
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("pickaxe");
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		if (blockIn.getBlock().getHarvestLevel(blockIn) <= this.getToolMaterial().getHarvestLevel())
		{
			return true;
		}

		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!this.canRangeAction(stack, entityLiving))
		{
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		EntityPlayer player = (EntityPlayer) entityLiving;
		int damegeCount = 0;

		for (BlockPos posRange : this.getRangeBlockPos(pos, player))
		{
			if (posRange.equals(pos))
			{
				continue;
			}

			if (worldIn.getBlockState(posRange) == state)
			{
				IBlockState stateRange = worldIn.getBlockState(posRange);
				Block blockRange = stateRange.getBlock();

				blockRange.harvestBlock(worldIn, player, posRange, stateRange, worldIn.getTileEntity(posRange), stack);

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
				{
					blockRange.dropXpOnBlockBreak(worldIn, posRange, blockRange.getExpDrop(stateRange, worldIn, posRange, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));
				}

				worldIn.destroyBlock(posRange, false);

				if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
				{
					++damegeCount;
				}
			}
		}

		if (0 < damegeCount)
		{
			for (int count = 0; count <= Math.min(damegeCount, 4); ++count)
			{
				stack.damageItem(1, player);
			}
		}
		else
		{
			if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
			{
				stack.damageItem(1, player);
			}
		}

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (this.canChageMode(playerIn) || !playerIn.canPlayerEdit(pos, facing, stack))
		{
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		for (int slot = 0; slot < playerIn.inventory.getHotbarSize(); ++slot)
		{
			if (playerIn.inventory.getStackInSlot(slot) != null && playerIn.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock)
			{
				ItemStack stackInv = (ItemStack) playerIn.inventory.getStackInSlot(slot);
				ItemBlock itemBlockInv = (ItemBlock) stackInv.getItem();

				if (itemBlockInv.onItemUse(stackInv, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS)
				{
					if (stackInv.stackSize <= 0)
					{
						playerIn.inventory.setInventorySlotContents(slot, (ItemStack) null);
					}

					return EnumActionResult.SUCCESS;
				}

				break;
			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	@Override
	@SideOnly(Side.CLIENT)
	public TextComponentTranslation getModeName()
	{
		return new TextComponentTranslation("item.tool_mattock.mode_name", new Object[0]);
	}

	@Override
	public Item getModeAttachment()
	{
		return TanpopoItems.ATTACHMENT_MATTOCK;
	}

	private boolean canRangeAction(ItemStack stack, EntityLivingBase entityLiving)
	{
		if (this.isMode(stack) && (entityLiving instanceof EntityPlayer))
		{
			return true;
		}

		return false;
	}

	private Set<BlockPos> getRangeBlockPos(BlockPos pos, EntityPlayer player)
	{
		Set<BlockPos> posSet = Sets.newHashSet();
		RayTraceResult rayTraceResult = ForgeHooks.rayTraceEyes(player, 5.0D);

		if (rayTraceResult == null)
		{
			return posSet;
		}

		int side = rayTraceResult.sideHit.getIndex();
		int range = 1;

		switch (side)
		{
			case 2 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, -range, 0), pos.add(range, range, 0)))
				{
					posSet.add(posAround);
				}

				break;

			case 3 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(range, -range, 0), pos.add(-range, range, 0)))
				{
					posSet.add(posAround);
				}

				break;

			case 4 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, -range), pos.add(0, range, range)))
				{
					posSet.add(posAround);
				}

				break;

			case 5 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, range), pos.add(0, range, -range)))
				{
					posSet.add(posAround);
				}

				break;

			default :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, 0, -range), pos.add(range, 0, range)))
				{
					posSet.add(posAround);
				}
		}

		return posSet;
	}

}
