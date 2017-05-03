package com.msc.jkeel;

/**
 * Used, in case someone tries to access a non-existent language.
 *
 * @author Marcel
 * @since 01.05.2017
 */
public class LanguageNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = -2264163458108984188L;

	public LanguageNotFoundException(final String message)
	{
		super(message);
	}
}
