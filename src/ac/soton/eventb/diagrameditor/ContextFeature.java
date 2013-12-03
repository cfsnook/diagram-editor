package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eventb.emf.core.context.Context;

import ac.soton.eventb.diagrameditor.features.add.EventBComponentAddFeature;

public class ContextFeature implements IFeature {
	@Override
	public Matcher<IAddContext, IAddFeature> getAddMatcher() {
		return new Matcher<IAddContext, IAddFeature>() {

			@Override
			public boolean match(IAddContext o) {
				return o.getNewObject() instanceof Context;
			}

			@Override
			public IAddFeature getFeature(IAddContext o, EventBDiagramFeatureProvider e) {
				if(this.match(o)) {
					return new EventBComponentAddFeature(e);
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
