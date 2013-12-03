package ac.soton.eventb.diagrameditor.features;

import java.util.Collection;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;

public interface IEventBFeature {
	public boolean canAdd();

	public boolean canCreate();

	public boolean canCreateRelationship();

	public boolean canDelete();

	public boolean canDirectEdit();

	public boolean canUpdate();

	public Matcher<IAddContext, IAddFeature> getAddMatcher();

	public Collection<ICreateFeature> getCreateFeatures(
			EventBDiagramFeatureProvider e);

	public Collection<? extends ICreateConnectionFeature> getCreateRelationshipFeatures(
			EventBDiagramFeatureProvider eventBDiagramFeatureProvider);

	public Matcher<IDeleteContext, IDeleteFeature> getDeleteMatcher();

	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher();

	public Matcher<IUpdateContext, IUpdateFeature> getUpdateMatcher();

}