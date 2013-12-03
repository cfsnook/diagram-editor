package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IAddFeature;

abstract class Matcher<T, F extends IAddFeature> {
	public abstract boolean match(T o);
	public abstract F getFeature(T o, EventBDiagramFeatureProvider e);
}