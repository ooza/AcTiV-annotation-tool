
package activ;

/*
 * Compare two files depending on their last modifications date.
*/

import java.util.Comparator;
import java.io.File;
 
public class FileComparator implements Comparator<File>
{
	public int compare(File f1,File f2)
	{
		return (int)(f1.lastModified()-f2.lastModified());
	}
}