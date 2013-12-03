package ac.soton.eventb.diagrameditor.features;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
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
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamed;
import org.eventb.emf.core.EventBNamedCommentedElement;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;
import ac.soton.eventb.diagrameditor.IEventBFeature;
import ac.soton.eventb.diagrameditor.Matcher;

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
	public boolean canDirectEdit() {
		return true;
	}

	@Override
	public boolean canUpdate() {
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
	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher() {
		return new Matcher<IDirectEditingContext, IDirectEditingFeature>() {

			@Override
			public IDirectEditingFeature getFeature(IDirectEditingContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBComponentDirectEditFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IDirectEditingContext o,
					EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o
						.getPictogramElement()) instanceof EventBNamed;
			}
		};
	}

	@Override
	public Matcher<IUpdateContext, IUpdateFeature> getUpdateMatcher() {
		return new Matcher<IUpdateContext, IUpdateFeature>() {

			@Override
			public IUpdateFeature getFeature(IUpdateContext o,
					EventBDiagramFeatureProvider e) {
				return new EventBComponentUpdateFeature(e);
			}

			@Override
			public boolean match(IUpdateContext o,
					EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o
						.getPictogramElement()) instanceof EventBNamed;
			}
		};
	}

}
class EventBComponentAddFeature extends AbstractAddShapeFeature {

	private static final IColorConstant CLASS_BACKGROUND = new ColorConstant(
			255, 255, 255);
	private static final IColorConstant CLASS_FOREGROUND = new ColorConstant(
			100, 50, 50);

	public EventBComponentAddFeature(IFeatureProvider fp) {
		super(fp);

	}

	@Override
	public PictogramElement add(IAddContext context) {
		final EventBNamedCommentedElement element = (EventBNamedCommentedElement) context
				.getNewObject();
		final Diagram targetDiagram = (Diagram) context.getTargetContainer();

		final IPeCreateService peCreateService = Graphiti.getPeCreateService();
		final ContainerShape containerShape = peCreateService
				.createContainerShape(targetDiagram, true);

		final int width = 150;
		final int height = 100;

		final IGaService gaService = Graphiti.getGaService();

		{
			final RoundedRectangle rr = gaService.createRoundedRectangle(
					containerShape, 5, 5);
			rr.setForeground(this.manageColor(CLASS_FOREGROUND));
			rr.setBackground(this.manageColor(CLASS_BACKGROUND));
			rr.setLineWidth(2);
			gaService.setLocationAndSize(rr, context.getX(), context.getY(),
					width, height);
			this.link(containerShape, element);
		}
		{
			// create shape for text
			final Shape shape = peCreateService.createShape(containerShape,
					false);

			// create and set text graphics algorithm
			final Text text = gaService.createText(shape, element.getName());
			text.setForeground(this.manageColor(CLASS_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			// vertical alignment has as default value "center"
			text.setFont(gaService.manageFont(this.getDiagram(), "Arial", 10,
					false, false));
			gaService.setLocationAndSize(text, 0, 0, width, 20);

			// create link and wire it
			this.link(shape, element);
		}

		peCreateService.createChopboxAnchor(containerShape);
		this.layoutPictogramElement(containerShape);

		return containerShape;
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (context.getNewObject() instanceof EventBNamedCommentedElement) {
			if (context.getTargetContainer() instanceof Diagram) {
				for (final PictogramElement pe : this.getDiagram()
						.getChildren()) {
					if (this.getBusinessObjectForPictogramElement(pe).equals(
							context.getNewObject())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
class EventBComponentUpdateFeature extends AbstractUpdateFeature {

	public EventBComponentUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return true;
	}

	@Override
	public boolean update(IUpdateContext context) {
		if (context.getPictogramElement() instanceof ContainerShape) {
			for (final Shape s : ((ContainerShape) context
					.getPictogramElement()).getChildren()) {
				if (s.getGraphicsAlgorithm() instanceof Text) {
					final String name = ((EventBNamed) this
							.getBusinessObjectForPictogramElement(context
									.getPictogramElement())).doGetName();
					((AbstractText) s.getGraphicsAlgorithm()).setValue(name);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		if (context.getPictogramElement() instanceof ContainerShape) {
			for (final Shape s : ((ContainerShape) context
					.getPictogramElement()).getChildren()) {

				if (s.getGraphicsAlgorithm() instanceof Text) {
					final String name = ((EventBNamed) this
							.getBusinessObjectForPictogramElement(context
									.getPictogramElement())).doGetName();
					return ((AbstractText) s.getGraphicsAlgorithm()).getValue()
							.equals(name) ? Reason.createFalseReason() : Reason
									.createTrueReason();
				}
			}
		}
		return Reason.createFalseReason();
	}

}

class EventBComponentDeleteFeature extends DefaultDeleteFeature {

	public EventBComponentDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void delete(IDeleteContext context) {
		final EventBNamedCommentedElement e = (EventBNamedCommentedElement) this
				.getBusinessObjectForPictogramElement(context
						.getPictogramElement());
		EcoreUtil.delete(e);
		super.delete(context);
	}

}

class EventBComponentDirectEditFeature extends
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
