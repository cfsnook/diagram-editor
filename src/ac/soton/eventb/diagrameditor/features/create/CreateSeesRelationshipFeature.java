package ac.soton.eventb.diagrameditor.features.create;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.diagrameditor.relations.MachineSeesRelation;

public class CreateSeesRelationshipFeature extends
		AbstractCreateConnectionFeature {

	public CreateSeesRelationshipFeature(IFeatureProvider fp) {
		super(fp, "Sees", "Create an Event-B Sees Relationship");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());
		final EObject target = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getTargetPictogramElement());

		return source instanceof Machine && target instanceof Context;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());

		return source instanceof Machine;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());
		final EObject target = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getTargetPictogramElement());

		if (source instanceof Machine && target instanceof Context) {
			final MachineSeesRelation msr = new MachineSeesRelation(
					(Machine) source, (Context) target);
			final AddConnectionContext acc = new AddConnectionContext(
					context.getSourceAnchor(), context.getTargetAnchor());
			acc.setNewObject(msr);
			return (Connection) this.getFeatureProvider().addIfPossible(acc);
		}
		return null;
	}

}
