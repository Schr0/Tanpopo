package schr0.tanpopo.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import schr0.tanpopo.init.TanpopoTileEntitys;

public class ProxyServer
{

	/**
	 * modの事前・初期設定時イベント(クライアント).
	 */
	public void preInitEventClient(FMLPreInitializationEvent event)
	{
		// none
	}

	/**
	 * modの事中・初期設定時イベント(クライアント).
	 */
	public void initEventClient(FMLInitializationEvent event)
	{
		(new TanpopoTileEntitys()).init();
	}

	/**
	 * modの事後・初期設定時イベント(クライアント).
	 */
	public void postInitEventClient(FMLPostInitializationEvent event)
	{
		// none
	}

	/**
	 * Minecraft.
	 */
	public Minecraft getMinecraft()
	{
		return null;
	}

	/**
	 * ログの出力
	 */
	public void infoModLog(String format, Object... data)
	{
		// none
	}

}
