package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;

public interface IEventBFeature {
	public boolean canAdd();

	public boolean canDelete();

	public boolean canDirectEdit();

	public boolean canUpdate();

	public Matcher<IAddContext, IAddFeature> getAddMatcher();

	public Matcher<IDeleteContext, IDeleteFeature> getDeleteMatcher();

	public Matcher<IDirectEditingContext, IDirectEditingFeature> getDirectEditingMatcher();

	public Matcher<IUpdateContext, IUpdateFeature> getUpdateMatcher();

}