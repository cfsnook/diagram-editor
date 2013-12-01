package ac.soton.eventb.diagrameditor.relations;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.persistence.ProjectResource;

public class ContextExtendsRelation implements EventBRelation {
	Context source;
	Context target;
	//source extends target
	
	public ContextExtendsRelation(Context source, Context target) {
		this.source = source;
		this.target = target;
		if(!source.getExtends().contains(target)) {
			source.getExtends().add(target);
		}
	}

	public ContextExtendsRelation(String key, ProjectResource pr) {
		String[] keys = key.substring("extends:".length()).split("<!extends!>");
		this.source = (Context) pr.getEObject(URI.createURI(keys[0], true).fragment());
		this.target = (Context) pr.getEObject(URI.createURI(keys[1], true).fragment());
	}

	public Context getSource() {
		return source;
	}
	public Context getTarget() {
		return target;
	}

	@Override
	public String getKey() {
		return "extends:" + 
				EcoreUtil.getURI(this.getSource()).toString() +
				"<!extends!>" +
				EcoreUtil.getURI(this.getTarget()).toString();
	}

	@Override
	public void delete() {
		this.getSource().getExtends().remove(getTarget());
		
	}
}
