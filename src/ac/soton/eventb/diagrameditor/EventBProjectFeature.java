package ac.soton.eventb.diagrameditor;

import java.util.logging.Logger;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;

import ac.soton.eventb.diagrameditor.features.update.EventBProjectUpdateFeature;

public class EventBProjectFeature implements IEventBFeature {

	@Override
	public boolean canAdd() {
		return false;
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canDirectEdit() {
		return false;
	}

	@Override
	public Matcher<IAddContext, IAddFeature> getAddMatcher() {
		return null;
	}

	@Override
	public Matcher<IDeleteContext, IDeleteFeature> getDeleteMatcher() {
		return null;
	}

	@Override
	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher() {
		return null;
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
				return o.getPictogramElement() instanceof Diagram;
			}
			
			@Override
			public IUpdateFeature getFeature(IUpdateContext o,
					EventBDiagramFeatureProvider e) {
				if(this.match(o, e)) {
					return new EventBProjectUpdateFeature(e);
				}
				return null;
			}
		};
	}

}
