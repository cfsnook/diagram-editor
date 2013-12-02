package ac.soton.eventb.diagrameditor.features;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;
import org.eventb.emf.core.EventBNamedCommentedElement;

public class EventBComponentDeleteFeature extends DefaultDeleteFeature {

	public EventBComponentDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public void delete(IDeleteContext context) {
		EventBNamedCommentedElement e = (EventBNamedCommentedElement) getBusinessObjectForPictogramElement(context.getPictogramElement());
		EcoreUtil.delete(e);
		super.delete(context);
	}

}
