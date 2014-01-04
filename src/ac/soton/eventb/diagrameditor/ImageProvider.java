package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

public class ImageProvider extends AbstractImageProvider{
	
	public static final String PREFIX = ImageProvider.class.getPackage().getName() + ".";
	public static final String IMG_MACHINE = PREFIX + "machine";
	public static final String IMG_CONTEXT = PREFIX + "context";
	
	private static final String Root_Folder = "icons/";
	@Override
	protected void addAvailableImages() {
		addImageFilePath(IMG_MACHINE, Root_Folder + "machine.PNG");
		addImageFilePath(IMG_CONTEXT, Root_Folder + "context.PNG");
		
	}

}
