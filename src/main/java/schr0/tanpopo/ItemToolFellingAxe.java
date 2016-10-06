package schr0.tanpopo;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolFellingAxe extends ItemTool
{

	private static final Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(new Block[]
	{ Blocks.LOG, Blocks.LOG2 });

	private static final List<Material> EFFECTIVE_MATERIALS = Lists.newArrayList(Material.WOOD, Material.PLANTS, Material.VINE);

	public ItemToolFellingAxe()
	{
		super(8.0F, -3.0F, TanpopoToolMaterials.TIER_0, EFFECTIVE_BLOCKS);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("axe");
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		for (Material material : EFFECTIVE_MATERIALS)
		{
			if (material == state.getMaterial())
			{
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		for (BlockPos posFelling : this.getFellingBlockPos(pos, state, worldIn))
		{
			if (this.canFellingBlocks(state, worldIn.getBlockState(posFelling)))
			{
				IBlockState stateAround = worldIn.getBlockState(posFelling);
				Block blockAround = stateAround.getBlock();

				if (!worldIn.isRemote)
				{
					worldIn.setBlockToAir(posFelling);
				}

				blockAround.dropBlockAsItem(worldIn, posFelling, stateAround, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
			}
		}

		return true;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private boolean canFellingBlocks(IBlockState state, IBlockState stateFelling)
	{
		if (state.getBlock() == stateFelling.getBlock())
		{
			return true;
		}

		if (stateFelling.getBlock() instanceof BlockLeaves)
		{
			return true;
		}

		return false;
	}

	private List<BlockPos> getFellingBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList(pos);

		for (int up = 0; !world.isAirBlock(pos.up(up)); ++up)
		{
			BlockPos posUp = pos.up(up);

			if (255 < posUp.getY())
			{
				break;
			}

			for (BlockPos posAround : this.getAroundBlockPos(posUp, state, world))
			{
				posList.add(posAround);
			}
		}

		return posList;
	}

	private List<BlockPos> getAroundBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList();

		for (BlockPos posNorth : this.getNorthBlockPos(pos, state, world))
		{
			posList.add(posNorth);

			for (BlockPos posNorthUp : this.getUpBlockPos(posNorth, state, world))
			{
				posList.add(posNorthUp);
			}
		}

		for (BlockPos posSouth : this.getSouthBlockPos(pos, state, world))
		{
			posList.add(posSouth);

			for (BlockPos posSouthUp : this.getUpBlockPos(posSouth, state, world))
			{
				posList.add(posSouthUp);
			}
		}

		for (BlockPos posWest : this.getWestBlockPos(pos, state, world))
		{
			posList.add(posWest);

			for (BlockPos posWestUp : this.getUpBlockPos(posWest, state, world))
			{
				posList.add(posWestUp);
			}
		}

		for (BlockPos posEast : this.getEastBlockPos(pos, state, world))
		{
			posList.add(posEast);

			for (BlockPos posEastUp : this.getUpBlockPos(posEast, state, world))
			{
				posList.add(posEastUp);
			}
		}

		return posList;
	}

	private List<BlockPos> getUpBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList();

		for (int up = 0; this.canFellingBlocks(state, world.getBlockState(pos.up(up))); ++up)
		{
			BlockPos posUp = pos.up(up);

			if (255 < posUp.getY())
			{
				break;
			}

			posList.add(posUp);
		}

		return posList;
	}

	private List<BlockPos> getNorthBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList();

		for (int north = 0; this.canFellingBlocks(state, world.getBlockState(pos.north(north))); ++north)
		{
			BlockPos posNorth = pos.north(north);

			if (16 < north)
			{
				break;
			}

			posList.add(posNorth);

			for (int northWest = 0; this.canFellingBlocks(state, world.getBlockState(posNorth.west(northWest))); ++northWest)
			{
				BlockPos posNorthWest = posNorth.west(northWest);

				if (8 < northWest)
				{
					break;
				}

				posList.add(posNorthWest);

				for (BlockPos posNorthWestAround : BlockPos.getAllInBox(posNorthWest.add(-1, 0, -1), posNorthWest.add(1, 0, 1)))
				{
					if (posNorthWestAround == posNorthWest)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posNorthWestAround)))
					{
						posList.add(posNorthWestAround);
					}
				}
			}

			for (int northEast = 0; this.canFellingBlocks(state, world.getBlockState(posNorth.east(northEast))); ++northEast)
			{
				BlockPos posNorthEast = posNorth.east(northEast);

				if (8 < northEast)
				{
					break;
				}

				posList.add(posNorthEast);

				for (BlockPos posNorthEastAround : BlockPos.getAllInBox(posNorthEast.add(-1, 0, -1), posNorthEast.add(1, 0, 1)))
				{
					if (posNorthEastAround == posNorthEast)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posNorthEastAround)))
					{
						posList.add(posNorthEastAround);
					}
				}
			}
		}

		return posList;
	}

	private List<BlockPos> getSouthBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList();

		for (int south = 0; this.canFellingBlocks(state, world.getBlockState(pos.south(south))); ++south)
		{
			BlockPos posSouth = pos.south(south);

			if (16 < south)
			{
				break;
			}

			posList.add(posSouth);

			for (int southWest = 0; this.canFellingBlocks(state, world.getBlockState(posSouth.west(southWest))); ++southWest)
			{
				BlockPos posSouthWest = posSouth.west(southWest);

				if (8 < southWest)
				{
					break;
				}

				posList.add(posSouthWest);

				for (BlockPos posSouthWestAround : BlockPos.getAllInBox(posSouthWest.add(-1, 0, -1), posSouthWest.add(1, 0, 1)))
				{
					if (posSouthWestAround == posSouthWest)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posSouthWestAround)))
					{
						posList.add(posSouthWestAround);
					}
				}
			}

			for (int southEast = 0; this.canFellingBlocks(state, world.getBlockState(posSouth.east(southEast))); ++southEast)
			{
				BlockPos posSouthEast = posSouth.east(southEast);

				if (8 < southEast)
				{
					break;
				}

				posList.add(posSouthEast);

				for (BlockPos posSouthEastAround : BlockPos.getAllInBox(posSouthEast.add(-1, 0, -1), posSouthEast.add(1, 0, 1)))
				{
					if (posSouthEastAround == posSouthEast)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posSouthEastAround)))
					{
						posList.add(posSouthEastAround);
					}
				}
			}
		}

		return posList;
	}

	private List<BlockPos> getWestBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList();

		for (int west = 0; this.canFellingBlocks(state, world.getBlockState(pos.west(west))); ++west)
		{
			BlockPos posWest = pos.west(west);

			if (16 < west)
			{
				break;
			}

			posList.add(posWest);

			for (int westNorth = 0; this.canFellingBlocks(state, world.getBlockState(pos.north(westNorth))); ++westNorth)
			{
				BlockPos posWestNorth = posWest.north(westNorth);

				if (8 < westNorth)
				{
					break;
				}

				posList.add(posWestNorth);

				for (BlockPos posWestNorthAround : BlockPos.getAllInBox(posWestNorth.add(-1, 0, -1), posWestNorth.add(1, 0, 1)))
				{
					if (posWestNorthAround == posWestNorth)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posWestNorthAround)))
					{
						posList.add(posWestNorthAround);
					}
				}
			}

			for (int westSouth = 0; this.canFellingBlocks(state, world.getBlockState(pos.south(westSouth))); ++westSouth)
			{
				BlockPos posWestSouth = posWest.south(westSouth);

				if (8 < westSouth)
				{
					break;
				}

				posList.add(posWestSouth);

				for (BlockPos posWestSouthAround : BlockPos.getAllInBox(posWestSouth.add(-1, 0, -1), posWestSouth.add(1, 0, 1)))
				{
					if (posWestSouthAround == posWestSouth)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posWestSouthAround)))
					{
						posList.add(posWestSouthAround);
					}
				}
			}
		}

		return posList;
	}

	private List<BlockPos> getEastBlockPos(BlockPos pos, IBlockState state, World world)
	{
		List<BlockPos> posList = Lists.newArrayList();

		for (int east = 0; this.canFellingBlocks(state, world.getBlockState(pos.east(east))); ++east)
		{
			BlockPos posEast = pos.east(east);

			if (16 < east)
			{
				break;
			}

			posList.add(posEast);

			for (int eastNorth = 0; this.canFellingBlocks(state, world.getBlockState(pos.north(eastNorth))); ++eastNorth)
			{
				BlockPos posEastNorth = posEast.north(eastNorth);

				if (8 < eastNorth)
				{
					break;
				}

				posList.add(posEastNorth);

				for (BlockPos posEastNorthAround : BlockPos.getAllInBox(posEastNorth.add(-1, 0, -1), posEastNorth.add(1, 0, 1)))
				{
					if (posEastNorthAround == posEastNorth)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posEastNorthAround)))
					{
						posList.add(posEastNorthAround);
					}
				}
			}

			for (int eastSouth = 0; this.canFellingBlocks(state, world.getBlockState(pos.south(eastSouth))); ++eastSouth)
			{
				BlockPos posEastSouth = posEast.south(eastSouth);

				if (8 < eastSouth)
				{
					break;
				}

				posList.add(posEastSouth);

				for (BlockPos posEastSouthAround : BlockPos.getAllInBox(posEastSouth.add(-1, 0, -1), posEastSouth.add(1, 0, 1)))
				{
					if (posEastSouthAround == posEastSouth)
					{
						continue;
					}

					if (this.canFellingBlocks(state, world.getBlockState(posEastSouthAround)))
					{
						posList.add(posEastSouthAround);
					}
				}
			}
		}

		return posList;
	}

}