package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IFeature;

public abstract class Matcher<T, F extends IFeature> {
	public abstract F getFeature(T o, EventBDiagramFeatureProvider e);

	public abstract boolean match(T o, EventBDiagramFeatureProvider e);
}