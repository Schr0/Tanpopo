package schr0.tanpopo.item;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoConfiguration;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.init.TanpopoToolMaterials;

public class ItemToolMowingHoe extends ItemModeTool
{

	private static final Set<Material> EFFECTIVE_MATERIALS = Sets.newHashSet(new Material[]
	{
			Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY,
			Material.PLANTS, Material.VINE, Material.CORAL, Material.GOURD
	});

	private static final Set<Material> MOWING_MATERIALS = Sets.newHashSet(new Material[]
	{
			Material.PLANTS, Material.VINE, Material.CORAL, Material.GOURD
	});

	private static final int MOWING_MODE_BLOCK_LIMIT = TanpopoConfiguration.mowingModeBlockLimit;

	public ItemToolMowingHoe()
	{
		super(1.5F, -3.0F, TanpopoToolMaterials.TIER_0);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("shovel");
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		if (blockIn.getBlock() instanceof IPlantable)
		{
			return true;
		}

		for (Material material : EFFECTIVE_MATERIALS)
		{
			if (material == blockIn.getMaterial())
			{
				return (blockIn.getBlock().getHarvestLevel(blockIn) <= this.getToolMaterial().getHarvestLevel());
			}
		}

		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!this.canMowingAction(stack, state, entityLiving))
		{
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		EntityPlayer player = (EntityPlayer) entityLiving;
		Set<BlockPos> posSet = new LinkedHashSet<>();

		this.getAroundBlockPos(posSet, worldIn, pos);

		for (BlockPos posMowing : this.getMowingBlockPos(posSet, worldIn, pos))
		{
			IBlockState stateMowing = worldIn.getBlockState(posMowing);
			Block blockMowing = stateMowing.getBlock();

			blockMowing.harvestBlock(worldIn, player, posMowing, stateMowing, worldIn.getTileEntity(posMowing), stack);

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
			{
				blockMowing.dropXpOnBlockBreak(worldIn, posMowing, blockMowing.getExpDrop(stateMowing, worldIn, posMowing, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));
			}

			worldIn.destroyBlock(posMowing, false);
		}

		stack.damageItem(1, player);

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (this.canChageMode(playerIn) || !playerIn.canPlayerEdit(pos, facing, stack) || (facing == EnumFacing.DOWN))
		{
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		if (this.getTillBlock(worldIn, pos) != null)
		{
			worldIn.setBlockState(pos, this.getTillBlock(worldIn, pos));

			for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
			{
				if (pos.equals(posAround))
				{
					continue;
				}

				if (this.getTillBlock(worldIn, posAround) != null)
				{
					worldIn.setBlockState(posAround, this.getTillBlock(worldIn, posAround));
				}
			}

			stack.damageItem(1, playerIn);

			worldIn.playSound(playerIn, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	@Override
	@SideOnly(Side.CLIENT)
	public TextComponentTranslation getModeName()
	{
		return new TextComponentTranslation("item.tool_mowing_hoe.mode_name", new Object[0]);
	}

	@Override
	public Item getModeAttachment()
	{
		return TanpopoItems.ATTACHMENT_MOWING_HOE;
	}

	private boolean canMowingAction(ItemStack stack, IBlockState state, EntityLivingBase entityLiving)
	{
		if (this.isMode(stack) && (entityLiving instanceof EntityPlayer))
		{
			return this.isMowingBlocks(state);
		}

		return false;
	}

	private boolean isMowingBlocks(IBlockState state)
	{
		if (state.getBlock() instanceof IPlantable)
		{
			return true;
		}

		for (Material material : MOWING_MATERIALS)
		{
			if (material == state.getMaterial())
			{
				return (state.getBlock().getHarvestLevel(state) <= this.getToolMaterial().getHarvestLevel());
			}
		}

		return false;
	}

	private Set<BlockPos> getAroundBlockPos(Set<BlockPos> posSet, World world, BlockPos pos)
	{
		if (MOWING_MODE_BLOCK_LIMIT < posSet.size())
		{
			return posSet;
		}

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
		{
			if (this.isMowingBlocks(world.getBlockState(posAround)))
			{
				if (posSet.add(posAround))
				{
					this.getAroundBlockPos(posSet, world, posAround);
				}
			}
		}

		return posSet;
	}

	private Set<BlockPos> getMowingBlockPos(Set<BlockPos> posSet, World world, BlockPos pos)
	{
		Set<BlockPos> posSetMowing = Sets.newHashSet();

		for (BlockPos posAround : posSet)
		{
			for (BlockPos posLimit : BlockPos.getAllInBox(pos.add(-4, 0, -4), pos.add(4, 0, 4)))
			{
				if ((posLimit.getX() == posAround.getX()) && (posLimit.getZ() == posAround.getZ()))
				{
					posSetMowing.add(posAround);
				}
			}
		}

		return posSetMowing;
	}

	@Nullable
	private IBlockState getTillBlock(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		IBlockState stateFarmland = this.getMoistureFarmland(world, pos);

		if (!world.isAirBlock(pos.up()))
		{
			return (IBlockState) null;
		}

		if (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.GRASS_PATH)
		{
			return stateFarmland;
		}

		if (state.getBlock() == Blocks.DIRT)
		{
			switch ((BlockDirt.DirtType) state.getValue(BlockDirt.VARIANT))
			{
				case DIRT :

					return stateFarmland;

				case COARSE_DIRT :

					return Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT);

				default :
					break;
			}
		}

		if (state.getBlock() == Blocks.FARMLAND)
		{
			if ((state.getBlock().getMetaFromState(state) < 7) && (stateFarmland.getBlock().getMetaFromState(stateFarmland) == 7))
			{
				return stateFarmland;
			}
		}

		return (IBlockState) null;
	}

	private IBlockState getMoistureFarmland(World world, BlockPos pos)
	{
		IBlockState state = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 6);

		for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
		{
			if (world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER)
			{
				return state.withProperty(BlockFarmland.MOISTURE, 7);
			}
		}

		return state;
	}

}
