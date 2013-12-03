package ac.soton.eventb.diagrameditor;

import org.eclipse.core.runtime.IPath;
import org.eclipse.gmf.tooling.runtime.part.DefaultDiagramEditorUtil;

public class GraphitiDiagramEditorUtil {

	public static String getUniqueFileName(IPath containerFullPath,
			String fileName, String extension) {
		return DefaultDiagramEditorUtil.getUniqueFileName(containerFullPath,
				fileName, extension,
				DefaultDiagramEditorUtil.EXISTS_IN_WORKSPACE);
	}
}
