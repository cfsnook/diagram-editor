package ac.soton.eventb.diagrameditor.features.create;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.context.ContextFactory;

import ac.soton.eventb.diagrameditor.EventBDiagramFeatureProvider;

public class CreateEventBContextFeature extends AbstractCreateFeature {
	public CreateEventBContextFeature(IFeatureProvider fp) {
		super(fp, "Context", "An EventB Context");

	}

	
	@Override
	public boolean canCreate(ICreateContext context) {
		boolean validContainer = context.getTargetContainer() instanceof Diagram;
		return validContainer;
	}

	@Override
	public Object[] create(ICreateContext context) {
		 Context c = ContextFactory.eINSTANCE.createContext();
		 ((EventBDiagramFeatureProvider)this.getFeatureProvider()).getProject().getComponents().add(c);
		 addGraphicalRepresentation(context, c);
		 getFeatureProvider().getDirectEditingInfo().setActive(true);
		 
		 //((EventBDiagramFeatureProvider)this.getFeatureProvider()).save();
		 return new Object[] { c };
	}
	

}
