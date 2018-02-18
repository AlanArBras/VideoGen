package fr.binome.vtff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class VideoGeneratorHelper {
	
	public String prepareCsvLines(int id, Playlist playlist, int numberOfVideos) {
		String content = "";
		long playlistSize = 0;
		int tmpVideoNumberVariant = 0;
		content += id + ";";
		while (tmpVideoNumberVariant < numberOfVideos) {
		//	System.out.println(playlist.get(tmpVideoNumberVariant).getPath());
	//		File file = new File(playlist.get(tmpVideoNumberVariant).getPath());
			
			if (playlist.get(tmpVideoNumberVariant).isPresence()) {
				content += "TRUE;";
				//TODO : Set Size plutot?
				long fileSize = playlist.get(tmpVideoNumberVariant).getSize();
				playlistSize += fileSize;
				System.out.println(playlistSize + " "+ playlist.get(tmpVideoNumberVariant).getName() );
			} else if (!playlist.get(tmpVideoNumberVariant).isPresence()) {
				content += "FALSE;";
			}
			tmpVideoNumberVariant++;
		}
		content += playlistSize+"\n";
		return content;
	}

	public String prepareHeaderCsv(Playlist playlist, int numberOfVideos) {
		String header = "";
		header += "id;";

		List<String> columnsNames = new LinkedList<String>();
		for (int i = 0; i < playlist.sizeList(); i++) {
			String headerNames = playlist.get(i).getName().toString();
			String pathNames = playlist.get(i).getPath().toString();

			if (!columnsNames.contains(headerNames)) {
				columnsNames.add(headerNames);
			}
		}
		// ajout des noms des vidéos dans les headers du CSV
		for (String namesOfVideos : columnsNames) {
			header += namesOfVideos + ";";
		}
		// ajout de la colonne size
		header += "size\n";
		return header;
	}

	public boolean createCSV(String header,String content) {
		try {
			if(!content.isEmpty() && content != null && !header.isEmpty() && header != null)  {
			FileWriter writer = new FileWriter("listOfVariants.csv");
			writer.write(header+content);
			writer.close();
			System.out.println(header+content);
			}
			
		}catch(

	IOException e)
	{
			System.out.println("impossible de générer le fichier CSV listOfVariants.csv");
		}return true;
}

}
