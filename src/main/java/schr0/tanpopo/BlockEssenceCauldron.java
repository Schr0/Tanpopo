package schr0.tanpopo;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEssenceCauldron extends BlockCauldron
{

	/*
		public static class ActivatedCraft
		{
			public @Nullable ItemStack getResult(ItemStack stack)
			{
				return (ItemStack) null;
			}
		}
	
		public static final BlockEssenceCauldron.ActivatedCraft ACTIVATED_CRAFT_DEFAULTED = new BlockEssenceCauldron.ActivatedCraft();
		public static final RegistryDefaulted<Item, BlockEssenceCauldron.ActivatedCraft> ACTIVATED_CRAFT_REGISTRY = new RegistryDefaulted(ACTIVATED_CRAFT_DEFAULTED);
	
		static
		{
			ACTIVATED_CRAFT_REGISTRY.putObject(Items.COAL, new BlockEssenceCauldron.ActivatedCraft()
			{
	
				@Override
				public ItemStack getResult(ItemStack stack)
				{
					if (stack.getItemDamage() != 1)
					{
						return super.getResult(stack);
					}
	
					return new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL);
				}
	
			});
		}
	
		private @Nullable ItemStack getActivatedCraftResult(ItemStack stack)
		{
			if (ACTIVATED_CRAFT_REGISTRY.getObject(stack.getItem()) == ACTIVATED_CRAFT_DEFAULTED)
			{
				return (ItemStack) null;
			}
	
			return (ItemStack) ACTIVATED_CRAFT_REGISTRY.getObject(stack.getItem()).getResult(stack);
		}
	//*/

	public BlockEssenceCauldron()
	{
		super();
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
	public void fillWithRain(World world, BlockPos pos)
	{
		// none
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		int meta = ((Integer) state.getValue(LEVEL)).intValue();
		float depth = (float) pos.getY() + (6.0F + (float) (4 * meta)) / 16.0F;

		if (!worldIn.isRemote && entityIn.isBurning() && (entityIn.getEntityBoundingBox().minY <= (double) depth))
		{
			this.onCatchFire(worldIn, pos, state, meta);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (heldItem != null)
		{
			int level = ((Integer) state.getValue(LEVEL)).intValue();

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
						this.addResultItemToInventory(playerIn, hand, heldItem, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE));

						worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);

						playerIn.addStat(StatList.CAULDRON_USED);
					}

					worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				else
				{
					if (!worldIn.isRemote)
					{
						this.addResultItemToInventory(playerIn, hand, heldItem, new ItemStack(TanpopoItems.ESSENCE_GLASS_BOTTLE));

						--level;

						if (level < 0)
						{
							worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);
						}
						else
						{
							this.setWaterLevel(worldIn, pos, state, level);
						}

						playerIn.addStat(StatList.CAULDRON_USED);
					}

					worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}

				return true;
			}
			/*
						else if (this.getActivatedCraftResult(heldItem) != null)
						{
							if (!worldIn.isRemote)
							{
								this.addResultItemToInventory(playerIn, hand, heldItem, this.getActivatedCraftResult(heldItem));
			
								--level;
			
								if (level < 0)
								{
									worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 2);
								}
								else
								{
									this.setWaterLevel(worldIn, pos, state, level);
								}
			
								playerIn.addStat(StatList.CAULDRON_USED);
							}
			
							worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			
							BlockEssence.spawnParticles(worldIn, pos);
			
							return true;
						}
			//*/
			return false;
		}

		return true;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private int getCheckPosXyz()
	{
		return 4;
	}

	private void onCatchFire(World world, BlockPos pos, IBlockState state, int meta)
	{
		if (world.isRemote)
		{
			return;
		}

		float strength = (meta == 0) ? (6.0F) : (2.0F);

		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), strength, true);

		world.setBlockToAir(pos);

		int checkPosXyz = getCheckPosXyz();

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.isAirBlock(posAround))
			{
				world.setBlockState(posAround, Blocks.FIRE.getDefaultState(), 2);
			}
		}
	}

	private void addResultItemToInventory(EntityPlayer player, EnumHand hand, ItemStack heldItem, ItemStack newHeldItem)
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
