package helpers;

import org.pegdown.PegDownProcessor;

public class MarkdownHelper {
	public static String toHtml(String markdown) {
		PegDownProcessor p = new PegDownProcessor();
		return p.markdownToHtml(markdown);
	}
}