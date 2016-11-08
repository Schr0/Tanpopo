package schr0.tanpopo.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoBlocks;
import schr0.tanpopo.init.TanpopoBehaviorsColors;
import schr0.tanpopo.init.TanpopoEventClient;
import schr0.tanpopo.init.TanpopoItems;
import schr0.tanpopo.init.TanpopoPacket;
import schr0.tanpopo.init.TanpopoTileEntitys;

@SideOnly(Side.CLIENT)
public class ProxyClient extends ProxyServer
{

	@Override
	public void preInitEventClient(FMLPreInitializationEvent event)
	{
		(new TanpopoItems()).initClient();
		(new TanpopoBlocks()).initClient();
	}

	@Override
	public void initEventClient(FMLInitializationEvent event)
	{
		(new TanpopoTileEntitys()).initClient();

		(new TanpopoEventClient()).initClient();
	}

	@Override
	public void postInitEventClient(FMLPostInitializationEvent event)
	{
		(new TanpopoBehaviorsColors()).initClient();

		(new TanpopoPacket()).initClient();
	}

	@Override
	public Minecraft getMinecraft()
	{
		return FMLClientHandler.instance().getClient();
	}

	@Override
	public void infoModLog(String format, Object... data)
	{
		FMLLog.info(format, data);
	}

}
