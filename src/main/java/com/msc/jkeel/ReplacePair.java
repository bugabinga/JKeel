package com.msc.jkeel;

public class ReplacePair
{
	private final String	tag;
	private final String	replacement;

	/**
	 * Initializes the {@link ReplacePair} Object.
	 *
	 * @param tag
	 *            the tag
	 * @param replacement
	 *            the replacement for the tag
	 */
	public ReplacePair(final String tag, final String replacement)
	{
		this.tag = tag;
		this.replacement = replacement;
	}

	/**
	 * @return the {@link #tag} to replace
	 */
	public String getTag()
	{
		return tag;
	}

	/**
	 * @return the {@link #replacement} for the tag
	 */
	public String getReplacement()
	{
		return replacement;
	}
}
