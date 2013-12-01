package ac.soton.eventb.diagrameditor.features.add;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;

public class EventBMachineAddFeature extends EventBComponentAddFeature {

	public EventBMachineAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		PictogramElement toAdd = super.add(context);
		return toAdd;
		
	}
	

}
