package carisma.check.rabac;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Transition;

import carisma.core.analysis.AnalysisHost;
import carisma.core.analysis.OutputFileParameter;
import carisma.core.analysis.result.AnalysisResultMessage;
import carisma.core.analysis.result.StatusType;
import carisma.core.checks.CheckParameter;
import carisma.core.checks.CarismaCheckWithID;
import carisma.profile.umlsec.rabac.UMLsec;
import carisma.profile.umlsec.rabac.UMLsecUtil;

public class RABACConfigCheck implements CarismaCheckWithID, ActionListener {
	
	// Check IDs
	public static final String CHECK_ID = "carisma.check.rabac.configuration"; //$NON-NLS-1$
	public static final String PARAM_CONFIGURATION = "carisma.check.rabac.configuration"; //$NON-NLS-1$
	public static final String CHECK_NAME = "RABACsec: Create transformation input"; //$NON-NLS-1$

	AnalysisHost analysisHost;
	Map<String, CheckParameter> checkParameters;

	RABACConfig config = new RABACConfig();

	private List<String> usersTag;
	private Set<String> users, objects;

	private JComboBox<String> user, role, object, attribute;
	private JComboBox<String> type = new JComboBox<String>(new String[] { "User", "Object" });
	private JCheckBox active = new JCheckBox("Active");
	private JTextField value = new JTextField("", 6);
	private JButton save = new JButton("Save");

	/**
	 * Run the check
	 * 
	 * @param parameters
	 *            parameters for this check
	 * @param host
	 *            deliver analysis results to this host
	 * @return success of the check
	 */
	@Override
	public boolean perform(Map<String, CheckParameter> parameters, AnalysisHost host) {
		this.analysisHost = host;
		this.checkParameters = parameters;
		Resource model = host.getAnalyzedModel();

		if (model.getContents().isEmpty()) {
			host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Empty model"));
			return false;
		}

		if (model.getContents().get(0) instanceof Package) {
			Package content = (Package) model.getContents().get(0);
			List<Element> abac = UMLsecUtil.getStereotypedElements(content, UMLsec.ABAC);
			int abacNum = abac.size();
			if (abacNum == 0) {
				host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Could not find a abac stereotype"));
				return false;
			}
			if (abacNum > 1) {
				host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Found " + abacNum
						+ " abac stereotypes, model should only contain one"));
				return false;
			}
			Element abacClass = abac.get(0);

			this.usersTag = UMLsecUtil.getStringValues("roles", UMLsec.ABAC, abacClass);
			if (this.usersTag.size() == 0) {
				host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Roles missing!"));
				return false;
			}
			this.users = RABACCheck.parseTag(this.usersTag.get(0), null, 0);
			if (this.users.size() == 0) {
				host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Roles missing!"));
				return false;
			}

			List<Element> abacRequire = UMLsecUtil.getStereotypedElements(content, UMLsec.ABACREQUIRE);
			this.objects = new HashSet<String>();
			for (Element e : abacRequire) {
				this.objects.add(e instanceof Transition ? ((Transition) e).containingStateMachine().getName()
						: ((Operation) e).getClass_().getName());
			}

