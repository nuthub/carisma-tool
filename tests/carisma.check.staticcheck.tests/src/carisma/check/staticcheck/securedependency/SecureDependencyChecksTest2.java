package carisma.check.staticcheck.securedependency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import carisma.profile.umlsec.UMLsecActivator;
import carisma.profile.umlsec.UmlsecPackage;


public class SecureDependencyChecksTest2 {
	
	private String filepath = "resources/models2";

	private ResourceSet rs;
	private Model model = null;

	@After
	public final void cleanup() {
		this.model.eResource().unload();
		this.model = null;
	}
	
	@Before
	public final void init() throws FileNotFoundException, IOException {
		UMLResourcesUtil.initGlobalRegistries();
		
		this.rs = new ResourceSetImpl();
		UMLResourcesUtil.initLocalRegistries(rs);
		
		Map<String, Object> extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
		extensionToFactoryMap.put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		extensionToFactoryMap.put("*",
				UMLResource.Factory.INSTANCE);

		Registry packageRegistry = this.rs.getPackageRegistry();
		packageRegistry.put(UmlsecPackage.eNS_URI, UmlsecPackage.eINSTANCE);
		UMLsecActivator.loadUMLsecProfile(this.rs);	
		EcoreUtil.resolveAll(this.rs);
	}
	
	@Test
	public final void BasicCasesTest01() {
		this.model = loadModel(this.filepath, "BasicCases-V-1.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	public final void BasicCasesTest02() {
		this.model = loadModel(this.filepath, "BasicCases-2.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void BasicCasesTest03() {
		this.model = loadModel(this.filepath, "BasicCases-V-3.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(2, secureDependencyViolations.size());
	}
	@Test
	public final void BasicCasesTest04() {
		this.model = loadModel(this.filepath, "BasicCases-4.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void BasicCasesTest05() {
		this.model = loadModel(this.filepath, "BasicCases-V-5.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void BasicCasesTest06() {
		this.model = loadModel(this.filepath, "BasicCases06.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	
	
	@Test
	public final void InheritanceCasesTest01() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-1.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}	
	@Test
	public final void InheritanceCasesTest02() {
		this.model = loadModel(this.filepath, "InheritanceCases-2.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest03() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-3.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest04() {
		this.model = loadModel(this.filepath, "InheritanceCases-4.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(2, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest05() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-5.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest06() {
		this.model = loadModel(this.filepath, "InheritanceCases-6.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest07() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-7.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest08() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-8.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest09() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-9.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest01() {
		this.model = loadModel(this.filepath, "OverridingCases-V-1.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest02() {
		this.model = loadModel(this.filepath, "OverridingCases-2.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest03() {
		this.model = loadModel(this.filepath, "OverridingCases-V-3.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest04() {
		this.model = loadModel(this.filepath, "OverridingCases-V-4.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest05() {
		this.model = loadModel(this.filepath, "OverridingCases-5.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest06() {
		this.model = loadModel(this.filepath, "OverridingCases-V-6.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest07() {
		this.model = loadModel(this.filepath, "OverridingCases-V-7.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void OverridingCasesTest09() {
		this.model = loadModel(this.filepath, "OverridingCases-V-9.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(2, secureDependencyViolations.size());
	}
	
	@Test
	public final void InheritanceCasesTest10() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-10.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}	
	@Test
	public final void InheritanceCasesTest11() {
		this.model = loadModel(this.filepath, "InheritanceCases-11.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest12() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-12.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest13() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-13.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(2, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest14() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-14.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(1, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest15() {
		this.model = loadModel(this.filepath, "InheritanceCases-V-15.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(2, secureDependencyViolations.size());
	}
	@Test
	public final void InheritanceCasesTest16() {
		this.model = loadModel(this.filepath, "InheritanceCases-16.uml");
		SecureDependencyChecks sdc = new SecureDependencyChecks(null);
		sdc.checkSecureDependency(this.model);
		List<SecureDependencyViolation> secureDependencyViolations = sdc.getViolations();
		assertEquals(0, secureDependencyViolations.size());
	}
	
	private Model loadModel(String filepath, String name) {
		File file = new File(new File(filepath), name);
		if (file.exists()) {
			URI umlResourcePluginURI = URI.createURI(file.getAbsolutePath());
			Resource r = rs.createResource(umlResourcePluginURI);
			try (FileInputStream in = new FileInputStream(file)) {
				r.load(in, Collections.EMPTY_MAP);
				EList<EObject> contents = r.getContents();
				assertTrue(1 <= contents.size());
				EObject obj = contents.get(0);
				assertTrue(obj instanceof Model);
				return (Model) obj;
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
		} else {
			fail("File \"" + new File(new File(filepath), name) + "\" doesn't exist!");
		}
		return null;
	}

	protected static void registerPathmaps(URI umlResourcePluginURI) {
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
				umlResourcePluginURI.appendSegment("libraries").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
				umlResourcePluginURI.appendSegment("metamodels").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
				umlResourcePluginURI.appendSegment("profiles").appendSegment(""));

	}

}
