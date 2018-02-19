import fr.huitel.monfort.VideoGenUtils
import fr.huitel.monfort.VideoGeneratorHelper
import java.io.FileWriter
import java.io.Writer
import java.util.ArrayList
import java.util.Random
import org.eclipse.emf.common.util.URI
import org.junit.Test
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel

import static org.junit.Assert.*
import org.xtext.example.mydsl.videoGen.VideoDescription
import java.util.LinkedList
import java.io.File
import java.util.List
import java.util.HashMap
import fr.huitel.monfort.VideoGenFiltersAndText

class VideoGenTest1 {
	//Stocke les videos ajout�es selon leur path avec un index i comme cl�
	static HashMap<Integer,String> videoIdAndPath = new HashMap()
	
	@Test
	/**
	 * Pr�requis : avoir g�n�r� les fichiers CSV de la grammaire � tester au pr�alable
	 * TP4 : Q3
	 */
	def void testVerifNbVariantes() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example3.videogen"))
		assertEquals(new VideoGenUtils().nbVariantes(videoGen),VideoGeneratorHelper.getLineNumber("videosListExample3.csv"))
	}

	@Test
	/**
	 * Questions 1 et 2 TP2
	 */
	def void testLoadModel() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example7.videogen"))
		assertNotNull(videoGen)
		//println(videoGen.information.authorName)
		// ecriture d'un fichier de playlists
	 	val writer = new FileWriter("playlistOfVideos.txt")
		videoGen.medias.forEach [ video |
			if (video instanceof MandatoryVideoSeq) {
				println('Mandatory video list adding... ' + video.description.videoid)
				writer.write("file '"+video.description.location+"'\n")
			}
			
			if(video instanceof OptionalVideoSeq) {
				println('Optional list adding... ' + video.description.videoid)
				//50 % de chance
				if (new Random().nextInt(2) == 0) {
				// percentage d'optionel ON
				writer.write("file '"+video.description.location+"'\n")
				}
			}
			if(video instanceof AlternativeVideoSeq) {
				//Prise d'une valeur al�atoire sur le script de test
				var randomizedValue = new Random().nextInt(video.videodescs.size())
				println('Alternative list adding... ' + video.videodescs.get(randomizedValue).videoid)
				// On randomise la vid�o alternative � lire
				writer.write("file '"+video.videodescs.get(randomizedValue).location+"'\n")
			}
				// G�n�ration du fichier de la playlist en cours
		]
				//Finalise la g�n�ration de fichier
				writer.close
				//lancement de la concat�nation des fichiers		
					
				var p = Runtime.runtime.exec("ffmpeg -y -f concat -safe 0 -i playlistOfVideos.txt -c copy finalMp4.mp4")
				p.waitFor
				//lecture du fichier g�n�r� en sortie
			    Runtime.runtime.exec("ffplay -autoexit finalMp4.mp4")
			    
			    //test de creation de Gif
			    generateGif("finalMp4.mp4")
			    //test de creation d'image
			    generateImage("finalMp4.mp4")
	}
	
	def void generateImage(String filename) {
		var cmd =  Runtime.runtime.exec("ffmpeg -i "+filename+" -ss 00:00:02.000 -vframes 1 thumbnails/" + filename + "1.png")
		cmd.waitFor
	}
	
	@Test
	/**
	 * TP4 Q7
	 */
	def void testFiltersAndProbability(){
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertNotNull(videoGen)
		val writer = new FileWriter("playlistOfVideosFiltered.txt")
		var probability =""
	 	//probabilit� al�atoire pour pouvoir lancer une probabilit�
	 	val random = new Random
	 	val randomNumber = random.nextDouble()
		videoGen.medias.forEach [ video |
			if (video instanceof MandatoryVideoSeq) {
				println('Mandatory video list adding... ' + video.description.videoid)
				writer.write("file '"+video.description.location.replace(".mp4","")+"filtered.mp4"+"'\n")
				new VideoGenFiltersAndText().createrFilterCommand(video.description)
			}
			else if(video instanceof OptionalVideoSeq) {
				if(video.description.probability != ""){
					if(randomNumber <= video.description.probability as double/100){
						println('Optional video list adding... ' + video.description.videoid)
						writer.write("file '"+video.description.location.replace(".mp4","")+"filtered.mp4"+"'\n")
						new VideoGenFiltersAndText().createrFilterCommand(video.description)
					}
				}
			}
			else if(video instanceof AlternativeVideoSeq) {
				for (videodesc : video.videodescs){
				//TODO : g�rer al�atoirement les vid�os le pourcentage de videos alternatives
				writer.write("file '"+videodesc.location.replace(".mp4","")+"filtered.mp4"+"' \n")
				new VideoGenFiltersAndText().createrFilterCommand(videodesc)
				}
				
			}
		]
		writer.close()
		//TODO : Probl�me r�current de lancement sous windows
		var p =Runtime.runtime.exec("ffmpeg -y -f concat -safe 0 -i playlistOfVideosFiltered.txt -c copy filteredMp4.mp4")
		p.waitFor
		
		//lecture du fichier g�n�r� en sortie
		Runtime.runtime.exec("ffplay -autoexit filteredMp4.mp4")
	}
	
	
	 /**
     * Question 3
     */
    def void generateGif(String location){
		var cmd = Runtime.runtime.exec("ffmpeg -i "+location+" -ss 00:00:00.000 -pix_fmt rgb24 -r 10 -s 800x600 -t 00:00:05.000 "+"gif/"+location.replace(".mp4","").replace("/","")+".gif")
        cmd.waitFor()
    }
    
    
	@Test
	def void testCreateCSV(){
		//for(var id = 1 ; id<=11;id++){
		//g�n�re le csv de l'exemple voulu
			generateCsv(6)
		//}
	}
	
	
	/**
	 * Question 1 et 2 TP3
	 * G�n�rateur de toute les listes de playlist
	 */
	def static listOfPlaylistVideos(VideoGeneratorModel videoGen) {
        var listOfPlaylist = new ArrayList<ArrayList<VideoDescription>>
        listOfPlaylist.add(new ArrayList<VideoDescription>())
        for (video : videoGen.medias) {
            if (video instanceof MandatoryVideoSeq) {
                for (list : listOfPlaylist) {
                    list.add(video.description)
                }
            } else if (video instanceof OptionalVideoSeq) {
                var listTmp = new ArrayList<ArrayList<VideoDescription>>
                for (list : listOfPlaylist) {
                    var tmp = list.clone as ArrayList<VideoDescription>
                    listTmp.add(list)
                    tmp.add(video.description)
                    listTmp.add(tmp)
                }
                listOfPlaylist = listTmp
            } else if (video instanceof AlternativeVideoSeq) {
                var listTmp = new ArrayList<ArrayList<VideoDescription>>
                for (vids : video.videodescs) {
                    for (list : listOfPlaylist) {
                        var tmp = list.clone as ArrayList<VideoDescription>
                        tmp.add(vids)
                        listTmp.add(tmp)
                    }
                }
                listOfPlaylist = listTmp
            }
        }
        return listOfPlaylist
    }
	
	
	/**
	 * G�n�re les fichiers CSV avec l'id des fichiers example.videogen lus
	 * @param int :id
	 */
	def static void generateCsv(int id) {
        var String content = "";
        val videoGenHelper = new VideoGenHelper()
        var videogen = videoGenHelper.loadVideoGenerator(URI.createURI("example"+id+".videogen"))
        assertNotNull(videogen)
        print("example"+id+".videogen \n")
        var listVariantVid = new LinkedList<LinkedList<VideoDescription>>
        content += "id;"
        val videos = videogen
        var List<String> nameColumns = new LinkedList<String>()
        //Id de video pour la hashmap globale
        var tempId = 1
        for (video : videos.medias) {
            if (video instanceof MandatoryVideoSeq) {
                nameColumns.add(video.description.videoid)
                print("location"+video.description.location)
                //ajout des videos dans la hashmap globale
                videoIdAndPath.put(tempId++,video.description.location)
                content += video.description.videoid + ";"
            } else if (video instanceof OptionalVideoSeq) {
                nameColumns.add(video.description.videoid)
                print("location"+video.description.location)
                content += video.description.videoid + ";"
                //ajout � la hashmap globale pour obtenir un index et le path
               	videoIdAndPath.put(tempId++,video.description.location)
            } else if (video instanceof AlternativeVideoSeq) {
                for (videodesc : video.videodescs){
                //ajout � la hashmap globale pour obtenir un index et le path
              	videoIdAndPath.put(tempId++,videodesc.location)
                nameColumns.add(videodesc.videoid)
                content += videodesc.videoid + ";"
                }
            }
        }
        // R�cup�ration de la taille de chaque fichiers
        for (listVariantVideos : listOfPlaylistVideos(videogen)) {
            var temp = new LinkedList<VideoDescription>
            for (var int i = 0; i < listVariantVideos.size(); i++) {
                var VideoDescription currentVideo = listVariantVideos.get(i)
                temp.add(currentVideo)
            }
            listVariantVid.add(temp)
        }
        
        content += "size;"
        content += "realSize\n"
       
       // ajouter une taille � chaque videos
        var int ii = 1
        for (listVariantVideos : listVariantVid) {
            var int size = 0
            content += (ii) + ";"
            var List<String> listName = new ArrayList<String>()
            for (var int i = 0; i < listVariantVideos.size(); i++) {
                val File currentFile = new File(listVariantVideos.get(i).location)
                val currentFileSize = currentFile.length() as int
                size += currentFileSize
                print(listVariantVideos.get(i).videoid+"\n")
                listName.add(listVariantVideos.get(i).videoid)
            }
            
            // Pour chaque colonne de noms on met � true ou false selon le cas
            for (name : nameColumns) {
                if (listName.contains(name)) {
                    content  += "TRUE;"
                } else {
                    content += "FALSE;"
                }
            }
            content += size + "\n"
            print(content)
            ii++
        }
        var filename = "videosListExample"+id+".csv"
        val file = new FileWriter(filename)
        file.write(content)
        file.close() 
        /*Cr�e un CSV avec un realSize*/
        VideoGeneratorHelper.getRealSize(filename,videoIdAndPath)
        println("CSV contenu \n" + content)
    }
    
    
        
}

