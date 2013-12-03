package ac.soton.eventb.diagrameditor.features.create;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.diagrameditor.relations.MachineRefinesRelation;

public class CreateRefinesRelationshipFeature extends
		AbstractCreateConnectionFeature {

	public CreateRefinesRelationshipFeature(IFeatureProvider fp) {
		super(fp, "Refines", "Create an Event-B Refines Relationship");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());
		final EObject target = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getTargetPictogramElement());

		return source instanceof Machine && target instanceof Machine;
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

		if (source instanceof Machine && target instanceof Machine) {
			final MachineRefinesRelation msr = new MachineRefinesRelation(
					(Machine) source, (Machine) target);
			final AddConnectionContext acc = new AddConnectionContext(
					context.getSourceAnchor(), context.getTargetAnchor());
			acc.setNewObject(msr);
			return (Connection) this.getFeatureProvider().addIfPossible(acc);
		}
		return null;
	}

}
