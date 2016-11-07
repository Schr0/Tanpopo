package schr0.tanpopo.tileentity;

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
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.block.BlockEssenceCauldron;
import schr0.tanpopo.init.TanpopoNBTTags;
import schr0.tanpopo.init.TanpopoPacket;
import schr0.tanpopo.packet.MessageParticleBlock;

public class TileEntityEssenceCauldron extends TileEntity implements ITickable, IInventory
{

	private static final RegistryDefaulted<Item, EssenceCauldronCraft> REGISTRY_ESSENCE_CAULDRON_CRAFT = TanpopoRegistry.getRegistryEssenceCauldronCraft();
	private static final String TAG_KEY = TanpopoNBTTags.TILEENTITY_ESSENCE_CAULDRON;
	private static final int SIZE_INVENTORY = 1;
	private static final int SIZE_STACKSIZE = 64;
	private ItemStack[] inventoryContents;
	private int craftTime;

	public TileEntityEssenceCauldron()
	{
		this.inventoryContents = new ItemStack[SIZE_INVENTORY];
		this.craftTime = 0;
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

		this.craftTime = compound.getInteger(TAG_KEY);
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

		compound.setInteger(TAG_KEY, this.craftTime);

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
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);

		if (itemstack != null)
		{
			this.markDirty();
		}

		return itemstack;
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
		return this.isEssenceCauldronCraft(stack, this.getWorld(), this.getPos());
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
	public void update()
	{
		World world = this.getWorld();
		BlockPos posTile = this.getPos();

		if (world == null)
		{
			return;
		}

		if (this.getStackInInventory() != null)
		{
			ItemStack stackInv = this.getStackInInventory();

			if (!this.isEssenceCauldronCraft(stackInv, world, posTile))
			{
				this.craftTime = 0;

				this.clear();

				if (stackInv != null)
				{
					Block.spawnAsEntity(world, posTile, stackInv);
				}

				return;
			}

			if (!world.isRemote)
			{
				++this.craftTime;

				if (this.getEssenceCauldronCraftTime(stackInv) < this.craftTime)
				{
					this.craftTime = 0;

					this.clear();

					this.onEssenceCauldronCraft(stackInv, world, posTile);

					world.playSound(null, posTile, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				else
				{
					if (this.craftTime % 30 == 0)
					{
						world.playSound(null, posTile, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.25F, 1.0F);
					}

					this.spawnCraftingParticles(world, posTile);
				}
			}
		}
		else
		{
			this.craftTime = 0;
		}

		for (EntityItem entityItem : this.getUpEntityItems(world))
		{
			if (this.isEssenceCauldronCraft(entityItem.getEntityItem(), world, posTile))
			{
				TileEntity tileEntity = world.getTileEntity(pos);

				if (tileEntity instanceof TileEntityEssenceCauldron)
				{
					TileEntityHopper.putDropInInventoryAllSlots((TileEntityEssenceCauldron) tileEntity, entityItem);
				}
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private void spawnCraftingParticles(World world, BlockPos pos)
	{
		TanpopoPacket.DISPATCHER.sendToAll(new MessageParticleBlock(0, pos.getX(), pos.getY(), pos.getZ()));
	}

	@Nullable
	private ItemStack getStackInInventory()
	{
		return this.getStackInSlot(0);
	}

	@Nullable
	private EssenceCauldronCraft getEssenceCauldronCraft(ItemStack stack)
	{
		if (REGISTRY_ESSENCE_CAULDRON_CRAFT.getObject(stack.getItem()) == null)
		{
			return (EssenceCauldronCraft) null;
		}

		return (EssenceCauldronCraft) REGISTRY_ESSENCE_CAULDRON_CRAFT.getObject(stack.getItem());
	}

	private boolean isEssenceCauldronCraft(ItemStack stack, World world, BlockPos pos)
	{
		if (stack == null)
		{
			return false;
		}

		ItemStack stackCopy = stack.copy();
		int stackSize = stackCopy.stackSize;
		int level = 1 + ((Integer) world.getBlockState(pos).getValue(BlockCauldron.LEVEL)).intValue();

		if (this.getEssenceCauldronCraft(stackCopy) != null)
		{
			if (this.getEssenceCauldronCraftStackCost(stackCopy) <= stackSize && this.getEssenceCauldronCraftEssenceCost(stackCopy) <= level)
			{
				return this.getEssenceCauldronCraft(stackCopy).getResult(stackCopy) != null;
			}
		}

		return false;
	}

	private int getEssenceCauldronCraftEssenceCost(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getEssenceCauldronCraft(stackCopy) != null)
		{
			return this.getEssenceCauldronCraft(stackCopy).getEssenceCost(stackCopy);
		}

		return 0;
	}

	private int getEssenceCauldronCraftStackCost(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getEssenceCauldronCraft(stackCopy) != null)
		{
			return this.getEssenceCauldronCraft(stackCopy).getStackCost(stackCopy);
		}

		return 0;
	}

	private int getEssenceCauldronCraftTime(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getEssenceCauldronCraft(stackCopy) != null)
		{
			return this.getEssenceCauldronCraft(stackCopy).getTime(stackCopy);
		}

		return 0;
	}

	@Nullable
	private ItemStack getEssenceCauldronCraftResult(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getEssenceCauldronCraft(stackCopy) != null)
		{
			return this.getEssenceCauldronCraft(stackCopy).getResult(stackCopy);
		}

		return null;
	}

	private void onEssenceCauldronCraft(ItemStack stack, World world, BlockPos pos)
	{
		ItemStack result = this.getEssenceCauldronCraftResult(stack);

		if (!this.putStackInFacingInventory(result, world, pos))
		{
			Block.spawnAsEntity(world, pos, result);
		}

		int stackSize = stack.stackSize;
		int level = ((Integer) world.getBlockState(pos).getValue(BlockCauldron.LEVEL)).intValue();

		stackSize -= this.getEssenceCauldronCraftStackCost(stack);
		level -= this.getEssenceCauldronCraftEssenceCost(stack);

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
			IBlockState state = world.getBlockState(pos);

			if (state.getBlock() instanceof BlockEssenceCauldron)
			{
				((BlockEssenceCauldron) state.getBlock()).setEssenceLevel(world, pos, state, level);
			}
		}
		else
		{
			world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
		}

		if (stack != null)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileEntityEssenceCauldron)
			{
				if (this.isEssenceCauldronCraft(stack, world, pos))
				{
					TileEntityHopper.putStackInInventoryAllSlots((TileEntityEssenceCauldron) tileEntity, stack, null);
				}
			}
			else
			{
				Block.spawnAsEntity(world, pos, stack);
			}
		}

	}

	private boolean putStackInFacingInventory(ItemStack stack, World world, BlockPos pos)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			if (facing == EnumFacing.UP)
			{
				continue;
			}

			BlockPos posFacing = pos.offset(facing);
			TileEntity tileEntity = world.getTileEntity(posFacing);

			if (tileEntity instanceof IInventory)
			{
				TileEntityHopper.putStackInInventoryAllSlots((IInventory) tileEntity, stack, facing.getOpposite());

				return true;
			}
		}

		return false;
	}

	@Nullable
	private List<EntityItem> getUpEntityItems(World world)
	{
		double posX = (double) this.pos.getX() + 0.5D;
		double posY = (double) this.pos.getY() + 0.5D;
		double posZ = (double) this.pos.getZ() + 0.5D;
		List<EntityItem> listEntityItem = TileEntityHopper.getCaptureItems(world, posX, posY, posZ);

		return listEntityItem;
	}

}
