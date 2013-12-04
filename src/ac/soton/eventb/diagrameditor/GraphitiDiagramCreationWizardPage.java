package ac.soton.eventb.diagrameditor;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class GraphitiDiagramCreationWizardPage extends
		WizardNewFileCreationPage {
	private final String fileExtension;
	private String extension;

	public GraphitiDiagramCreationWizardPage(String pageName,
			IStructuredSelection selection, String fileExtension) {
		super(pageName, selection);
		this.fileExtension = fileExtension;
		// TODO Auto-generated constructor stub
	}

	protected String getExtension() {
		return fileExtension;
	}

	public URI getURI() {
		IPath path = getContainerFullPath();
		if (path == null) {
			path = new Path("");
		}
		String fileName = getFileName();
		if (fileName == null || fileName.length() == 0) {
			fileName = copyFileName();
		}
		path = path.append(getUniqueFileName(path, fileName, extension));
		return URI.createPlatformResourceURI(path.toString(), false);
	}

	private String copyFileName() {
		String fileName = null;
		IWizardPage previousPage = getPreviousPage();
		if (previousPage instanceof GraphitiDiagramCreationWizardPage) {
			GraphitiDiagramCreationWizardPage copyPage = (GraphitiDiagramCreationWizardPage) previousPage;
			fileName = copyPage.getFileName();
			if (fileName.endsWith(copyPage.extension)) {
				fileName = fileName.substring(0, fileName.length()
						- (copyPage.extension.length() + 1));
			}
		}
		return fileName;
	}

	protected IPath getFilePath() {
		IPath path = getContainerFullPath();
		if (path == null) {
			path = new Path("");
		}
		String fileName = getFileName();
		if (fileName != null) {
			path = path.append(fileName);
		}
		return path;
	}

	private static String getUniqueFileName(final IPath containerFullPath,
			final String fileName, final String extension) {
		IPath contPath = containerFullPath == null ? new Path("")
				: containerFullPath;
		String name = (fileName == null || fileName.trim().length() == 0) ? "default"
				: fileName;
		IPath filePath = contPath.append(name);
		if (extension.equals(filePath.getFileExtension())) {
			name = name.substring(0, name.length() - (extension.length() + 1));
		} else {
			filePath = filePath.addFileExtension(extension);
		}
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		if (workspaceRoot.exists(filePath)) {
			int x = getNumber(name);
			if (x >= 0) {
				name = name.substring(0, name.length() - digits(x));
			} else {
				x = 0;
			}
			do {
				x++;
				String newName = name + x + "." + extension;
				filePath = filePath.append(newName);
			} while (workspaceRoot.exists(filePath));
		}
		return filePath.lastSegment();
	}

	private static int getNumber(final String string) {
		int index = string.length();
		while (index > 0 && Character.isDigit(string.charAt(index - 1))) {
			index--;
		}
		if (index < string.length()) {
			try {
				return Integer.parseInt(string.substring(index));
			} catch (NumberFormatException exception) {
				// ignore exception and return 0
			}
		}
		return -1;
	}

	private static final int BASE = 10;

	private static int digits(final int x) {
		int digits = 1;
		int a = x;
		while (a >= BASE) {
			a /= BASE;
			digits++;
		}
		return digits;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName(GraphitiDiagramEditorUtil.getUniqueFileName(
				getContainerFullPath(), getFileName(), getExtension()));
		setPageComplete(validatePage());
	}

	protected boolean validatePage() {
		if (!super.validatePage()) {
			return false;
		}
		String extension = getExtension();
		if (extension != null
				&& !getFilePath().toString().endsWith("." + extension)) {
			return false;
		}
		return true;
	}
}
