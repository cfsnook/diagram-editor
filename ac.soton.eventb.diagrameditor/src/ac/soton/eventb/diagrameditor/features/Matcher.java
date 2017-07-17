package ac.soton.eventb.diagrameditor.features;

import org.eclipse.graphiti.features.IFeature;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;

public abstract class Matcher<T, F extends IFeature> {
	public abstract F getFeature(T o, EventBDiagramFeatureProvider e);

	public abstract boolean match(T o, EventBDiagramFeatureProvider e);
}