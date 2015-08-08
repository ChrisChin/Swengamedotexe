package game.dataStorage;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * This is a file filter, filter out files that don't have a
 * extension name of .xml
 * 
 * @author Jinpeng Song (songjinp)
 */
public class XMLFileFilter extends FileFilter{
	private String extensionName = ".xml";
	private String typeDescription = "Extensible Markup Language";
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		}
		return f.getName().endsWith(extensionName);
	}

	@Override
	public String getDescription() {
		return typeDescription;
	}

}
