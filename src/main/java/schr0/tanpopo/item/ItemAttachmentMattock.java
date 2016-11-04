package schr0.tanpopo.item;

import net.minecraft.item.Item;
import schr0.tanpopo.init.TanpopoItems;

public class ItemAttachmentMattock extends ItemModeToolAttachment
{

	public ItemAttachmentMattock()
	{
		super();
	}

	@Override
	public Item getDefaultModeTool()
	{
		return TanpopoItems.TOOL_MATTOCK;
	}

}
