package fr.huitel.monfort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoGeneratorHelper {
	
	/**
	 * Création du fichier CSV avec le realSize des fichiers videos concaténnés
	 * @param csvFile : fichier csv de base en entrée
	 * @param videoIdAndSize : hashmap de videos avec leur path
	 * @throws FileNotFoundException
	 */
	public static void getRealSize(String csvFile,HashMap<Integer,String> videoIdAndSize) throws FileNotFoundException {
		String firstline = "";
		String line = "";
        final String cvsSplitBy = ";";
        String result = "";
        //Nom de fichier CSV à réécrire
        String rewritedFilename ="";
        //Tableau pour accueillir les valeurs des résultats de realSize pour les ajouter par lignes par la fin
        List<String> resultRealSizeTab = new ArrayList<String>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	//ligne de playlist à lire
        	int playlistLine = 0;
        	//récupère le nombre de lignes dans le fichier
        	BufferedReader brLine = new BufferedReader(new FileReader(csvFile));
        	LineNumberReader lnr = new LineNumberReader(brLine);
        	int lineNumber = 0;
            while (lnr.readLine() != null){
            	lineNumber++;
            }
            //Enlève la première ligne dans le calcul du nombre de lignes
            lnr.close();
            //passe la première ligne du fichier
        	br.readLine();
            while ((line = br.readLine()) != null) {
                String[] trueFalse = line.split(cvsSplitBy);
                
                //crée un fichier de playlist
                FileWriter writer = new FileWriter("playlistOfVideos"+playlistLine+".txt");
                // passe la colonne des id dans le fichier
                int i = 1;
                while(i<=lineNumber) {
	                // true False de chaque ligne
	                if(trueFalse[i].equals("TRUE")) {
	                	//Récupère le numéro d'ID de la video
	                	videoIdAndSize.get(i);
	                	//prend le path dans la playlist à générer pour concaténer la video
	                	writer.write("file '"+videoIdAndSize.get(i)+"'\n");
	                }
	                i++;
                }
                //Finalise la génération de fichier
				writer.close();
				//lancement de la concaténation des fichiers
				Process p = Runtime.getRuntime().exec("ffmpeg -y -f concat -safe 0 -i playlistOfVideos"+playlistLine+".txt -c copy finalMp4"+playlistLine+".mp4");
				try {
					p.waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                //récupère la valeur réelle de la taille du fichier mp4 généré
                File currentFile = new File("finalMp4"+playlistLine+".mp4");
                File ffmepgListOfFile = new File("playlistOfVideos"+playlistLine+".txt");
                playlistLine++;
                long currentFileSize = currentFile.length();
                result= ";"+currentFileSize;
                //ajouter dans la liste le résultat de realSize obtenu
                resultRealSizeTab.add(result);
                //Suppression du fichier généré pour ne pas créer de problème si on relance la méthode
                currentFile.delete();
                ffmepgListOfFile.delete();
                rewritedFilename = csvFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //réécrit dans le fichier csv
        FileReader fileCsv = new FileReader(rewritedFilename);
        try (BufferedReader brCsv = new BufferedReader(new FileReader(csvFile))) {
        	//crée un nouveau fichier rewrited pour ne pas confondre les deux ficheirs csv
        	 FileWriter rewriteCsv = new FileWriter(rewritedFilename.replace(".csv","")+"realSize.csv");
        	//passe la première ligne du fichier mais la récupère
        	firstline = brCsv.readLine();        	
        	int nbOnResultList = 0;
        	//réécrit la première ligne
        	rewriteCsv.write(firstline+"\n");
            while ((line = brCsv.readLine()) != null) {
            	//écrit le résult obtenur dans le CSV
            	rewriteCsv.write(line+resultRealSizeTab.get(nbOnResultList)+"\n");
            	nbOnResultList++;
            }
            rewriteCsv.close();
        }
        catch(IOException e) {
        	e.printStackTrace();
    	}
    }
	
	public static int getLineNumber(String csvFile) {
		int lineNumber = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	//récupère le nombre de lignes dans le fichier
        	BufferedReader brLine = new BufferedReader(new FileReader(csvFile));
        	LineNumberReader lnr = new LineNumberReader(brLine);
        	//passer la première ligne d'entête du fichier csv
        	lnr.readLine();
            while (lnr.readLine() != null){
            	lineNumber++;
            }
            //Enlève la première ligne dans le calcul du nombre de lignes
            lnr.close();
		}
		catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println(lineNumber);
		return lineNumber;
	}
}
