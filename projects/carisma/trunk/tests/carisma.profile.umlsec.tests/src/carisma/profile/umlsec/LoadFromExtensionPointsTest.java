package carisma.profile.umlsec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.papyrus.infra.services.resourceloading.OnDemandLoadingModelSet;
import org.eclipse.uml2.uml.Profile;
import org.junit.Test;

public class LoadFromExtensionPointsTest {

	private final static String PROFILE_PLUGIN_URI = "platform:/plugin/carisma.profile.umlsec/profile/UMLsec.profile.uml";
	
	@Test
	public void plugin() throws IOException{
		ResourceSet rs = new ResourceSetImpl();
		URI uri = URI.createURI(PROFILE_PLUGIN_URI);
		Resource r = rs.createResource(uri);
		r.load(Collections.EMPTY_MAP);
		assertTrue(r.getContents().size() == 1);
		EObject eObject = r.getContents().get(0);
		assertTrue(eObject instanceof Profile);
		Profile profile = (Profile) eObject;
		assertTrue(profile.getURI().equals(UMLsecActivator.UML_URI));
	}
	
	@Test
	public void papyrus() throws IOException{
		ResourceSet rs = new OnDemandLoadingModelSet();
		URI uri = URI.createURI(PROFILE_PLUGIN_URI);
		Resource r = rs.createResource(uri);
		r.load(Collections.EMPTY_MAP);
		assertTrue(r.getContents().size() == 1);
		EObject eObject = r.getContents().get(0);
		assertTrue(eObject instanceof Profile);
		Profile profile = (Profile) eObject;
		assertTrue(profile.getURI().equals(UMLsecActivator.UML_URI));
	}
}