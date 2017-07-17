package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

public class ImageProvider extends AbstractImageProvider{
	
	public static final String PREFIX = ImageProvider.class.getPackage().getName() + ".";
	public static final String IMG_MACHINE = PREFIX + "machine";
	public static final String IMG_CONTEXT = PREFIX + "context";
	public static final String IMG_SEES = PREFIX + "sees";
	public static final String IMG_REFINES = PREFIX + "refines";
	public static final String IMG_EXTENTS = PREFIX + "extends";
	
	private static final String Root_Folder = "icons/";
	@Override
	protected void addAvailableImages() {
		addImageFilePath(IMG_MACHINE, Root_Folder + "machine.PNG");
		addImageFilePath(IMG_CONTEXT, Root_Folder + "context.PNG");
		addImageFilePath(IMG_SEES, Root_Folder + "sees.PNG");
		addImageFilePath(IMG_REFINES, Root_Folder + "refines.PNG");
		addImageFilePath(IMG_EXTENTS, Root_Folder + "extends.PNG");
		
	}
	
}