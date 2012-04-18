import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class FileFixer {
	public static void main(String input[]) throws IOException {
		String srcBase = "C:\\Documents and Settings\\jenfield\\Desktop\\src\\Part 1";
		String destBase = "C:\\Documents and Settings\\jenfield\\Desktop\\src\\Part ";
		String end = ".mp3";

		String[][] transfer = new String[243 - 221 + 1][2];
		int first = 221;
		int last = 243;
		for (int i = first; i <= last; i++) {

			int pos = -(first - i);
			int j = last-pos;
			transfer[pos][0] = srcBase + i + end;
			transfer[pos][1] = destBase + i + end;

			File source = new File(transfer[pos][0]);
			File dest = new File(transfer[pos][1]);

			if (!dest.exists()) {

				dest.createNewFile();

			}

			InputStream in = null;

			OutputStream out = null;

			try {

				in = new BufferedInputStream(new FileInputStream(source));

				out = new BufferedOutputStream(new FileOutputStream(dest));

				byte[] buf = new byte[1024];

				int len;

				while ((len = in.read(buf)) > 0) {

					out.write(buf, 0, len);

				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}

			finally {

				in.close();

				out.close();
			}

			System.out.println("\t" + transfer[pos][0] + "\n\t"
					+ transfer[pos][1]);
		}
	}
}
