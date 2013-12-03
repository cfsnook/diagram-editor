package ac.soton.eventb.diagrameditor.features.update;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eventb.emf.core.EventBNamedCommentedElement;
import org.eventb.emf.core.Project;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.persistence.ProjectResource;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;
import ac.soton.eventb.diagrameditor.relations.ContextExtendsRelation;
import ac.soton.eventb.diagrameditor.relations.MachineRefinesRelation;
import ac.soton.eventb.diagrameditor.relations.MachineSeesRelation;

public class EventBProjectUpdateFeature extends AbstractUpdateFeature {

	public EventBProjectUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {

		return context.getPictogramElement() instanceof Diagram;
	}

	@Override
	public boolean update(IUpdateContext context) {
		boolean updated = false;
		Project project = (Project) this
				.getBusinessObjectForPictogramElement(context
						.getPictogramElement());

		if (project == null) {
			final ProjectResource pr = ((EventBDiagramFeatureProvider) this
					.getFeatureProvider()).getProjectResource();
			project = (Project) pr.getContents().get(0);
			this.link(context.getPictogramElement(), project);
			updated = true;
		}

		for (final EventBNamedCommentedElement e : project.getComponents()) {
			final AddContext ac = new AddContext();
			ac.setTargetContainer(this.getDiagram());
			ac.setNewObject(e);
			this.getFeatureProvider().addIfPossible(ac);
		}

		this.getDiagram().getConnections().clear();

		for (final Shape s : this.getDiagram().getChildren()) {
			if (s instanceof ContainerShape) {
				final ContainerShape c = (ContainerShape) s;
				final EventBNamedCommentedElement element = (EventBNamedCommentedElement) this
						.getBusinessObjectForPictogramElement(c);
				if (element instanceof Machine) {
					final Machine m = (Machine) element;
					for (final Context ctx : m.getSees()) {
						for (final Shape innerShape : this.getDiagram()
								.getChildren()) {
							if (this.getBusinessObjectForPictogramElement(innerShape) == ctx) {
								final AnchorContainer a1 = s;
								final AnchorContainer a2 = innerShape;
								Graphiti.getPeService().createChopboxAnchor(a1);
								Graphiti.getPeService().createChopboxAnchor(a2);

								final AddConnectionContext acc = new AddConnectionContext(
										a1.getAnchors().get(0), a2.getAnchors()
												.get(0));
								acc.setNewObject(new MachineSeesRelation(m, ctx));
								this.getFeatureProvider().addIfPossible(acc);
							}
						}
					}
					for (final Machine mac : m.getRefines()) {
						for (final Shape innerShape : this.getDiagram()
								.getChildren()) {
							if (this.getBusinessObjectForPictogramElement(innerShape) == mac) {
								final AnchorContainer a1 = s;
								final AnchorContainer a2 = innerShape;
								Graphiti.getPeService().createChopboxAnchor(a1);
								Graphiti.getPeService().createChopboxAnchor(a2);

								final AddConnectionContext acc = new AddConnectionContext(
										a1.getAnchors().get(0), a2.getAnchors()
												.get(0));
								acc.setNewObject(new MachineRefinesRelation(m,
										mac));
								this.getFeatureProvider().addIfPossible(acc);
							}
						}
					}
				} else if (element instanceof Context) {
					final Context ctx1 = (Context) element;
					for (final Context ctx2 : ctx1.getExtends()) {
						for (final Shape innerShape : this.getDiagram()
								.getChildren()) {
							if (this.getBusinessObjectForPictogramElement(innerShape) == ctx2) {

								final AnchorContainer a1 = s;
								final AnchorContainer a2 = innerShape;
								Graphiti.getPeService().createChopboxAnchor(a1);
								Graphiti.getPeService().createChopboxAnchor(a2);

								final AddConnectionContext acc = new AddConnectionContext(
										a1.getAnchors().get(0), a2.getAnchors()
												.get(0));
								acc.setNewObject(new ContextExtendsRelation(
										ctx1, ctx2));
								this.getFeatureProvider().addIfPossible(acc);
							}
						}
					}
				}
			}
		}

		return updated;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		return Reason.createFalseReason();
	}

}
