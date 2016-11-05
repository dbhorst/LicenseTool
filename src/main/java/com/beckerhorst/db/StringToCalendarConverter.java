package com.beckerhorst.db;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * A converter implementation to convert {@link String} to {@link Calendar} and
 * backwards.
 * 
 * @author dirk
 */
public class StringToCalendarConverter implements Converter<String, Calendar> {

	private static final long serialVersionUID = 1L;

	/**
	 * Returns the date format.
	 * 
	 * @param locale
	 *            The locale to use
	 * @return A DateFormat instance
	 */
	protected DateFormat getFormat(Locale locale) {
		DateFormat f = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		f.setLenient(false);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.lang.Class, java.util.Locale)
	 */
	@Override
	public Calendar convertToModel(String value, Class<? extends Calendar> targetType, Locale locale)
			throws ConversionException {
		if (targetType != getModelType()) {
			throw new ConversionException("Converter only supports " + getModelType().getName() + " (targetType was "
					+ targetType.getName() + ")");
		}

		if (value == null) {
			return null;
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		// Remove leading and trailing white space
		value = value.trim();

		ParsePosition parsePosition = new ParsePosition(0);
		Calendar parsedValue = new GregorianCalendar(locale);
		parsedValue.setTime(getFormat(locale).parse(value, parsePosition));
		if (parsePosition.getIndex() != value.length()) {
			throw new ConversionException("Could not convert '" + value + "' to " + getModelType().getName());
		}

		return parsedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang.
	 * Object, java.lang.Class, java.util.Locale)
	 */
	@Override
	public String convertToPresentation(Calendar value, Class<? extends String> targetType, Locale locale)
			throws ConversionException {

		if (value == null) {
			return null;
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		return getFormat(locale).format(value.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<Calendar> getModelType() {
		return Calendar.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getPresentationType()
	 */
	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
