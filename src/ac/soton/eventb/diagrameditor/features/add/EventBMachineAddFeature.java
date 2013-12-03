package ac.soton.eventb.diagrameditor.features.add;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class EventBMachineAddFeature extends EventBComponentAddFeature {

	public EventBMachineAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		final PictogramElement toAdd = super.add(context);
		return toAdd;

	}

}
