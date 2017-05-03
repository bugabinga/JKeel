package com.msc.jkeel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class JKeel
{
	private static JKeel globalInstance = new JKeel();

	/**
	 * Returns JKeel's {@link #globalInstance global instance}.
	 */
	public static JKeel global()
	{
		return globalInstance;
	}

	/**
	 * Map that holds the properties for each language.
	 */
	private final Map<String, Properties>	data			= new HashMap<>();
	/**
	 * Language that is supposed to be used as the default.
	 */
	private String							defaultLanguage	= null;

	/**
	 * Loads a language and adds it as a default if wanted.
	 *
	 * @param localizationFile
	 *            the file to load the language from
	 * @param language
	 *            name(key) of the language
	 * @param asDefaultLanguage
	 *            if the language should be the default language
	 * @throws FileNotFoundException
	 *             if the language file wasn't found
	 * @throws IOException
	 *             if the language file couldn't be loaded
	 */
	private void loadLanguage(final File localizationFile, final String language, final boolean asDefaultLanguage)
			throws FileNotFoundException, IOException
	{
		final Properties languageProperties = new Properties();
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(localizationFile), StandardCharsets.UTF_8))
		{
			languageProperties.load(reader);
			data.put(language, languageProperties);

			if (asDefaultLanguage)
			{
				defaultLanguage = language;
			}
		}
	}

	/**
	 * Deletes all loaded languages and loads the given one as the default.
	 *
	 * @param localizationFile
	 *            the file to load the language from
	 * @param language
	 *            name(key) of the language
	 * @throws FileNotFoundException
	 *             if the language file wasn't found
	 * @throws IOException
	 *             if the language file couldn't be loaded
	 */
	public void setLanguage(final File localizationFile, final String language)
			throws FileNotFoundException, IOException
	{
		data.clear();
		loadLanguage(localizationFile, language, true);
	}

	/**
	 * Adds a language.
	 *
	 * @param localizationFile
	 *            the file to load the language from
	 * @param language
	 *            name(key) of the language
	 * @param asDefaultLanguage
	 *            use this as the default language
	 * @throws FileNotFoundException
	 *             if the language file wasn't found
	 * @throws IOException
	 *             if the language file couldn't be loaded
	 */
	public void addLanguage(final File localizationFile, final String language, final boolean asDefaultLanguage)
			throws FileNotFoundException, IOException
	{
		loadLanguage(localizationFile, language, asDefaultLanguage);
	}

	/**
	 * Deletes a language if it's not the default language.
	 *
	 * @param language
	 *            to delete
	 * @return true if it has been deleted, otherwise false (was default)
	 */
	public boolean removeLanguage(final String language)
	{
		if (language.equals(defaultLanguage))
		{
			return false;
		}
		data.remove(language);
		return true;
	}

	/**
	 * Sets a language as a defualt, only works if that language is already loaded.
	 *
	 * @param language
	 *            to set as default
	 * @return true if the language was set, otherwise false (wasn't loaded)
	 */
	public void setDefaultLanguage(final String language)
	{
		checkLanguage(language);
		defaultLanguage = language;
	}

	/**
	 * Returns the text that has been set and replaces one after another.
	 *
	 * @param key
	 *            the key of the text
	 * @return
	 */
	public String getText(final String key)
	{
		return getTextOfLanguage(defaultLanguage, key, new String[] {});
	}

	/**
	 * Returns the text that has been set and replaces one after another.
	 *
	 * @param key
	 *            the key of the text
	 * @param replacements
	 *            replacements for the tags
	 * @return
	 */
	public String getText(final String key, final String... replacements)
	{
		return getTextOfLanguage(defaultLanguage, key, replacements);
	}

	/**
	 * Returns the text that has been set and replaces one after another.
	 *
	 * @param language
	 *            the language to retrieve the text from
	 * @param key
	 *            the key of the text
	 * @param replacements
	 *            replacements for the tags
	 * @return
	 */
	public String getTextOfLanguage(final String language, final String key, final String... replacements)
	{
		checkLanguage(language);
		String string = data.get(language).getProperty(key);
		if (Objects.nonNull(string))
		{
			for (final String replacement : replacements)
			{
				string = string.replaceFirst("(\\(\\[.*?\\]\\))", replacement);
			}
		}
		else if (!isDefaultLanguage(language))
		{
			return getTextOfLanguage(language, key, replacements);
		}
		return string;

	}

	/**
	 * Returns the text that has been set and replaces tags by their name.
	 *
	 * @param language
	 *            the language to retrieve the text from
	 * @param key
	 *            the key of the text
	 * @param tagAndReplacement
	 *            tags that should be replaced including their replacementtext
	 * @return the text with replaced tags
	 */
	public String getText(final String key, final ReplacePair... tagAndReplacement)
	{
		return getTextOfLanguage(defaultLanguage, key, tagAndReplacement);
	}

	/**
	 * Returns the text that has been set and replaces tags by their name.
	 *
	 * @param key
	 *            the key of the text
	 * @param tagAndReplacement
	 *            tags that should be replaced including their replacementtext
	 * @return the text with replaced tags
	 */
	public String getTextOfLanguage(final String language, final String key, final ReplacePair... tagAndReplacement)
	{
		checkLanguage(language);
		String string = data.get(language).getProperty(key);
		if (Objects.nonNull(string))
		{
			for (final ReplacePair replacementPair : tagAndReplacement)
			{
				string = string.replaceAll("(?i)(\\(\\[" + replacementPair.getTag() + "?\\]\\))", replacementPair.getReplacement());
			}
		}
		else if (!isDefaultLanguage(language))
		{
			return getTextOfLanguage(language, key, tagAndReplacement);
		}
		return string;
	}

	/**
	 * @return {@link #defaultLanguage}
	 */
	public String getDefaultLanguage()
	{
		return defaultLanguage;
	}

	/**
	 * Checks whether a language is the default one.
	 *
	 * @param language
	 *            the language to check
	 * @return true if it is, false otherwise
	 */
	public boolean isDefaultLanguage(final String language)
	{
		if (Objects.isNull(language) || Objects.isNull(defaultLanguage))
		{
			return false;
		}
		return language.equalsIgnoreCase(defaultLanguage);
	}

	/**
	 * Checks if a language is loaded.
	 *
	 * @param language
	 *            the language to check for
	 * @throws LanguageNotFoundException
	 *             if the language isn't loaded
	 */
	private void checkLanguage(final String language)
	{
		if (Objects.isNull(data.get(language)))
		{
			throw new LanguageNotFoundException("Language '" + language + "' couldn't be found.");
		}
	}

	/**
	 * Checks whether a language is loaded.
	 *
	 * @param language
	 *            the language to check for
	 * @return true if it is loaded, false otherwise.
	 */
	public boolean isLanguageLoaded(final String language)
	{
		try
		{
			checkLanguage(language);
			return true;
		}
		catch (final LanguageNotFoundException e)
		{
			return false;
		}
	}
}
