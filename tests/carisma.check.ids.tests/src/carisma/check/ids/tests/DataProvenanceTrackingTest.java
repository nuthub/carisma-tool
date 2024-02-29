package carisma.check.ids.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import carisma.check.idschecks.dataprovenancecheck.DataProvenanceCheck;

import carisma.core.analysis.AnalysisHost;
import carisma.core.analysis.RegisterInUseException;
import carisma.core.analysis.RegisterNotInUseException;
import carisma.core.analysis.UserAbortedAnalysisException;
import carisma.core.analysis.result.AnalysisResultMessage;
import carisma.core.logging.LogLevel;
import carisma.core.logging.Logger;
import carisma.modeltype.uml2.StereotypeApplication;
import carisma.profile.umlsec.umlsec4ids.*;

/**
 * JUnit test-file for the DataProvenanceTracking of the Umlsec4IDS plugin.
 * @author Alexander Peikert
 *
 */
public class DataProvenanceTrackingTest {
private String filepath = "resources/models/dataprovenancetracking";
	
	private ResourceSet rs = new ResourceSetImpl();
	
	private Resource modelres = null;
	
	private Model model = null;
	
	public final void loadModel(final String testmodelname) throws IOException {
		File testmodelfile = new File(this.filepath + File.separator + testmodelname);
		assertTrue(testmodelfile.exists());
		this.modelres = this.rs.createResource(URI.createFileURI(testmodelfile.getAbsolutePath()));
		this.modelres.load(Collections.EMPTY_MAP);
		this.model = (Model) this.modelres.getContents().get(0);
	}
	
	//--------------------------------------------------------------------------------------------------
		private class TestHost implements AnalysisHost {

			public TestHost() {
				// TODO Auto-generated constructor stub
			}

			@Override
			public void addResultMessage(final AnalysisResultMessage detail) {
				Logger.log(LogLevel.INFO, detail.getText());
			}

			@Override
			public void appendToReport(final String text) {
				Logger.log(LogLevel.INFO, text);			
			}

			@Override
			public void appendLineToReport(final String text) {
				Logger.log(LogLevel.INFO, text);			
			}

			@Override
			public Resource getAnalyzedModel() {
				return DataProvenanceTrackingTest.this.modelres;
			}

			@Override
			public String getCurrentModelFilename() {
				return DataProvenanceTrackingTest.this.modelres.getURI().toFileString();
			}

			@Override
			public void putToRegister(final String registerName, final Object data)
					throws RegisterInUseException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean isRegisterInUse(final String registerName) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object getFromRegister(final String registerName)
					throws RegisterNotInUseException {
				return null;
			}

			@Override
			public Object removeFromRegister(final String registerName)
					throws RegisterNotInUseException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void displayError(final String message) {
				// TODO Auto-generated method stub
				Logger.log(LogLevel.INFO, message);
			}

			@Override
			public File getFileToBeWritten(final File file)
					throws UserAbortedAnalysisException {
				// TODO Auto-generated method stub
				return file;
			}
		}
		//--------------------------------------------------------------------------------------------------
		
		// test for empty model
		@Test
		public void testEmptyModelDPT() throws IOException {
			loadModel("dataprovenancetracking.uml");
			DataProvenanceCheck check1 = new DataProvenanceCheck();
			List<Element> dpt = UMLsecUtil.getStereotypedElements(model, UMLsec.DATAPROVENANCETRACKING);
			assertEquals(0, dpt.size());
			StereotypeApplication dptApp = UMLsecUtil.getStereotypeApplication(this.model, UMLsec.DATAPROVENANCETRACKING);
			assertNull(dptApp);
			TestHost analysisHost = new TestHost();
			assertFalse(check1.perform(null, analysisHost));
			this.modelres.unload();
		}
		

		
		
		// test for no clearing house
		@Test
		public void testNoClearingHouseDPT() throws IOException {
			loadModel("dataprovenancetracking_no_clearing.uml");
			DataProvenanceCheck check2 = new DataProvenanceCheck();
			List<Element> dpt = UMLsecUtil.getStereotypedElements(model, UMLsec.DATAPROVENANCETRACKING);
			assertEquals(1, dpt.size());
			TestHost analysisHost = new TestHost();
			assertFalse(check2.perform(null, analysisHost));
			this.modelres.unload();
		}
		
		// test for protected action not in clearing house
		@Test
		public void testProtectedActionNotInClearingHouseDPT() throws IOException {
			loadModel("dataprovenancetracking_protected_no_clearing.uml");
			DataProvenanceCheck check3 = new DataProvenanceCheck();
			List<Element> dpt = UMLsecUtil.getStereotypedElements(model, UMLsec.DATAPROVENANCETRACKING);
			assertEquals(1, dpt.size());
			TestHost analysisHost = new TestHost();
			assertFalse(check3.perform(null, analysisHost));
			this.modelres.unload();
		}
		
		// test for subpartitions in the model
		@Test
		public void testSubpartitionsDPT() throws IOException {
			loadModel("dataprovenancetracking_subpartition.uml");
			DataProvenanceCheck check4 = new DataProvenanceCheck();
			List<Element> dpt = UMLsecUtil.getStereotypedElements(model, UMLsec.DATAPROVENANCETRACKING);
			assertEquals(1, dpt.size());
			TestHost analysisHost = new TestHost();
			assertTrue(check4.perform(null, analysisHost));
			this.modelres.unload();
		}
		
		// test for no dataprovenancetracking stereotype applied
		@Test
		public void testNoStereotypeDPT() throws IOException {
			loadModel("dataprovenancetracking_no_stereo.uml");
			DataProvenanceCheck check5 = new DataProvenanceCheck();
			List<Element> dpt = UMLsecUtil.getStereotypedElements(model, UMLsec.DATAPROVENANCETRACKING);
			assertEquals(0, dpt.size());
			TestHost analysisHost = new TestHost();
			assertTrue(check5.perform(null, analysisHost));
			this.modelres.unload();
		}
		
		// test for correct model
		@Test
		public void testCorrectModelDPT() throws IOException {
			loadModel("dataprovenancetracking_correct_model.uml");
			DataProvenanceCheck check6 = new DataProvenanceCheck();
			List<Element> dpt = UMLsecUtil.getStereotypedElements(model, UMLsec.DATAPROVENANCETRACKING);
			assertEquals(1, dpt.size());
			TestHost analysisHost = new TestHost();
			assertTrue(check6.perform(null, analysisHost));
			this.modelres.unload();
		}


}