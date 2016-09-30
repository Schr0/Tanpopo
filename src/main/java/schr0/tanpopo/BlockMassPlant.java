package schr0.tanpopo;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMassPlant extends Block
{

	public static final PropertyInteger PROPERTY_STAGE = PropertyInteger.create("stage", 0, TanpopoBlocks.META_MASS_PLANT);

	public static final AxisAlignedBB AABB = new AxisAlignedBB(0.05D, 0.05D, 0.05D, 0.95D, 0.95D, 0.95D);

	public BlockMassPlant()
	{
		super(Material.PLANTS);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(this.getStageProperty(), Integer.valueOf(0)));
		this.setSoundType(SoundType.PLANT);
		this.setHardness(0.2F);
		this.setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for (int meta = 0; meta <= TanpopoBlocks.META_MASS_PLANT; meta++)
		{
			list.add(new ItemStack(itemIn, 1, meta));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		for (int i = 0; i < 5; ++i)
		{
			double posX = (double) pos.getX() + (0.5D + ((double) rand.nextFloat() - 0.5D) * 1.25D);
			double posY = (double) pos.getY() + rand.nextFloat();
			double posZ = (double) pos.getZ() + (0.5D + ((double) rand.nextFloat() - 0.5D) * 1.25D);

			if (this.isMaxStage(stateIn))
			{
				worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, posX, posY, posZ, 0.0D, -128.0D, 0.0D, new int[0]);
			}
			else if (this.canFerment(worldIn, pos))
			{
				worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, posX, posY, posZ, -255.0D, -255.0D, -255.0D, new int[0]);
			}
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return TanpopoItems.MATERIAL_MASS;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		int stage = (Integer) state.getValue(this.getStageProperty()).intValue();

		return (this.getMaxStage() <= stage) ? (1) : (0);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 8;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.withStage(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return this.getStage(state);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]
		{ PROPERTY_STAGE });
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (this.isMaxStage(state) && (entityIn instanceof EntityLivingBase))
		{
			EntityLivingBase entityLivingBase = (EntityLivingBase) entityIn;

			entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.POISON, 5 * 20, 0));
			entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 5 * 20, 0));
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (this.isMaxStage(state))
		{
			BlockPos posDown = pos.down();

			if (worldIn.getBlockState(posDown).getBlock() == Blocks.CAULDRON)
			{
				worldIn.destroyBlock(pos, false);

				worldIn.playSound(null, posDown, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

				worldIn.setBlockState(posDown, TanpopoBlocks.ESSENCE_CAULDRON.getDefaultState().withProperty(BlockEssenceCauldron.LEVEL, 3), 2);
			}
		}
		else if (this.canFerment(worldIn, pos))
		{
			int chance = this.getFermentChance(worldIn, pos);

			if (rand.nextInt(chance) == 0)
			{
				int stage = this.getStage(state);

				if (stage < this.getMaxStage())
				{
					worldIn.setBlockState(pos, this.withStage(stage + 1), 2);
				}
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	public PropertyInteger getStageProperty()
	{
		return PROPERTY_STAGE;
	}

	public int getMaxStage()
	{
		return TanpopoBlocks.META_MASS_PLANT;
	}

	public int getStage(IBlockState state)
	{
		return ((Integer) state.getValue(this.getStageProperty())).intValue();
	}

	public IBlockState withStage(int stage)
	{
		return this.getDefaultState().withProperty(this.getStageProperty(), Integer.valueOf(stage));
	}

	public boolean isMaxStage(IBlockState state)
	{
		return (this.getMaxStage() <= ((Integer) state.getValue(this.getStageProperty())).intValue());
	}

	public int getCheckPosXyz()
	{
		return 4;
	}

	public boolean canFerment(World world, BlockPos pos)
	{
		int checkPosXyz = this.getCheckPosXyz();

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			IBlockState stateAround = world.getBlockState(posAround);

			if (stateAround.getMaterial() == Material.WATER)
			{
				return (world.getLight(pos) < 13);
			}
		}

		return false;
	}

	public int getFermentChance(World worldIn, BlockPos pos)
	{
		int chance = 8;
		int checkPosXyz = this.getCheckPosXyz();

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			IBlockState stateAround = worldIn.getBlockState(posAround);

			if (stateAround.getBlock() == this)
			{
				--chance;
			}
		}

		chance = Math.max(2, chance);

		return chance;
	}

}