package schr0.tanpopo.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoBehaviorsColors;
import schr0.tanpopo.init.TanpopoBlocks;
import schr0.tanpopo.init.TanpopoEventClient;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.init.TanpopoPacket;
import schr0.tanpopo.init.TanpopoTileEntitys;

@SideOnly(Side.CLIENT)
public class ProxyClient extends ProxyServer
{

	@Override
	public void preInitEventProxy(FMLPreInitializationEvent event)
	{
		(new TanpopoItems()).initClient();

		(new TanpopoBlocks()).initClient();
	}

	@Override
	public void initEventProxy(FMLInitializationEvent event)
	{
		(new TanpopoTileEntitys()).initClient();

		(new TanpopoPacket()).initClient();

		(new TanpopoEventClient()).initClient();
	}

	@Override
	public void postInitEventProxy(FMLPostInitializationEvent event)
	{
		(new TanpopoBehaviorsColors()).initClient();
	}

}
