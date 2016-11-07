package schr0.tanpopo.init;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.Tanpopo;
import schr0.tanpopo.packet.MessageHandlerParticleBlock;
import schr0.tanpopo.packet.MessageParticleBlock;

public class TanpopoPacket
{

	public static final String CHANNEL_NAME = Tanpopo.MOD_ID;

	public static final SimpleNetworkWrapper DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);

	public static final int ID_PARTICLE_BLOCK = 0;

	public void init()
	{
		// none
	}

	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		registerClient();
	}

	private static void registerClient()
	{
		DISPATCHER.registerMessage(MessageHandlerParticleBlock.class, MessageParticleBlock.class, ID_PARTICLE_BLOCK, Side.CLIENT);
	}

}
