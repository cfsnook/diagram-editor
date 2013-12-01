package ac.soton.eventb.diagrameditor.features;

import java.util.logging.Logger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.EventBNamedCommentedElement;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;

public class EventBComponentDirectEditFeature extends
		AbstractDirectEditingFeature {

	public EventBComponentDirectEditFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		
        PictogramElement pe = context.getPictogramElement();
        Object bo = getBusinessObjectForPictogramElement(pe);
        GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
        // support direct editing, if it is a EClass, and the user clicked
        // directly on the text and not somewhere else in the rectangle
        if (bo instanceof EventBNamedCommentedElement && ga instanceof Text) {
            return true;
        }
        // direct editing not supported in all other cases
        return false;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		// TODO Auto-generated method stub
		return ((EventBNamed)getBusinessObjectForPictogramElement(context.getPictogramElement())).doGetName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
        EventBNamed element = (EventBNamed) getBusinessObjectForPictogramElement(pe);
        element.doSetName(value);
        //((EventBDiagramFeatureProvider)getFeatureProvider()).save();
 
        updatePictogramElement(((Shape) pe).getContainer());
	}
	

}
