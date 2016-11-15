package schr0.tanpopo.packet;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import schr0.tanpopo.Tanpopo;

public class MessageHandlerParticleBlock implements IMessageHandler<MessageParticleBlock, IMessage>
{

	@Override
	public IMessage onMessage(MessageParticleBlock message, MessageContext ctx)
	{
		Minecraft mc = Tanpopo.proxy.getMinecraft();
		World world = mc.theWorld;
		BlockPos pos = message.getParticlePos();
		Random random = world.rand;

		switch (message.getParticleType())
		{
			case 0 :

				essenceCauldronInsertParticles(world, pos, random);

				break;

			case 1 :

				essenceCauldronCraftingParticles(world, pos, random);

				break;
		}

		return null;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void essenceCauldronInsertParticles(World world, BlockPos pos, Random random)
	{
		double posX = (double) pos.getX() + (0.5D + ((double) random.nextFloat() - 0.5D) * 0.85D);
		double posY = (double) pos.getY() + 1.0D;
		double posZ = (double) pos.getZ() + (0.5D + ((double) random.nextFloat() - 0.5D) * 0.85D);
		double musicalScaleA = ((double) 7 / 24.0D);
		world.spawnParticle(EnumParticleTypes.NOTE, posX, posY, posZ, musicalScaleA, 0.0D, 0.0D, new int[0]);
	}

	private static void essenceCauldronCraftingParticles(World world, BlockPos pos, Random random)
	{
		double posX = (double) pos.getX() + (0.5D + ((double) random.nextFloat() - 0.5D) * 0.85D);
		double posY = (double) pos.getY() + random.nextFloat();
		double posZ = (double) pos.getZ() + (0.5D + ((double) random.nextFloat() - 0.5D) * 0.85D);
		world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX, posY, posZ, -255.0D, -217.0D, 0.0D, new int[0]);
	}

}
