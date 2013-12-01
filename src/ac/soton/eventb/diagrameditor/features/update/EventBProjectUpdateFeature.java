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
	public IReason updateNeeded(IUpdateContext context) {
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		boolean updated = false;
		Project project = (Project) getBusinessObjectForPictogramElement(context.getPictogramElement());
		
		if(project == null) {
			ProjectResource pr = ((EventBDiagramFeatureProvider)this.getFeatureProvider()).getProjectResource();
			project = (Project) pr.getContents().get(0);
			link(context.getPictogramElement(), project);
			updated = true;
		}

		for(EventBNamedCommentedElement e : project.getComponents()) {
			AddContext ac = new AddContext();
			ac.setTargetContainer(getDiagram());
			ac.setNewObject(e);
			getFeatureProvider().addIfPossible(ac);
		}
		
		getDiagram().getConnections().clear();
		
		for(Shape s : getDiagram().getChildren()) {
			if(s instanceof ContainerShape) {
				ContainerShape c = (ContainerShape)s;
				EventBNamedCommentedElement element = (EventBNamedCommentedElement) getBusinessObjectForPictogramElement(c);
				if(element instanceof Machine) {
					Machine m = (Machine)element;
					for(Context ctx : m.getSees()) {
						for(Shape innerShape : getDiagram().getChildren()) {
							if(getBusinessObjectForPictogramElement(innerShape) == ctx) {								
								AnchorContainer a1 = (AnchorContainer)s;
								AnchorContainer a2 = (AnchorContainer)innerShape;
								Graphiti.getPeService().createChopboxAnchor(a1);
								Graphiti.getPeService().createChopboxAnchor(a2);
																
								AddConnectionContext acc = new AddConnectionContext(a1.getAnchors().get(0), a2.getAnchors().get(0));
								acc.setNewObject(new MachineSeesRelation(m, ctx));
								getFeatureProvider().addIfPossible(acc);
							}
						}
					}
					for(Machine mac : m.getRefines()) {
						for(Shape innerShape : getDiagram().getChildren()) {
							if(getBusinessObjectForPictogramElement(innerShape) == mac) {								
								AnchorContainer a1 = (AnchorContainer)s;
								AnchorContainer a2 = (AnchorContainer)innerShape;
								Graphiti.getPeService().createChopboxAnchor(a1);
								Graphiti.getPeService().createChopboxAnchor(a2);
																
								AddConnectionContext acc = new AddConnectionContext(a1.getAnchors().get(0), a2.getAnchors().get(0));
								acc.setNewObject(new MachineRefinesRelation(m, mac));
								getFeatureProvider().addIfPossible(acc);
							}
						}
					}
				}
				else if(element instanceof Context) {
					Context ctx1 = (Context)element;
					for(Context ctx2 : ctx1.getExtends()) {
						for(Shape innerShape : getDiagram().getChildren()) {
							if(getBusinessObjectForPictogramElement(innerShape) == ctx2) {
								
								AnchorContainer a1 = (AnchorContainer)s;
								AnchorContainer a2 = (AnchorContainer)innerShape;
								Graphiti.getPeService().createChopboxAnchor(a1);
								Graphiti.getPeService().createChopboxAnchor(a2);
																
								AddConnectionContext acc = new AddConnectionContext(a1.getAnchors().get(0), a2.getAnchors().get(0));
								acc.setNewObject(new ContextExtendsRelation(ctx1, ctx2));
								getFeatureProvider().addIfPossible(acc);
							}
						}
					}
				}
			}
		}

		return updated;
	}

}
