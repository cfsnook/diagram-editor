package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;
import ac.soton.eventb.diagrameditor.IEventBFeature;
import ac.soton.eventb.diagrameditor.Matcher;
import ac.soton.eventb.diagrameditor.features.add.EventBRelationshipAddFeature;
import ac.soton.eventb.diagrameditor.relations.EventBRelation;

public class EventBRelationFeature implements IEventBFeature {
	@Override
	public boolean canAdd() {
		return true;
	}

	@Override
	public boolean canDelete() {
		return true;
	}

	@Override
	public Matcher<IAddContext, IAddFeature> getAddMatcher() {
		return new Matcher<IAddContext, IAddFeature>() {

			@Override
			public IAddFeature getFeature(IAddContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBRelationshipAddFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IAddContext o, EventBDiagramFeatureProvider e) {
				return o.getNewObject() instanceof EventBRelation;
			}
		};
	}

	@Override
	public Matcher<IDeleteContext, IDeleteFeature> getDeleteMatcher() {
		return new Matcher<IDeleteContext, IDeleteFeature>() {
			@Override
			public IDeleteFeature getFeature(IDeleteContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBRelationshipDeleteFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IDeleteContext o,
					EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o
						.getPictogramElement()) instanceof EventBRelation;
			}
		};
	}

	@Override
	public boolean canDirectEdit() {
		return false;
	}

	@Override
	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher() {
		return null;
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public Matcher<IUpdateContext, IUpdateFeature> getUpdateMatcher() {
		return null;
	}

	
}
