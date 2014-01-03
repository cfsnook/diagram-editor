package ac.soton.eventb.diagrameditor;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class DiagramTypeProvider extends AbstractDiagramTypeProvider {
	
	private IToolBehaviorProvider[] toolbhavProvider;
	
	public DiagramTypeProvider() {
		super();

		this.setFeatureProvider(new EventBDiagramFeatureProvider(this));

	}

	@Override
	public boolean isAutoUpdateAtReset() {
		return true;
	}

	@Override
	public boolean isAutoUpdateAtRuntime() {
		return true;
	}

	@Override
	public boolean isAutoUpdateAtStartup() {
		return true;
	}

	@Override
	public void resourcesSaved(Diagram diagram, Resource[] savedResources) {
		super.resourcesSaved(diagram, savedResources);
		((EventBDiagramFeatureProvider) this.getFeatureProvider()).save();
	}
	
	public IToolBehaviorProvider[] getToolBehProvider(){
		if(toolbhavProvider == null){
			toolbhavProvider = new IToolBehaviorProvider[]{
					new DiagramToolBehaviorProvider(this)
			};
		}
		return toolbhavProvider;
	}

}
