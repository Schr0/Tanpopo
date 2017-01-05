package schr0.tanpopo.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.Tanpopo;

@SideOnly(Side.CLIENT)
public class TanpopoEventClient
{

	public void initClient()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onRenderBlockOverlayEvent(RenderBlockOverlayEvent event)
	{
		EntityPlayer player = event.getPlayer();

		if (player.isInsideOfMaterial(TanpopoBlocks.MATERIAL_LIQUID_ESSENCE))
		{
			Minecraft mc = FMLClientHandler.instance().getClient();
			ResourceLocation resUnderEssence = new ResourceLocation(Tanpopo.MOD_RESOURCE_DOMAIN + "textures/misc/under_essence.png");
			float renderPartialTicks = event.getRenderPartialTicks();

			mc.getTextureManager().bindTexture(resUnderEssence);
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			float f = mc.thePlayer.getBrightness(renderPartialTicks);
			GlStateManager.color(f, f, f, 0.5F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			float f1 = 4.0F;
			float f2 = -1.0F;
			float f3 = 1.0F;
			float f4 = -1.0F;
			float f5 = 1.0F;
			float f6 = -0.5F;
			float f7 = -mc.thePlayer.rotationYaw / 64.0F;
			float f8 = mc.thePlayer.rotationPitch / 64.0F;
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.pos(-1.0D, -1.0D, -0.5D).tex((double) (4.0F + f7), (double) (4.0F + f8)).endVertex();
			vertexbuffer.pos(1.0D, -1.0D, -0.5D).tex((double) (0.0F + f7), (double) (4.0F + f8)).endVertex();
			vertexbuffer.pos(1.0D, 1.0D, -0.5D).tex((double) (0.0F + f7), (double) (0.0F + f8)).endVertex();
			vertexbuffer.pos(-1.0D, 1.0D, -0.5D).tex((double) (4.0F + f7), (double) (0.0F + f8)).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();

			event.setCanceled(true);
		}
	}

}
