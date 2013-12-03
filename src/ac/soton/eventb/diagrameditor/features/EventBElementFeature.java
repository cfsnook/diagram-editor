package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamed;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;
import ac.soton.eventb.diagrameditor.IEventBFeature;
import ac.soton.eventb.diagrameditor.Matcher;
import ac.soton.eventb.diagrameditor.features.add.EventBComponentAddFeature;
import ac.soton.eventb.diagrameditor.features.update.EventBComponentUpdateFeature;

public class EventBElementFeature implements IEventBFeature {

	public EventBElementFeature() {
		super();
	}

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
					return new EventBComponentAddFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IAddContext o, EventBDiagramFeatureProvider e) {
				return o.getNewObject() instanceof EventBNamed;
			}
		};
	}
	
	@Override
	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher() {
		return new Matcher<IDirectEditingContext, IDirectEditingFeature>() {
			
			@Override
			public boolean match(IDirectEditingContext o, EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o.getPictogramElement()) instanceof EventBNamed;
			}
			
			@Override
			public IDirectEditingFeature getFeature(IDirectEditingContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBComponentDirectEditFeature(e);
				}
				return null;
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
					return new EventBComponentDeleteFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IDeleteContext o,
					EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o
						.getPictogramElement()) instanceof EventBElement;
			}
		};
	}

	@Override
	public boolean canDirectEdit() {
		return true;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public Matcher<IUpdateContext, IUpdateFeature> getUpdateMatcher() {
		return new Matcher<IUpdateContext, IUpdateFeature>() {
			
			@Override
			public boolean match(IUpdateContext o, EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o.getPictogramElement()) instanceof EventBNamed;
			}
			
			@Override
			public IUpdateFeature getFeature(IUpdateContext o,
					EventBDiagramFeatureProvider e) {
				return new EventBComponentUpdateFeature(e);
			}
		};
	}
	
	

}