package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;

import ac.soton.eventb.diagrameditor.relations.EventBRelation;

public class EventBRelationshipDeleteFeature extends
		org.eclipse.graphiti.ui.features.DefaultDeleteFeature {

	public EventBRelationshipDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void deleteBusinessObject(Object bo) {
		final EventBRelation r = (EventBRelation) bo;
		r.delete();
	}

}