			List<Element> abacAttribute = UMLsecUtil.getStereotypedElements(content, UMLsec.ABACATTRIBUTE);
			for (Element e : abacAttribute) {
				List<String> nameTag = UMLsecUtil.getStringValues("name", UMLsec.ABACATTRIBUTE, e);
				// use name of operation when no explicit one is given
				String name = nameTag.size() == 0 ? ((NamedElement) e).getName() : nameTag.get(0);

				for (Attribute a : this.config.getAttributes()) {
					if (a.getName().equals(name)) {
						host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Duplicate attribute " + name
								+ " !"));
						return false;
					}
				}

				Attribute a = new Attribute();
				a.setName(name);
				this.config.getAttributes().add(a);
			}

			host.addResultMessage(new AnalysisResultMessage(StatusType.INFO, "CHECK_ID stereotypes are valid"));
			configGUI();
			return true;
		}

		host.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Content is not a model!"));
		return false;
	}

	private void configGUI() {
		// build data structure to store session roles
		for (String u : this.users) {
			this.config.getSessions().put(u, new SetWrapper());
		}

		JPanel session = new JPanel();
		((FlowLayout) session.getLayout()).setAlignment(0);
		session.add(new JLabel("User:"));
		this.user = new JComboBox<String>(this.users.toArray(new String[0]));
		session.add(this.user);
		session.add(new JLabel("Role:"));
		this.role = new JComboBox<String>();
		session.add(this.role);
		this.active.setEnabled(false);
		session.add(this.active);

		JPanel filter = new JPanel();
		((FlowLayout) filter.getLayout()).setAlignment(0);
		filter.add(new JLabel("Attribute:"));
		this.attribute = new JComboBox<String>();
		for (Attribute a : this.config.getAttributes()) {
			this.attribute.addItem(a.getName());
		}
		filter.add(this.attribute);
		filter.add(new JLabel("Type:"));
		filter.add(this.type);
		filter.add(new JLabel("Object:"));
		this.object = new JComboBox<String>(this.objects.toArray(new String[0]));
		this.object.setEnabled(false);
		filter.add(this.object);
		filter.add(new JLabel("Value:"));
		filter.add(this.value);

		this.save.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.user.addActionListener(this);
		this.role.addActionListener(this);
		this.active.addActionListener(this);
		this.attribute.addActionListener(this);
		this.type.addActionListener(this);
		this.object.addActionListener(this);
		this.value.addActionListener(this);
		this.save.addActionListener(this);

		JFrame configGUI = new JFrame();
		configGUI.setLayout(new BoxLayout(configGUI.getContentPane(), BoxLayout.Y_AXIS));
		configGUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		configGUI.setTitle("RABACsec transformation input");
		configGUI.setSize(600, 150);
		configGUI.setLocationRelativeTo(null);
		configGUI.add(session);
		configGUI.add(filter);
		configGUI.add(this.save);
		configGUI.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.user) {
			this.role.removeAllItems();
			this.role.setModel(new DefaultComboBoxModel<String>(RABACCheck.parseTag(this.usersTag.get(0),
					(String) this.user.getSelectedItem(), 0).toArray(new String[0])));
			this.role.setSelectedIndex(-1);
			this.active.setEnabled(false);
		}

		if (event.getSource() == this.role) {
			this.active.setEnabled(true);
			this.active.setSelected(this.config.getSessions().get(this.user.getSelectedItem()).getSet()
					.contains(this.role.getSelectedItem()));
		}

		if (event.getSource() == this.active) {
			Set<String> roles = this.config.getSessions().get(this.user.getSelectedItem()).getSet();
			if (this.active.isSelected()) {
				roles.add((String) this.role.getSelectedItem());
			} else {
				roles.remove(this.role.getSelectedItem());
			}
		}

		if (event.getSource() == this.attribute) {
			for (Attribute a : this.config.getAttributes()) {
				if (a.getName().equals(this.attribute.getSelectedItem())) {
					this.type.setSelectedItem(a.getName());
					this.value.setText(a.getValues().get(
							this.type.getSelectedItem().equals("User") ? this.user.getSelectedItem() : this.object.getSelectedItem()));
					break;
				}
			}
		}

		if (event.getSource() == this.type) {
			this.object.setEnabled(!this.type.getSelectedItem().equals("User"));
			updateAttributes();
		}

		if (event.getSource() == this.object) {
			for (Attribute a : this.config.getAttributes()) {
				if (a.getName().equals(this.attribute.getSelectedItem())) {
					this.value.setText(a.getValues().get(this.object.getSelectedItem()));
					break;
				}
			}
		}

		if (event.getSource() == this.value) {
			updateAttributes();
		}

		if (event.getSource() == this.save) {
			// reduce file size
			for (String u : this.users) {
				if (this.config.getSessions().get(u) != null && this.config.getSessions().get(u).getSet().isEmpty()) {
					this.config.getSessions().remove(u);
				}
			}

			try {
				Marshaller m = JAXBContext.newInstance(RABACConfig.class).createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(this.config,
						((OutputFileParameter) this.checkParameters.get(PARAM_CONFIGURATION)).getValue());
			} catch (Exception e) {
				this.analysisHost.addResultMessage(new AnalysisResultMessage(StatusType.ERROR, "Error writing configuration file!"));
			}
			this.analysisHost.addResultMessage(new AnalysisResultMessage(StatusType.INFO, "Saved configuration file"));
		}
	}

	private void updateAttributes() {
		for (Attribute a : this.config.getAttributes()) {
			if (a.getName().equals(this.attribute.getSelectedItem())) {
				a.setType((String) this.type.getSelectedItem());
				a.getValues().put(
						(String) (this.type.getSelectedItem().equals("User") ? this.user.getSelectedItem()
								: this.object.getSelectedItem()), this.value.getText());
				break;
			}
		}
	}

	@Override
	public String getCheckID() {
		return CHECK_ID;
	}

	@Override
	public String getName() {
		return CHECK_NAME;
	}

}