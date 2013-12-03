package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.IAddContext;

public interface IFeature {
	public Matcher<IAddContext,IAddFeature> getAddMatcher();

	public boolean canAdd();
}