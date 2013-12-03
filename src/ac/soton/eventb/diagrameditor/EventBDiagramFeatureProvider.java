package ac.soton.eventb.diagrameditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.IIndependenceSolver;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBObject;
import org.eventb.emf.core.Project;
import org.eventb.emf.persistence.ProjectResource;
import org.eventb.emf.persistence.factory.ProjectFactory;
import org.rodinp.core.IRodinElement;

import ac.soton.eventb.diagrameditor.features.EventBElementFeature;
import ac.soton.eventb.diagrameditor.features.EventBFeatureFactory;
import ac.soton.eventb.diagrameditor.features.EventBProjectFeature;
import ac.soton.eventb.diagrameditor.features.EventBRelationFeature;
import ac.soton.eventb.diagrameditor.features.IEventBFeature;
import ac.soton.eventb.diagrameditor.relations.ContextExtendsRelation;
import ac.soton.eventb.diagrameditor.relations.EventBRelation;
import ac.soton.eventb.diagrameditor.relations.MachineRefinesRelation;
import ac.soton.eventb.diagrameditor.relations.MachineSeesRelation;

public class EventBDiagramFeatureProvider extends DefaultFeatureProvider {

	private final class EventBIndependenceSolver implements IIndependenceSolver {
		@Override
		public Object getBusinessObjectForKey(String key) {
			if (key.startsWith("sees:")) {
				return new MachineSeesRelation(key,
						EventBDiagramFeatureProvider.this.getProjectResource());
			} else if (key.startsWith("extends:")) {
				return new ContextExtendsRelation(key,
						EventBDiagramFeatureProvider.this.getProjectResource());
			} else if (key.startsWith("refines:")) {
				return new MachineRefinesRelation(key,
						EventBDiagramFeatureProvider.this.getProjectResource());
			} else {
				final URI platformResourceURI = URI.createURI(key, true);
				return EventBDiagramFeatureProvider.this.pr
						.getEObject(platformResourceURI.fragment());
			}

		}

		@Override
		public String getKeyForBusinessObject(Object bo) {
			if (bo instanceof EventBElement) {
				final EventBElement element = (EventBElement) bo;
				return EcoreUtil.getURI(element).toString();
			} else if (bo instanceof EventBRelation) {
				return ((EventBRelation) bo).getKey();

			} else {
				return "";
			}

		}
	}

	private final List<ICreateFeature> createFeatures;

	private final List<ICreateConnectionFeature> createRelationshipFeatures;
	private final EventBFeatureFactory<IAddContext, IAddFeature> eventBAddFeatureFactory;
	private final EventBFeatureFactory<IDeleteContext, IDeleteFeature> eventBDeleteFeatureFactory;
	private final EventBFeatureFactory<IDirectEditingContext, IDirectEditingFeature> eventBDirectEditingFeatureFactory;
	private final EventBFeatureFactory<IUpdateContext, IUpdateFeature> eventBUpdateFeatureFactory;

	private final ProjectResource pr;

	public EventBDiagramFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);

		final ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
		.put("*", new ProjectFactory());
		this.pr = (ProjectResource) rs.createResource(URI
				.createPlatformResourceURI("DemoProject", true));
		try {
			this.pr.load(new HashMap<>());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.createFeatures = new ArrayList<>();
		this.createRelationshipFeatures = new ArrayList<>();
		this.eventBAddFeatureFactory = new EventBFeatureFactory<>();
		this.eventBDeleteFeatureFactory = new EventBFeatureFactory<>();
		this.eventBDirectEditingFeatureFactory = new EventBFeatureFactory<>();
		this.eventBUpdateFeatureFactory = new EventBFeatureFactory<>();

		final IEventBFeature[] features = { new EventBElementFeature(),
				new EventBRelationFeature(), new EventBProjectFeature() };

		for (final IEventBFeature f : features) {
			if (f.canAdd()) {
				this.eventBAddFeatureFactory.register(f.getAddMatcher());
			}
			if (f.canDelete()) {
				this.eventBDeleteFeatureFactory.register(f.getDeleteMatcher());
			}
			if (f.canDirectEdit()) {
				this.eventBDirectEditingFeatureFactory.register(f
						.getDirectEditingMatcher());
			}
			if (f.canUpdate()) {
				this.eventBUpdateFeatureFactory.register(f.getUpdateMatcher());
			}
			if (f.canCreate()) {
				this.createFeatures.addAll(f.getCreateFeatures(this));
			}
			if (f.canCreateRelationship()) {
				this.createRelationshipFeatures.addAll(f
						.getCreateRelationshipFeatures(this));
			}
		}

		Logger.getAnonymousLogger().severe(String.format("%d Features and %d Relations", this.createFeatures.size(), this.createRelationshipFeatures.size()));

		this.setIndependenceSolver(new EventBIndependenceSolver());
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		return this.eventBAddFeatureFactory.getFeature(context, this);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return this.createRelationshipFeatures.toArray(new ICreateConnectionFeature[] {});
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return this.createFeatures.toArray(new ICreateFeature[] {});
	}

	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		return this.eventBDeleteFeatureFactory.getFeature(context, this);
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(
			IDirectEditingContext context) {
		return this.eventBDirectEditingFeatureFactory.getFeature(context, this);
	}

	public Project getProject() {
		return (Project) this.getProjectResource().getContents().get(0);

	}

	public ProjectResource getProjectResource() {
		return this.pr;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		return this.eventBUpdateFeatureFactory.getFeature(context, this);
	}

	public void save() {
		try {
			this.getProjectResource().save(
					new HashMap<IRodinElement, EventBObject>());
		} catch (final IOException e1) {
			Logger.getLogger("diagram-editor").log(Level.SEVERE,
					"Couldn't Save", e1);
		}

	}

}
