package schr0.tanpopo.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.block.BlockEssenceCauldron;
import schr0.tanpopo.init.TanpopoNBTTags;
import schr0.tanpopo.init.TanpopoPacket;
import schr0.tanpopo.packet.MessageParticleBlock;

public class TileEntityEssenceCauldron extends TileEntity implements ITickable, ISidedInventory
{

	private static final ArrayList<EssenceCauldronCraft> LIST_ESSENCE_CAULDRON_CRAFT = TanpopoRegistry.getListEssenceCauldronCraft();
	private static final String TAG_KEY = TanpopoNBTTags.TILEENTITY_ESSENCE_CAULDRON;
	private static final int SIZE_INVENTORY = 1;
	private static final int SIZE_STACKSIZE = 64;
	private static final EnumFacing FACING_CAN_INSERT = EnumFacing.UP;
	private ItemStack[] inventoryContents;
	private int craftTickTime;

	public TileEntityEssenceCauldron()
	{
		this.inventoryContents = new ItemStack[SIZE_INVENTORY];
		this.craftTickTime = 0;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		this.inventoryContents = new ItemStack[this.getSizeInventory()];

		NBTTagList nbttagList = compound.getTagList("Items", 10);

		for (int tagCount = 0; tagCount < nbttagList.tagCount(); ++tagCount)
		{
			NBTTagCompound nbttagCompound = nbttagList.getCompoundTagAt(tagCount);
			int index = nbttagCompound.getByte("Slot") & 255;

			if (0 <= index && index < this.inventoryContents.length)
			{
				this.inventoryContents[index] = ItemStack.loadItemStackFromNBT(nbttagCompound);
			}
		}

		this.craftTickTime = compound.getInteger(TAG_KEY);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagList nbttagList = new NBTTagList();

		for (int index = 0; index < this.inventoryContents.length; ++index)
		{
			if (this.inventoryContents[index] != null)
			{
				NBTTagCompound nbttagCompound = new NBTTagCompound();

				nbttagCompound.setByte("Slot", (byte) index);
				this.inventoryContents[index].writeToNBT(nbttagCompound);
				nbttagList.appendTag(nbttagCompound);
			}
		}

		compound.setTag("Items", nbttagList);

		compound.setInteger(TAG_KEY, this.craftTickTime);

		return compound;
	}

	@Override
	public String getName()
	{
		return new TextComponentTranslation("tile.essence_cauldron_3.name", new Object[0]).getFormattedText();
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return SIZE_INVENTORY;
	}

