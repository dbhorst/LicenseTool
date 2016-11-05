package com.beckerhorst;

import org.springframework.beans.factory.annotation.Autowired;

import com.beckerhorst.db.License;
import com.beckerhorst.db.LicenseRepository;
import com.beckerhorst.translation.TranslationKeys;
import com.beckerhorst.translation.TranslationLocalizer;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The license editor to edit new and existing licenses.
 * 
 * @author dirk
 */
@SpringComponent
@UIScope
public class LicenseEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final LicenseRepository repo;

	/* The current edited license */
	private License license;

	/* Fields to edit properties in License entity */
	TextField licenseName = new TextField(TranslationLocalizer.get(TranslationKeys.LICENSE_NAME));
	DateField purchaseDateAsDate = new DateField(TranslationLocalizer.get(TranslationKeys.LICENSE_PURCHASE_DATE));
	DateField renewalDateAsDate = new DateField(TranslationLocalizer.get(TranslationKeys.LICENSE_RENEWAL_DATE));
	DateField expirationDateAsDate = new DateField(TranslationLocalizer.get(TranslationKeys.LICENSE_EXPIRATION_DATE));

	/* Action buttons */
	Button save = new Button(TranslationLocalizer.get(TranslationKeys.SAVE), FontAwesome.SAVE);
	Button cancel = new Button(TranslationLocalizer.get(TranslationKeys.CANCEL));
	CssLayout actions = new CssLayout(save, cancel);

	@Autowired
	public LicenseEditor(LicenseRepository repo) {
		this.repo = repo;

		// Add the inputs and buttons to the ui
		addComponents(licenseName, purchaseDateAsDate, renewalDateAsDate, expirationDateAsDate, actions);

		// Configure and style components
		setSpacing(true);
		setMargin(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire save button
		save.addClickListener(e -> repo.save(license));
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editLicense(License l) {
		// If license is null, do nothing
		if (l == null) {
			return;
		}

		// new or edit license
		final boolean persisted = l.getId() != null;

		if (persisted) {
			// Find fresh entity for editing
			license = repo.findOne(l.getId());
		} else {
			license = l;
		}

		// no cancel button, if new license
		cancel.setVisible(persisted);

		// Bind license properties to similarly named fields
		BeanFieldGroup.bindFieldsUnbuffered(license, this);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in licenseName field automatically
		licenseName.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or cancel
		// is clicked
		save.addClickListener(e -> h.onChange());
		cancel.addClickListener(e -> h.onChange());
	}
}
