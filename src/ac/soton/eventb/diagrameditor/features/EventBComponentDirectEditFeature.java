package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.EventBNamedCommentedElement;

public class EventBComponentDirectEditFeature extends
		AbstractDirectEditingFeature {

	public EventBComponentDirectEditFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {

		final PictogramElement pe = context.getPictogramElement();
		final Object bo = this.getBusinessObjectForPictogramElement(pe);
		final GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
		// support direct editing, if it is a EClass, and the user clicked
		// directly on the text and not somewhere else in the rectangle
		if (bo instanceof EventBNamedCommentedElement && ga instanceof Text) {
			return true;
		}
		// direct editing not supported in all other cases
		return false;
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		// TODO Auto-generated method stub
		return ((EventBNamed) this.getBusinessObjectForPictogramElement(context
				.getPictogramElement())).doGetName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		final PictogramElement pe = context.getPictogramElement();
		final EventBNamed element = (EventBNamed) this
				.getBusinessObjectForPictogramElement(pe);
		element.doSetName(value);
		// ((EventBDiagramFeatureProvider)getFeatureProvider()).save();

		this.updatePictogramElement(((Shape) pe).getContainer());
	}

}
