package schr0.tanpopo.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.block.BlockEssenceCauldron;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.init.TanpopoNBTTags;

public class TileEntityEssenceCauldron extends TileEntity implements ITickable, IInventory
{

	public static class ImmerseCraft
	{

		public boolean isCrafting(ItemStack stack)
		{
			return false;
		}

		public int getTime(ItemStack stack)
		{
			return 5 * 20;
		}

		public int getCost(ItemStack stack)
		{
			return 1;
		}

		@Nullable
		public ItemStack getResult(ItemStack stack)
		{
			return (ItemStack) null;
		}

	}

	public static final RegistryDefaulted<Item, ImmerseCraft> IMMERSE_CRAFT_REGISTRY = new RegistryDefaulted(null);
	private static final int SIZE_INVENTORY = 1;
	private static final int SIZE_STACKSIZE = 1;
	private static final String TAG_KEY = TanpopoNBTTags.TILEENTITY_ESSENCE_CAULDRON;
	private ItemStack[] inventoryContents;
	private int craftTime;

	static
	{

		IMMERSE_CRAFT_REGISTRY.putObject(Items.COAL, new ImmerseCraft()
		{

			@Override
			public boolean isCrafting(ItemStack stack)
			{
				if (stack.getItemDamage() == 1)
				{
					return true;
				}

				return false;
			}

			@Override
			public ItemStack getResult(ItemStack stack)
			{
				return new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL);
			}

		});
		/*
				IMMERSE_CRAFT_REGISTRY.putObject(TanpopoItems.ATTACHMENT_FELLING_AXE, new ImmerseCraft()
				{
		
					@Override
					public boolean isCrafting(ItemStack stack)
					{
						if (stack.getItem() instanceof ItemModeToolAttachment)
						{
							return ((ItemModeToolAttachment) stack.getItem()).isBroken(stack);
						}
		
						return false;
					}
		
					@Override
					public int getTime(ItemStack stack)
					{
						return 30 * 20;
					}
		
					public int getCost(ItemStack stack)
					{
						return 4;
					}
		
					@Override
					public ItemStack getResult(ItemStack stack)
					{
						stack.setItemDamage(0);
		
						return stack;
					}
		
				});
		//*/
	}

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
		return true;
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

		for (EntityItem entityItem : this.getCaptureItems(world))
		{
			if (this.isImmerseCraft(entityItem.getEntityItem()))
			{
				if (TileEntityHopper.putDropInInventoryAllSlots(this, entityItem))
				{
					this.craftTime = 0;
				}
			}
		}

		if (this.getInventoryStack() != null)
		{
			ItemStack stackInventory = this.getInventoryStack();

			if (this.isImmerseCraft(stackInventory))
			{
				++this.craftTime;

				if (this.getImmerseCraftTime(stackInventory) <= this.craftTime)
				{
					Block.spawnAsEntity(world, posTile.up(), this.getImmerseCraftResult(stackInventory));

					if (!world.isRemote)
					{
						this.clear();

						IBlockState state = world.getBlockState(posTile);
						int level = ((Integer) state.getValue(BlockCauldron.LEVEL)).intValue();

						level -= this.getImmerseCraftCost(stackInventory);

						if (level < 0)
						{
							world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
						}
						else
						{
							((BlockEssenceCauldron) state.getBlock()).setEssenceLevel(world, posTile, state, level);
						}
					}

					world.playSound(null, posTile, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}

				this.spawnParticles(world);
			}
		}
		else
		{
			this.craftTime = 0;
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	@Nullable
	private List<EntityItem> getCaptureItems(World world)
	{
		double posX = (double) this.pos.getX() + 0.5D;
		double posY = (double) this.pos.getY() + 0.5D;
		double posZ = (double) this.pos.getZ() + 0.5D;
		List<EntityItem> listEntityItem = TileEntityHopper.getCaptureItems(world, posX, posY, posZ);

		return listEntityItem;
	}

	@Nullable
	private ItemStack getInventoryStack()
	{
		return this.getStackInSlot(0);
	}

	@Nullable
	private ImmerseCraft getImmerseCraft(ItemStack stack)
	{
		if (IMMERSE_CRAFT_REGISTRY.getObject(stack.getItem()) == null)
		{
			return (ImmerseCraft) null;
		}

		return (ImmerseCraft) IMMERSE_CRAFT_REGISTRY.getObject(stack.getItem());
	}

	private boolean isImmerseCraft(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getImmerseCraft(stackCopy) != null)
		{
			return this.getImmerseCraft(stackCopy).isCrafting(stackCopy);
		}

		return false;
	}

	private int getImmerseCraftTime(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getImmerseCraft(stackCopy) != null)
		{
			return this.getImmerseCraft(stackCopy).getTime(stackCopy);
		}

		return 0;
	}

	private int getImmerseCraftCost(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getImmerseCraft(stackCopy) != null)
		{
			return this.getImmerseCraft(stackCopy).getCost(stackCopy);
		}

		return 0;
	}

	@Nullable
	private ItemStack getImmerseCraftResult(ItemStack stack)
	{
		ItemStack stackCopy = stack.copy();

		if (this.getImmerseCraft(stackCopy) != null)
		{
			return this.getImmerseCraft(stackCopy).getResult(stackCopy);
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles(World world)
	{
		Random random = world.rand;
		double posX = (double) this.pos.getX() + (0.5D + ((double) random.nextFloat() - 0.5D) * 0.85D);
		double posY = (double) this.pos.getY() + random.nextFloat();
		double posZ = (double) this.pos.getZ() + (0.5D + ((double) random.nextFloat() - 0.5D) * 0.85D);
		world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX, posY, posZ, -255.0D, -217.0D, 0.0D, new int[0]);
	}

}
