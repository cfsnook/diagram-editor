package ac.soton.eventb.diagrameditor.features.update;

import java.util.logging.Logger;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eventb.emf.core.EventBNamed;

public class EventBComponentUpdateFeature extends AbstractUpdateFeature {

	public EventBComponentUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return true;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		if(context.getPictogramElement() instanceof ContainerShape) {
			for(Shape s : ((ContainerShape)context.getPictogramElement()).getChildren()) {

				if(s.getGraphicsAlgorithm() instanceof Text) {
					String name = ((EventBNamed)getBusinessObjectForPictogramElement(context.getPictogramElement())).doGetName();
					return ((AbstractText) s.getGraphicsAlgorithm()).getValue().equals(name) ? Reason.createFalseReason() : Reason.createTrueReason(); 
				}
			}
		}
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		if(context.getPictogramElement() instanceof ContainerShape) {
			for(Shape s : ((ContainerShape)context.getPictogramElement()).getChildren()) {
				if(s.getGraphicsAlgorithm() instanceof Text) {
					String name = ((EventBNamed)getBusinessObjectForPictogramElement(context.getPictogramElement())).doGetName();
					((AbstractText)s.getGraphicsAlgorithm()).setValue(name);
					return true;
				}
			}
		}
		return false;
	}

}
