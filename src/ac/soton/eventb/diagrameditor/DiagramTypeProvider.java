package ac.soton.eventb.diagrameditor;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;

public class DiagramTypeProvider extends AbstractDiagramTypeProvider {
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

}
