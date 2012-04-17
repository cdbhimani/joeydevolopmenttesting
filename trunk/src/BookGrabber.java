import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class BookGrabber {

	public static void main(String args[]) throws IOException {


		int[] books = { 101, 102, 103, 104 };
		int[] chapters = { 73, 70, 82, 46 };

		String inputLine = null;
		StringBuilder book = new StringBuilder();
		StringBuilder summary = new StringBuilder();
		StringBuilder page = new StringBuilder();
		for (int b = 0; b < books.length; b++)
		{
			book.setLength(0);
			summary.setLength(0);
			for (int c = 1; c <= chapters[b]; c++)
			{
				System.out.printf("Bood [%d of %d] - Chapter [%d of %d]\n", b+1, books.length,c, chapters[b]);
				page.setLength(0);
				URL url = new URL("http://towerofthehand.com/books/"
						+ String.format("%03d", books[b]) + "/"
						+ String.format("%03d", c) + "/index.html");

				BufferedInputStream input = new BufferedInputStream(url
						.openConnection().getInputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(
						input));

				//Get Page from server
				while ((inputLine = in.readLine()) != null) {
					page.append(inputLine);
				}
				in.close();

				stripPage(page, book, summary);
			}

			{
				PrintWriter rst = new PrintWriter("c:\\test\\book["+ String.format("%03d", books[b]) +"].html");
				rst.println(book.toString());
				rst.close();
			}

			{
				PrintWriter rst = new PrintWriter("c:\\test\\summary["+ String.format("%03d", books[b]) +"].html");
				rst.println(summary.toString());
				rst.close();
			}
		}
	}

	public static void stripPage(StringBuilder page, StringBuilder book,StringBuilder summary)
	{
		//Strip out the heading and summary
		String rst = page.toString();
		String title = (rst.split("<div id=\"headline\">")[1].split("</div>")[0]);
		String smallSummary = (rst.split("<span class=\"teaser\">")[1].split("</span>")[0]);
		String body = (rst.split("<div class=\"entry_text\">")[1].split("</div>")[0]);

		title = Html2Text.decode(title);

		body = removeSUP(body);
		body = removeP(body);
		body = Html2Text.decode((body));
		body = addP(body);

		book.append("<hr/><b>");
		book.append(title);
		book.append("</b><br/>");
		book.append(body);
		book.append("<br/>");

		summary.append(smallSummary);
		summary.append("<br/>");
	}

	public static String removeP(String s)
	{
		return s.replaceAll("<p>", "AAPAA");
	}

	public static String addP(String s)
	{
		return s.replaceAll( "AAPAA","<p>");
	}
	public static String removeSUP(String s)
	{
		String rst = new String(s);
		while(rst.contains("<sup>"))
		{
			String split[] = rst.split("<sup>",2);
			rst = split[0]+split[1].split("</sup>",2)[1];
		}
		return rst;
	}
}

class Html2Text extends HTMLEditorKit.ParserCallback {
	 StringBuffer s;

	 public static String decode(String s)
	 {
		 Html2Text t = new Html2Text();
		 try {
			t.parse(new StringReader(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return t.getText();
	 }
	 public Html2Text() {}

	 public void parse(Reader in) throws IOException {
	   s = new StringBuffer();
	   ParserDelegator delegator = new ParserDelegator();
	   // the third parameter is TRUE to ignore charset directive
	   delegator.parse(in, this, Boolean.TRUE);
	 }

	 public void handleText(char[] text, int pos) {
	   s.append(text);
	 }

	 public String getText() {
	   return s.toString();
	 }

	 public static void main (String[] args) {
	   try {
	     // the HTML to convert
	     FileReader in = new FileReader("java-new.html");
	     Html2Text parser = new Html2Text();
	     parser.parse(in);
	     in.close();
	     System.out.println(parser.getText());
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	   }
	 }
	}