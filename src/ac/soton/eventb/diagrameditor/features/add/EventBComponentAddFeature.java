package ac.soton.eventb.diagrameditor.features.add;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eventb.emf.core.EventBNamedCommentedElement;

public class EventBComponentAddFeature extends AbstractAddShapeFeature {

	private static final IColorConstant CLASS_FOREGROUND = new ColorConstant(100, 50, 50);
	private static final IColorConstant CLASS_BACKGROUND = new ColorConstant(255, 255, 255);

	public EventBComponentAddFeature(IFeatureProvider fp) {
		super(fp);
		
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if(context.getNewObject() instanceof EventBNamedCommentedElement) {
			if(context.getTargetContainer() instanceof Diagram) {
				for(PictogramElement pe : getDiagram().getChildren()) {
					if(getBusinessObjectForPictogramElement(pe).equals(context.getNewObject())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		EventBNamedCommentedElement element = (EventBNamedCommentedElement)context.getNewObject();
		Diagram targetDiagram = (Diagram) context.getTargetContainer();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

		int width = 150;
		int height = 100;

		IGaService gaService = Graphiti.getGaService();

		{
			RoundedRectangle rr = gaService.createRoundedRectangle(containerShape, 5, 5);
			rr.setForeground(manageColor(CLASS_FOREGROUND));
			rr.setBackground(manageColor(CLASS_BACKGROUND));
			rr.setLineWidth(2);
			gaService.setLocationAndSize(rr, context.getX(), context.getY(), width, height);
			link(containerShape, element);
		}
		{
            // create shape for text
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set text graphics algorithm
            Text text = gaService.createText(shape, element.getName());
            text.setForeground(manageColor(CLASS_FOREGROUND));
            text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER ); 
            // vertical alignment has as default value "center"
            text.setFont(gaService.manageFont(getDiagram(), "Arial", 10, false, false));
            gaService.setLocationAndSize(text, 0, 0, width, 20);
 
            // create link and wire it
            link(shape, element);
        }
		
		peCreateService.createChopboxAnchor(containerShape);
		layoutPictogramElement(containerShape);
		
		return containerShape;
	}

}
