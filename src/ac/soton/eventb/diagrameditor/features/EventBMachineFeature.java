package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;
import ac.soton.eventb.diagrameditor.Matcher;
import ac.soton.eventb.diagrameditor.features.add.EventBMachineAddFeature;

public class EventBMachineFeature extends EventBElementFeature {
	@Override
	public Matcher<IAddContext, IAddFeature> getAddMatcher() {
		return new Matcher<IAddContext, IAddFeature>() {
			@Override
			public IAddFeature getFeature(IAddContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBMachineAddFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IAddContext o, EventBDiagramFeatureProvider e) {
				return o.getNewObject() instanceof Machine;
			}
		};
	}

}
