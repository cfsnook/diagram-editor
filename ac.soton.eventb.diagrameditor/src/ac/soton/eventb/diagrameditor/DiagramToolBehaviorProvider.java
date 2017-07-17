package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.tb.ContextMenuEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextMenuEntry;

public class DiagramToolBehaviorProvider extends DefaultToolBehaviorProvider{

	public DiagramToolBehaviorProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}
	
	public IContextMenuEntry[] getContextMenu(ICustomContext context){
		ContextMenuEntry menuEntry = new ContextMenuEntry(null, context);
		menuEntry.setText("gp29");
		menuEntry.setDescription("Custom Features Submenu");
		
		if(context instanceof ICustomContext){
			ICustomContext cc = (ICustomContext) context;
			ICustomFeature[] cf = getFeatureProvider().getCustomFeatures(cc);
			for(int i = 0; i < cf.length; i++){
				ICustomFeature customFeature = cf[i];
				if(customFeature.isAvailable(cc)){
					ContextMenuEntry entry = new ContextMenuEntry(customFeature, context);
					menuEntry.add(entry);
				}
			}
		}
		
		IContextMenuEntry ret[] = new IContextMenuEntry[]{menuEntry};
		
		return ret;
		
	}

}
