
package activ;

/*
 * Lists files and sorts inside a folder
*/

import java.io.File;
import java.util.ArrayList;

public class DirectoryReader {
	static File[] listFiles(String directoryPath){ 
		File[] files = null; 
		File directoryToScan = new File(directoryPath);
		ArrayList<File> liste=new ArrayList<File>();
		files = directoryToScan.listFiles(); 
		for(File f:files){
			liste.add(f);
		}
		java.util.Collections.sort(liste,new FileComparator());
		int cpt=0;
		for(File f:liste){
			//System.out.println("* "+f.getPath());
			files[cpt]=f;
			cpt++;
		}
		
		return files; 
	}
}
