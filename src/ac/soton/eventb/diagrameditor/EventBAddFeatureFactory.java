package ac.soton.eventb.diagrameditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.IAddContext;




public class EventBAddFeatureFactory {
	private static EventBAddFeatureFactory eventBAddFeatureFactory;
	private List<Matcher<IAddContext, IAddFeature>> featureList;

	private EventBAddFeatureFactory() {
		EventBAddFeatureFactory.eventBAddFeatureFactory = this;
		this.featureList = new ArrayList<>();
	}
	public static EventBAddFeatureFactory getInstance() {
		if(eventBAddFeatureFactory != null) {
			return eventBAddFeatureFactory;
		}
		eventBAddFeatureFactory = new EventBAddFeatureFactory();
		return eventBAddFeatureFactory;
		
	}
	public IAddFeature getFeature(IAddContext context, EventBDiagramFeatureProvider f) {
		for(Matcher<IAddContext, IAddFeature> m : featureList) {
			if(m.match(context)) {
				return m.getFeature(context, f);
			}
		}
		throw new UnsupportedOperationException("Factory couldn't instantiate from " + context.toString());
	}
	public void register(Matcher<IAddContext,IAddFeature> matcher) {
		this.featureList.add(matcher);
	}
}
