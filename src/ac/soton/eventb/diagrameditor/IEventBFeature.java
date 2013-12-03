package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;

public interface IEventBFeature {
	public boolean canAdd();

	public boolean canDelete();

	public Matcher<IAddContext, IAddFeature> getAddMatcher();

	public Matcher<IDeleteContext, IDeleteFeature> getDeleteMatcher();
}