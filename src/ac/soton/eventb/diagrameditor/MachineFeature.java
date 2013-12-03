package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.diagrameditor.features.add.EventBMachineAddFeature;

public class MachineFeature implements IFeature {
	public Matcher<IAddContext,IAddFeature> getAddMatcher() {
		return new Matcher<IAddContext, IAddFeature>() {
			@Override
			public boolean match(IAddContext o) {
				return o.getNewObject() instanceof Machine;
			}
			
			@Override
			public IAddFeature getFeature(IAddContext o, EventBDiagramFeatureProvider e) {
				if(this.match(o) ) {
					return new EventBMachineAddFeature(e);
				}
				return null;
			}
		};
	}

	@Override
	public boolean canAdd() {
		return true;
	}
}
