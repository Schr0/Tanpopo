package schr0.tanpopo.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageParticleBlock implements IMessage
{

	private int particleType;
	private int posX;
	private int posY;
	private int posZ;

	public MessageParticleBlock()
	{
		// none
	}

	public MessageParticleBlock(int particleType, BlockPos pos)
	{
		this.particleType = particleType;
		this.posX = pos.getX();
		this.posY = pos.getY();
		this.posZ = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.particleType = buf.readInt();
		this.posX = buf.readInt();
		this.posY = buf.readInt();
		this.posZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.particleType);
		buf.writeInt(this.posX);
		buf.writeInt(this.posY);
		buf.writeInt(this.posZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	public int getParticleType()
	{
		return this.particleType;
	}

	public BlockPos getParticlePos()
	{
		return new BlockPos(this.posX, this.posY, this.posZ);
	}

}
