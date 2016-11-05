package com.beckerhorst;

import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.beckerhorst.db.License;
import com.beckerhorst.db.LicenseRepository;
import com.beckerhorst.db.StringToCalendarConverter;
import com.beckerhorst.translation.TranslationKeys;
import com.beckerhorst.translation.TranslationLocalizer;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The main UI for the license tool.
 * 
 * @author dirk
 */
@SpringUI
@Theme("valo")
public class LicenseToolsUI extends UI {

	private static final long serialVersionUID = 1L;
	
	/* properties from the license class */
	private static final String PROPERTY_EXPIRATION_DATE = "expirationDate";
	private static final String PROPERTY_RENEWAL_DATE = "renewalDate";
	private static final String PROPERTY_PURCHASE_DATE = "purchaseDate";
	private static final String PROPERTY_LICENSE_NAME = "licenseName";

	private final LicenseRepository repo;
	private final LicenseEditor editor;
	private final Grid grid;
	private final TextField nameFilter;
	private final DateField expirationFromFilter;
	private final DateField expirationToFilter;
	private final Button searchBtn;
	private final Button addNewBtn;
	private final Button editBtn;
	private final Button deleteBtn;
	private final PopupView popup;

	@Autowired
	public LicenseToolsUI(LicenseRepository repo, LicenseEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid();
		this.nameFilter = new TextField(TranslationLocalizer.get(TranslationKeys.LICENSE_NAME));
		this.expirationFromFilter = new DateField(
				TranslationLocalizer.get(TranslationKeys.LICENSE_EXPIRATION_DATE_FROM));
		this.expirationToFilter = new DateField(TranslationLocalizer.get(TranslationKeys.LICENSE_EXPIRATION_DATE_TO));
		this.searchBtn = new Button(TranslationLocalizer.get(TranslationKeys.SEARCH), FontAwesome.SEARCH);
		this.addNewBtn = new Button(TranslationLocalizer.get(TranslationKeys.NEW_LICENSE), FontAwesome.PLUS);
		this.editBtn = new Button(TranslationLocalizer.get(TranslationKeys.EDIT), FontAwesome.PENCIL);
		this.deleteBtn = new Button(TranslationLocalizer.get(TranslationKeys.DELETE), FontAwesome.TRASH_O);
		this.popup = new PopupView("", editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout search = new HorizontalLayout(nameFilter, expirationFromFilter, expirationToFilter, searchBtn);
		HorizontalLayout actions = new HorizontalLayout(addNewBtn, editBtn, deleteBtn, popup);
		VerticalLayout mainLayout = new VerticalLayout(search, grid, actions);
		setContent(mainLayout);

		// Configure layouts and components
		search.setSpacing(true);
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(400, Unit.PIXELS);
		grid.setWidth(800, Unit.PIXELS);
		grid.setColumns(PROPERTY_LICENSE_NAME, PROPERTY_PURCHASE_DATE, PROPERTY_RENEWAL_DATE, PROPERTY_EXPIRATION_DATE);
		grid.getDefaultHeaderRow().getCell(PROPERTY_LICENSE_NAME)
				.setText(TranslationLocalizer.get(TranslationKeys.LICENSE_NAME));
		grid.getDefaultHeaderRow().getCell(PROPERTY_PURCHASE_DATE)
				.setText(TranslationLocalizer.get(TranslationKeys.LICENSE_PURCHASE_DATE));
		grid.getDefaultHeaderRow().getCell(PROPERTY_RENEWAL_DATE)
				.setText(TranslationLocalizer.get(TranslationKeys.LICENSE_RENEWAL_DATE));
		grid.getDefaultHeaderRow().getCell(PROPERTY_EXPIRATION_DATE)
				.setText(TranslationLocalizer.get(TranslationKeys.LICENSE_EXPIRATION_DATE));

		// configure Popup
		popup.setHideOnMouseOut(false);

		// configure search filter
		nameFilter.setInputPrompt(TranslationLocalizer.get(TranslationKeys.SEARCH_NAME));
		nameFilter.setWidth(250, Unit.PIXELS);
		search.setComponentAlignment(searchBtn, Alignment.BOTTOM_RIGHT);

		// Instantiate and edit new License when the button is clicked
		addNewBtn.addClickListener(e -> {
			editor.editLicense(new License("", new GregorianCalendar()));
			popup.setPopupVisible(true);
		});

		// Connect search button
		searchBtn.addClickListener(e -> {
			String licenseName = nameFilter.getValue();
			if (!expirationFromFilter.isEmpty() && !expirationToFilter.isEmpty()) {
				// Search for name, date from, date to
				grid.setContainerDataSource(new BeanItemContainer<License>(License.class,
						repo.findByLicenseNameStartsWithIgnoreCaseAndExpirationDateGreaterThanAndExpirationDateLessThan(
								licenseName, DateUtils.toCalendar(expirationFromFilter.getValue()),
								DateUtils.toCalendar(expirationToFilter.getValue()))));
			} else if (!expirationFromFilter.isEmpty() && expirationToFilter.isEmpty()) {
				// Search for name, date from
				grid.setContainerDataSource(new BeanItemContainer<License>(License.class,
						repo.findByLicenseNameStartsWithIgnoreCaseAndExpirationDateGreaterThan(licenseName,
								DateUtils.toCalendar(expirationFromFilter.getValue()))));
			} else if (expirationFromFilter.isEmpty() && !expirationToFilter.isEmpty()) {
				// Search for name, date to
				grid.setContainerDataSource(new BeanItemContainer<License>(License.class,
						repo.findByLicenseNameStartsWithIgnoreCaseAndExpirationDateLessThan(licenseName,
								DateUtils.toCalendar(expirationToFilter.getValue()))));
			} else {
				// Search for name
				listLicenses(nameFilter.getValue());
			}
		});

		// Connect selected License to editor or hide if none is selected
		editBtn.addClickListener(e -> {
			License l = (License) grid.getSelectedRow();
			if (l != null) {
				editor.editLicense(l);
				popup.setPopupVisible(true);
			}
		});

		// Delete selected license
		deleteBtn.addClickListener(e -> {
			License l = (License) grid.getSelectedRow();
			if (l != null) {
				repo.delete(l);
			}
			listLicenses();
		});

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			popup.setPopupVisible(false);
			listLicenses(nameFilter.getValue());
		});

		// Initialize listing
		listLicenses(null);

		// Set converter for the date representation
		grid.getColumn(PROPERTY_PURCHASE_DATE).setConverter(new StringToCalendarConverter());
		grid.getColumn(PROPERTY_RENEWAL_DATE).setConverter(new StringToCalendarConverter());
		grid.getColumn(PROPERTY_EXPIRATION_DATE).setConverter(new StringToCalendarConverter());
	}

	// TODO: Use lazy loading for large number of licenses
	private void listLicenses() {
		grid.setContainerDataSource(new BeanItemContainer<License>(License.class, repo.findAll()));
	}

	private void listLicenses(String licenseName) {
		if (StringUtils.isEmpty(licenseName)) {
			listLicenses();
		} else {
			grid.setContainerDataSource(new BeanItemContainer<License>(License.class,
					repo.findByLicenseNameContainsIgnoreCase(licenseName)));
		}
	}
}
