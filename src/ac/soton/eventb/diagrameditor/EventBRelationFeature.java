package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.IAddContext;

import ac.soton.eventb.diagrameditor.features.add.EventBRelationshipAddFeature;
import ac.soton.eventb.diagrameditor.relations.EventBRelation;

public class EventBRelationFeature implements IFeature {
	@Override
	public Matcher<IAddContext, IAddFeature> getAddMatcher() {
		return new Matcher<IAddContext, IAddFeature>() {

			@Override
			public boolean match(IAddContext o) {
				return o.getNewObject() instanceof EventBRelation;
			}

			@Override
			public IAddFeature getFeature(IAddContext o, EventBDiagramFeatureProvider e) {
				if(this.match(o)) {
					return new EventBRelationshipAddFeature(e);
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
