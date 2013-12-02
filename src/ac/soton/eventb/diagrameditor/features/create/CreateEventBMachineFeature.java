package ac.soton.eventb.diagrameditor.features.create;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.core.machine.MachineFactory;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;

public class CreateEventBMachineFeature extends AbstractCreateFeature {
	public CreateEventBMachineFeature(IFeatureProvider fp) {
		super(fp, "Machine", "An EventB Machine");

	}

	
	@Override
	public boolean canCreate(ICreateContext context) {
		boolean validContainer = context.getTargetContainer() instanceof Diagram;
		return validContainer;
	}

	@Override
	public Object[] create(ICreateContext context) {
		 Machine m = MachineFactory.eINSTANCE.createMachine();
		 ((EventBDiagramFeatureProvider)this.getFeatureProvider()).getProject().getComponents().add(m);
		 addGraphicalRepresentation(context, m);
		 getFeatureProvider().getDirectEditingInfo().setActive(true);
		 
		 //((EventBDiagramFeatureProvider)this.getFeatureProvider()).save();
		 return new Object[] { m };
	}
	

}
