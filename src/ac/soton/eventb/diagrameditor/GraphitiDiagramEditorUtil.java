package ac.soton.eventb.diagrameditor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.tooling.runtime.part.DefaultDiagramEditorUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.ui.statushandlers.StatusManager;

public abstract class GraphitiDiagramEditorUtil {

	/** the diagram type name of the graphiti diagram. */
	private static String diagramTypeName;
	/** the grid size (0 means no grid). */
	private static int gridSize = 0;
	/** the setting for snapping to grid. */
	private static boolean snapToGrid = false;

	public static String getUniqueFileName(IPath containerFullPath,
			String fileName, String extension) {
		return DefaultDiagramEditorUtil.getUniqueFileName(containerFullPath,
				fileName, extension,
				DefaultDiagramEditorUtil.EXISTS_IN_WORKSPACE);
	}

	public static Resource createDiagram(final URI diagramURI,
			final URI modelURI, IProgressMonitor progressMonitor) {
		TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE
				.createEditingDomain();
		progressMonitor.beginTask("Creating diagram and model files", 2);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		CommandStack commandStack = editingDomain.getCommandStack();

		final Resource diagramResource = resourceSet.createResource(diagramURI);
		final Resource modelResource = resourceSet.createResource(modelURI);

		if (diagramResource != null && modelResource != null) {
			commandStack.execute(new RecordingCommand(editingDomain) {

				@Override
				protected void doExecute() {
					createModel(diagramResource, diagramURI.lastSegment(),
							modelResource, modelURI.lastSegment());

				}

			});

			progressMonitor.worked(1);

			try {
				modelResource.save(createSaveOptions());
				diagramResource.save(createSaveOptions());
			} catch (IOException e) {
				IStatus status = new Status(
						IStatus.ERROR,
						"ac.soton.eventb.diagrameditor.GraphitiDiagramCreationWizardID",
						"Unable to store model and diagram resources", e);
				StatusManager.getManager().handle(status);
			}
			setCharset(WorkspaceSynchronizer.getFile(modelResource));
			setCharset(WorkspaceSynchronizer.getFile(diagramResource));
		}
		progressMonitor.done();

		return null;

	}

	public static Map<?, ?> createSaveOptions() {
		HashMap<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
				Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
		return saveOptions;
	}

	public static void setCharset(final IFile file) {
		try {
			if (file != null) {
				file.setCharset("UTF-8", new NullProgressMonitor());
			}
		} catch (CoreException e) {

		}
	}

	private static void createModel(final Resource diagramResource,
			final String diagramName, final Resource modelResource,
			final String modelName) {
		modelResource.setTrackingModification(true);
		EObject domainModel = createModel(modelName);
		modelResource.getContents().add(domainModel);
		diagramResource.setTrackingModification(true);
		Diagram diagram = Graphiti.getPeCreateService().createDiagram(
				diagramTypeName, diagramName, gridSize, snapToGrid);
		PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
		link.setPictogramElement(diagram);
		link.getBusinessObjects().add(domainModel);
		configureDiagram(diagram);
		diagramResource.getContents().add(diagram);
	}

	private static EObject createModel(String modelName) {
		// TODO Auto-generated method stub
		return null;
	}

	protected static void configureDiagram(final Diagram diagram) {
		// the default implementation does nothing
	}

}
