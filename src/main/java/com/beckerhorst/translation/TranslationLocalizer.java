package com.beckerhorst.translation;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Singelton class for the translation of text. 
 * @author dirk
 */
public class TranslationLocalizer {
	
	private static TranslationLocalizer INSTANCE;
    private MessageSource messageSource;
	
	private TranslationLocalizer() {
		messageSource = new ClassPathXmlApplicationContext("translation.xml");
	}
	
	/**
	 * Returns the translation for the given key.
	 * @param key The translation key
	 * @return The translated text for the given key.
	 */
	public static String get(String key) {
		if(null == INSTANCE) {
			INSTANCE = new TranslationLocalizer();
		}
			
		return INSTANCE.translate(key);
	}
	
	private String translate(String key) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, new Object[0], locale);
	}
}
