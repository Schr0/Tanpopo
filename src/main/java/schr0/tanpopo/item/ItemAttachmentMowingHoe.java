package schr0.tanpopo.item;

import net.minecraft.item.Item;
import schr0.tanpopo.init.TanpopoItems;

public class ItemAttachmentMowingHoe extends ItemModeToolAttachment
{

	public ItemAttachmentMowingHoe()
	{
		super();
	}

	@Override
	public Item getDefaultModeTool()
	{
		return TanpopoItems.TOOL_MOWING_HOE;
	}

}
