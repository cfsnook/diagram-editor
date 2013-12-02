package ac.soton.eventb.diagrameditor;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.IIndependenceSolver;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamedCommentedElement;
import org.eventb.emf.core.EventBObject;
import org.eventb.emf.core.Project;
import org.eventb.emf.persistence.ProjectResource;
import org.eventb.emf.persistence.factory.ProjectFactory;
import org.rodinp.core.IRodinElement;

import ac.soton.eventb.diagrameditor.features.EventBComponentDeleteFeature;
import ac.soton.eventb.diagrameditor.features.EventBComponentDirectEditFeature;
import ac.soton.eventb.diagrameditor.features.EventBRelationshipDeleteFeature;
import ac.soton.eventb.diagrameditor.features.add.EventBComponentAddFeature;
import ac.soton.eventb.diagrameditor.features.add.EventBMachineAddFeature;
import ac.soton.eventb.diagrameditor.features.add.EventBRelationshipAddFeature;
import ac.soton.eventb.diagrameditor.features.create.CreateEventBContextFeature;
import ac.soton.eventb.diagrameditor.features.create.CreateEventBMachineFeature;
import ac.soton.eventb.diagrameditor.features.create.CreateExtendsRelationshipFeature;
import ac.soton.eventb.diagrameditor.features.create.CreateRefinesRelationshipFeature;
import ac.soton.eventb.diagrameditor.features.create.CreateSeesRelationshipFeature;
import ac.soton.eventb.diagrameditor.features.update.EventBComponentUpdateFeature;
import ac.soton.eventb.diagrameditor.features.update.EventBProjectUpdateFeature;
import ac.soton.eventb.diagrameditor.relations.ContextExtendsRelation;
import ac.soton.eventb.diagrameditor.relations.EventBRelation;
import ac.soton.eventb.diagrameditor.relations.MachineRefinesRelation;
import ac.soton.eventb.diagrameditor.relations.MachineSeesRelation;

public class EventBDiagramFeatureProvider extends DefaultFeatureProvider {

	private final ProjectResource pr;

	public ProjectResource getProjectResource() {
		return pr;
	}

	public EventBDiagramFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);

		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ProjectFactory());
		this.pr = (ProjectResource) rs.createResource(URI.createPlatformResourceURI("DecompExample", true));
		try {
			pr.load(new HashMap<>());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setIndependenceSolver(new IIndependenceSolver() {

			@Override
			public String getKeyForBusinessObject(Object bo) {
				if(bo instanceof EventBElement) {
					EventBElement element = (EventBElement)bo;
					return EcoreUtil.getURI(element).toString();
				} 
				else if(bo instanceof EventBRelation) {
					return ((EventBRelation) bo).getKey();

				} else {
					return "";
				}

			}

			@Override
			public Object getBusinessObjectForKey(String key) {
				if(key.startsWith("sees:")) {
					return new MachineSeesRelation(key, getProjectResource());
				}
				else if(key.startsWith("extends:")) {
					return new ContextExtendsRelation(key, getProjectResource());
				}
				else if(key.startsWith("refines:")) {
					return new MachineRefinesRelation(key, getProjectResource());
				}
				else {
					URI platformResourceURI = URI.createURI(key, true);
					return pr.getEObject(platformResourceURI.fragment());
				}

			}
		});
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		if(context instanceof IAddConnectionContext) {
			if(context.getNewObject() instanceof EventBRelation) {
				return new EventBRelationshipAddFeature(this);
			}
		} else {
			if (context.getNewObject() instanceof EventBNamedCommentedElement) {
				return new EventBMachineAddFeature(this);
			}
			else if (context.getNewObject() instanceof EventBNamedCommentedElement) {
				return new EventBComponentAddFeature(this);
			}
		}

		return super.getAddFeature(context);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		if(context.getPictogramElement() instanceof Diagram) {
			return new EventBProjectUpdateFeature(this);
		}
		else if(getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof EventBNamedCommentedElement) {
			return new EventBComponentUpdateFeature(this);
		}
		return super.getUpdateFeature(context);
	}



	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		if(getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof EventBNamedCommentedElement) {
			return new EventBComponentDeleteFeature(this);
		}
		else if(getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof EventBRelation) {
			return new EventBRelationshipDeleteFeature(this);
		}
		return super.getDeleteFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return new ICreateFeature[] {
				new CreateEventBMachineFeature(this),
				new CreateEventBContextFeature(this)
		};
	}
	

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] {
				new CreateSeesRelationshipFeature(this),
				new CreateRefinesRelationshipFeature(this),
				new CreateExtendsRelationshipFeature(this)
		};
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(
			IDirectEditingContext context) {

		if(getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof EventBNamedCommentedElement) {
			return new EventBComponentDirectEditFeature(this);
		}

		return super.getDirectEditingFeature(context);
	}

	public Project getProject() {
		return (Project)this.getProjectResource().getContents().get(0);

	}

	public void save() {
		try {
			this.getProjectResource().save(new HashMap<IRodinElement, EventBObject>());
		} catch (IOException e1) {
			Logger.getLogger("diagram-editor").log(Level.SEVERE, "Couldn't Save", e1);
		}

	}

}
