package ac.soton.eventb.diagrameditor.features;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;
import ac.soton.eventb.diagrameditor.relations.ContextExtendsRelation;
import ac.soton.eventb.diagrameditor.relations.EventBRelation;
import ac.soton.eventb.diagrameditor.relations.MachineRefinesRelation;
import ac.soton.eventb.diagrameditor.relations.MachineSeesRelation;

class CreateExtendsRelationshipFeature extends AbstractCreateConnectionFeature {

	public CreateExtendsRelationshipFeature(IFeatureProvider fp) {
		super(fp, "Extends", "Create an Event-B Extends Relationship"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());
		final EObject target = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getTargetPictogramElement());

		return source instanceof Context && target instanceof Context;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());

		return source instanceof Context;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		final EObject source = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getSourcePictogramElement());
		final EObject target = (EObject) this
				.getBusinessObjectForPictogramElement(context
						.getTargetPictogramElement());

		if (source instanceof Context && target instanceof Context) {
			final ContextExtendsRelation msr = new ContextExtendsRelation(
					(Context) source, (Context) target);
			final AddConnectionContext acc = new AddConnectionContext(
					context.getSourceAnchor(), context.getTargetAnchor());
			acc.setNewObject(msr);
			return (Connection) this.getFeatureProvider().addIfPossible(acc);
		}
		return null;
	}

}

class CreateRefinesRelationshipFeature extends AbstractCreateConnectionFeature {

	public CreateRefinesRelationshipFeature(IFeatureProvider fp) {
		super(fp, "Refines", "Create an Event-B Refines Relationship"); //$NON-NLS-1$ //$NON-NLS-2$
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

class CreateSeesRelationshipFeature extends AbstractCreateConnectionFeature {

	public CreateSeesRelationshipFeature(IFeatureProvider fp) {
		super(fp, "Sees", "Create an Event-B Sees Relationship"); //$NON-NLS-1$ //$NON-NLS-2$
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

public class EventBRelationFeature implements IEventBFeature {
	@Override
	public boolean canAdd() {
		return true;
	}

	@Override
	public boolean canCreate() {
		return false;
	}

	@Override
	public boolean canCreateRelationship() {
		return true;
	}

	@Override
	public boolean canDelete() {
		return true;
	}

	@Override
	public boolean canDirectEdit() {
		return false;
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public Matcher<IAddContext, IAddFeature> getAddMatcher() {
		return new Matcher<IAddContext, IAddFeature>() {

			@Override
			public IAddFeature getFeature(IAddContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBRelationshipAddFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IAddContext o, EventBDiagramFeatureProvider e) {
				return o.getNewObject() instanceof EventBRelation;
			}
		};
	}

	@Override
	public Collection<ICreateFeature> getCreateFeatures(
			EventBDiagramFeatureProvider e) {
		return null;
	}

	@Override
	public Collection<? extends ICreateConnectionFeature> getCreateRelationshipFeatures(
			EventBDiagramFeatureProvider fp) {
		final ArrayList<ICreateConnectionFeature> relationshipList = new ArrayList<>();
		relationshipList.add(new CreateExtendsRelationshipFeature(fp));
		relationshipList.add(new CreateRefinesRelationshipFeature(fp));
		relationshipList.add(new CreateSeesRelationshipFeature(fp));
		return relationshipList;
	}

	@Override
	public Matcher<IDeleteContext, IDeleteFeature> getDeleteMatcher() {
		return new Matcher<IDeleteContext, IDeleteFeature>() {
			@Override
			public IDeleteFeature getFeature(IDeleteContext o,
					EventBDiagramFeatureProvider e) {
				if (this.match(o, e)) {
					return new EventBRelationshipDeleteFeature(e);
				}
				return null;
			}

			@Override
			public boolean match(IDeleteContext o,
					EventBDiagramFeatureProvider e) {
				return e.getBusinessObjectForPictogramElement(o
						.getPictogramElement()) instanceof EventBRelation;
			}
		};
	}

	@Override
	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher() {
		return null;
	}

	@Override
	public Matcher<IUpdateContext, IUpdateFeature> getUpdateMatcher() {
		return null;
	}

}

class EventBRelationshipAddFeature extends AbstractAddFeature {

	public EventBRelationshipAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		final IAddConnectionContext addConContext = (IAddConnectionContext) context;
		final IPeCreateService peCreateService = Graphiti.getPeCreateService();

		// CONNECTION WITH POLYLINE
		final Connection connection = peCreateService
				.createFreeFormConnection(this.getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());

		final IGaService gaService = Graphiti.getGaService();
		final Polyline polyline = gaService.createPolyline(connection);
		polyline.setLineWidth(new Integer(2));
		polyline.setForeground(this.manageColor(IColorConstant.BLACK));

		// create link and wire it
		this.link(connection, context.getNewObject());

		return connection;
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (context instanceof IAddConnectionContext
				&& context.getNewObject() instanceof EventBRelation) {
			return true;
		}
		return false;
	}

}

class EventBRelationshipDeleteFeature extends
org.eclipse.graphiti.ui.features.DefaultDeleteFeature {

	public EventBRelationshipDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void deleteBusinessObject(Object bo) {
		final EventBRelation r = (EventBRelation) bo;
		r.delete();
	}

}
