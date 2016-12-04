package schr0.tanpopo.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoBlocks;
import schr0.tanpopo.init.TanpopoFluids;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.tileentity.TileEntityEssenceCauldron;

public class BlockEssenceCauldron extends BlockCauldron implements ITileEntityProvider
{

	public BlockEssenceCauldron()
	{
		super();
		this.setTickRandomly(true);
		this.isBlockContainer = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for (int meta = 0; meta <= TanpopoBlocks.META_ESSENCE_CAULDRON; meta++)
		{
			list.add(new ItemStack(itemIn, 1, meta));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityEssenceCauldron();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileEntity);
		}

		super.breakBlock(worldIn, pos, state);

		worldIn.removeTileEntity(pos);
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		return tileEntity == null ? false : tileEntity.receiveClientEvent(id, param);
	}

	@Override
	public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level)
	{
		// none
	}

	@Override
	public void fillWithRain(World world, BlockPos pos)
	{
		// none
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (this.canCatchFire(worldIn, pos))
		{
			this.onCatchFire(worldIn, pos);
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);

		if (this.canCatchFire(world, pos))
		{
			this.onCatchFire(world, pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock)
	{
		super.neighborChanged(state, world, pos, neighborBlock);

		if (this.canCatchFire(world, pos))
		{
			this.onCatchFire(world, pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (heldItem != null)
		{
			int level = ((Integer) state.getValue(BlockCauldron.LEVEL)).intValue();

			if ((heldItem.getItem() == Items.BUCKET) || (heldItem.getItem() == Items.GLASS_BOTTLE))
			{
				boolean isBucket = (heldItem.getItem() == Items.BUCKET);

				if (isBucket)
				{
					if (level < TanpopoBlocks.META_ESSENCE_CAULDRON)
					{
						return false;
					}

					if (!worldIn.isRemote)
					{
						this.addNewHeldItemToInventory(playerIn, hand, heldItem, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE));

						worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);

						playerIn.addStat(StatList.CAULDRON_USED);
					}

					worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				else
				{
					if (!worldIn.isRemote)
					{
						this.addNewHeldItemToInventory(playerIn, hand, heldItem, new ItemStack(TanpopoItems.ESSENCE_GLASS_BOTTLE));

						--level;

						if (level < 0)
						{
							worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);
						}
						else
						{
							this.setEssenceLevel(worldIn, pos, state, level);
						}

						playerIn.addStat(StatList.CAULDRON_USED);
					}

					worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);

		if (this.canCatchFire(world, pos))
		{
			this.onCatchFire(world, pos);
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	public void setEssenceLevel(World worldIn, BlockPos pos, IBlockState state, int level)
	{
		worldIn.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
		worldIn.updateComparatorOutputLevel(pos, this);
	}

	private int getCheckPosXyz()
	{
		return 4;
	}

	private boolean canCatchFire(World world, BlockPos pos)
	{
		int checkPosXyz = this.getCheckPosXyz();

		for (Entity entityAround : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).expandXyz((double) checkPosXyz)))
		{
			if (entityAround.isBurning())
			{
				return true;
			}
		}

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.getBlockState(posAround).getMaterial() == Material.FIRE)
			{
				return true;
			}
		}

		return false;
	}

	private void onCatchFire(World world, BlockPos pos)
	{
		if (world.isRemote)
		{
			return;
		}

		float strength = 2.0F;

		if (world.getBlockState(pos).getBlock() == this)
		{
			strength = ((((Integer) world.getBlockState(pos).getValue(BlockCauldron.LEVEL)).intValue() + 1) * 2);
		}

		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), strength, true);

		world.setBlockToAir(pos);

		int checkPosXyz = this.getCheckPosXyz();

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.isAirBlock(posAround))
			{
				world.setBlockState(posAround, Blocks.FIRE.getDefaultState(), 2);
			}
		}
	}

	private void addNewHeldItemToInventory(EntityPlayer player, EnumHand hand, ItemStack heldItem, ItemStack newHeldItem)
	{
		if (!player.capabilities.isCreativeMode)
		{
			--heldItem.stackSize;

			if (heldItem.stackSize <= 0)
			{
				player.setHeldItem(hand, newHeldItem);
			}
			else if (!player.inventory.addItemStackToInventory(newHeldItem))
			{
				player.dropItem(newHeldItem, false);
			}
		}
	}

}