	@Override
	@Nullable
	public ItemStack getStackInSlot(int index)
	{
		return this.inventoryContents[index];
	}

	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack stack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);

		if (stack != null)
		{
			this.markDirty();
		}

		return stack;
	}

	@Override
	@Nullable
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.inventoryContents, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.inventoryContents[index] = stack;

		if (stack != null && this.getInventoryStackLimit() < stack.stackSize)
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return SIZE_STACKSIZE;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		this.markDirty();
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		this.markDirty();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return this.canCraft(stack);
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		// none
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int index = 0; index < this.inventoryContents.length; ++index)
		{
			this.inventoryContents[index] = null;
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[]
		{
				0
		};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (direction == FACING_CAN_INSERT)
		{
			return this.isItemValidForSlot(index, itemStackIn);
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return false;
	}

	@Override
	public void update()
	{
		World world = this.getWorld();
		BlockPos posTile = this.getPos();

		for (EntityItem entityItem : this.getPosUpEntityItems())
		{
			ItemStack stackEntityItem = entityItem.getEntityItem();

			if (this.canCraft(stackEntityItem))
			{
				ItemStack putStack = TileEntityHopper.putStackInInventoryAllSlots(this, stackEntityItem, FACING_CAN_INSERT);

				if (putStack != null && putStack.stackSize != 0)
				{
					entityItem.setEntityItemStack(putStack);
				}
				else
				{
					if (!world.isRemote)
					{
						entityItem.setDead();
					}
				}
			}
		}

		if (this.getStackInEssenceCauldron() != null)
		{
			ItemStack stackInv = this.getStackInEssenceCauldron();

			if (this.canCraft(stackInv))
			{
				if (!world.isRemote)
				{
					++this.craftTickTime;

					if (this.getCraftTickTime(stackInv) < this.craftTickTime)
					{
						this.craftTickTime = 0;

						this.clear();

						this.onCrafting(stackInv);

						world.playSound(null, posTile, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					else
					{
						if (this.craftTickTime % 20 == 0)
						{
							world.playSound(null, posTile, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.25F, 1.0F);
						}

						this.spawnCraftingParticles();
					}
				}
			}
			else
			{
				this.craftTickTime = 0;

				this.clear();

				if (stackInv != null)
				{
					Block.spawnAsEntity(world, posTile, stackInv);
				}
			}
		}
		else
		{
			this.craftTickTime = 0;

			this.clear();
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private void spawnCraftingParticles()
	{
		TanpopoPacket.DISPATCHER.sendToAll(new MessageParticleBlock(0, this.getPos()));
	}

	@Nullable
	private List<EntityItem> getPosUpEntityItems()
	{
		World world = this.getWorld();
		BlockPos posTile = this.getPos();
		double posX = (double) posTile.getX() + 0.5D;
		double posY = (double) posTile.getY() + 0.5D;
		double posZ = (double) posTile.getZ() + 0.5D;
		List<EntityItem> listEntityItem = TileEntityHopper.getCaptureItems(world, posX, posY, posZ);

		return listEntityItem;
	}

	@Nullable
	private ItemStack getStackInEssenceCauldron()
	{
		return this.getStackInSlot(0);
	}

	@Nullable
	private EssenceCauldronCraft getCraft(ItemStack stack)
	{
		for (EssenceCauldronCraft essenceCauldronCraft : LIST_ESSENCE_CAULDRON_CRAFT)
		{
			if (essenceCauldronCraft.getKeyItem().equals(stack.getItem()))
			{
				return essenceCauldronCraft;
			}
		}

		return (EssenceCauldronCraft) null;
	}

	private boolean canCraft(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();
		int stackSize = stackCopy.stackSize;
		int level = 1 + ((Integer) this.getWorld().getBlockState(this.getPos()).getValue(BlockCauldron.LEVEL)).intValue();

		if (this.getCraft(stackCopy) != null)
		{
			if (this.getCraftStackCost(stackCopy) <= stackSize && this.getCraftEssenceCost(stackCopy) <= level)
			{
				return this.getCraft(stackCopy).getResult(stackCopy) != null;
			}
		}

		return false;
	}

	private int getCraftEssenceCost(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getCraft(stackCopy) != null)
		{
			return this.getCraft(stackCopy).getEssenceCost(stackCopy);
		}

		return 0;
	}

	private int getCraftStackCost(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getCraft(stackCopy) != null)
		{
			return this.getCraft(stackCopy).getStackCost(stackCopy);
		}

		return 0;
	}

	private int getCraftTickTime(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getCraft(stackCopy) != null)
		{
			return this.getCraft(stackCopy).getTickTime(stackCopy);
		}

		return 0;
	}

	@Nullable
	private ItemStack getCraftResult(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getCraft(stackCopy) != null)
		{
			return this.getCraft(stackCopy).getResult(stackCopy);
		}

		return null;
	}

	private void onCrafting(ItemStack stack)
	{
		World world = this.getWorld();
		BlockPos posTile = this.getPos();

		ItemStack result = this.getCraftResult(stack);
		int stackSize = stack.stackSize;
		int level = ((Integer) world.getBlockState(posTile).getValue(BlockCauldron.LEVEL)).intValue();

		result = this.putStackInFacingInventory(result);
		stackSize -= this.getCraftStackCost(stack);
		level -= this.getCraftEssenceCost(stack);

		if (result != null)
		{
			Block.spawnAsEntity(world, posTile, result);
		}

		if (stackSize <= 0)
		{
			stack = null;
		}
		else
		{
			stack.stackSize = stackSize;
		}

		if (0 <= level)
		{
			IBlockState state = world.getBlockState(posTile);

			if (state.getBlock() instanceof BlockEssenceCauldron)
			{
				((BlockEssenceCauldron) state.getBlock()).setEssenceLevel(world, posTile, state, level);
			}
		}
		else
		{
			world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
		}

		if (stack != null)
		{
			if (world.getTileEntity(posTile) instanceof TileEntityEssenceCauldron)
			{
				TileEntity tileEntity = world.getTileEntity(posTile);

				if (this.canCraft(stack))
				{
					ItemStack putStack = TileEntityHopper.putStackInInventoryAllSlots((IInventory) tileEntity, stack, FACING_CAN_INSERT);

					if (putStack != null && putStack.stackSize != 0)
					{
						Block.spawnAsEntity(world, posTile, putStack);
					}
				}
			}
			else
			{
				Block.spawnAsEntity(world, posTile, stack);
			}
		}
	}

	@Nullable
	private ItemStack putStackInFacingInventory(ItemStack stack)
	{
		World world = this.getWorld();
		BlockPos posTile = this.getPos();

		for (EnumFacing facing : EnumFacing.VALUES)
		{
			if (facing == FACING_CAN_INSERT)
			{
				continue;
			}

			BlockPos posFacing = posTile.offset(facing);
			TileEntity tileEntity = world.getTileEntity(posFacing);

			if (tileEntity instanceof IInventory)
			{
				ItemStack putStack = TileEntityHopper.putStackInInventoryAllSlots((IInventory) tileEntity, stack, facing.getOpposite());

				if (putStack != null && putStack.stackSize != 0)
				{
					return putStack;
				}
			}
		}

		return (ItemStack) null;
	}

}
