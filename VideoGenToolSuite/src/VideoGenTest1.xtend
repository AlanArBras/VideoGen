import fr.binome.vtff.TypeOfVideos
import fr.binome.vtff.Video
import fr.binome.vtff.VideoGenUtils
import fr.binome.vtff.VideoGeneratorHelper
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
import fr.binome.vtff.Playlist
import org.xtext.example.mydsl.videoGen.VideoDescription
import java.util.LinkedList
import java.io.File
import java.util.List
import fr.binome.vtff.Utils

class VideoGenTest1 {

	
	
	@Test
	def void testVerifNbVariantes() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example2.videogen"))
	//	assertEquals(nbVariantes(videoGen), listPlayList.size)
	}

	@Test
	/**
	 * Questions 1 et 2 TP2
	 */
	def void testLoadModel() {

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example7.videogen"))
		assertNotNull(videoGen)
		println(videoGen.information.authorName)
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
				//Prise d'une valeur aléatoire sur le script de test
				var randomizedValue = new Random().nextInt(video.videodescs.size())
				println('Alternative list adding... ' + video.videodescs.get(randomizedValue).videoid)
				// On randomise la vidéo alternative à lire
				writer.write("file '"+video.videodescs.get(randomizedValue).location+"'\n")
			}
				// Génération du fichier de la playlist en cours
		]
				//Finalise la génération de fichier
				writer.close
				//lancement de la concaténation des fichiers				
				var p = Runtime.runtime.exec("ffmpeg -y -f concat -safe 0 -i playlistOfVideos.txt -c copy finalMp4.mp4")
				p.waitFor
				//lecture du fichier généré en sortie
			    Runtime.runtime.exec("ffplay finalMp4.mp4")
	}
	
	@Test
	def void testCreateCSV(){
		for(var id = 1 ; id<=11;id++){
			generateCsv(id)
		}
	}
	
	
	/**
	 * Question 1 et 2 TP3
	 * Générateur de toute les listes de playlist
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
	 * Génère les fichiers CSV avec l'id des fichiers example.videogen lus
	 * @param int :id
	 */
	def static void generateCsv(int id) {
        var String content = "";
        val videoGenHelper = new VideoGenHelper()
        var videogen = videoGenHelper.loadVideoGenerator(URI.createURI("example"+id+".videogen"))
        assertNotNull(videogen)
        print("example"+id+".videogen \n")
        var listVariantVid = new LinkedList<LinkedList<VideoDescription>>
        
        // ajouter une taille à chaqu
        for (listVariantVideos : listOfPlaylistVideos(videogen)) {
            var temp = new LinkedList<VideoDescription>
            for (var int i = 0; i < listVariantVideos.size(); i++) {
                var VideoDescription currentVideo = listVariantVideos.get(i)
                temp.add(currentVideo)
            }
            listVariantVid.add(temp)
        }
        content += "id;"
        val videos = videogen
        var List<String> nameColumns = new LinkedList<String>()
        
        for (video : videos.medias) {
            if (video instanceof MandatoryVideoSeq) {
                nameColumns.add(video.description.videoid)
                content += video.description.videoid + ";"
            } else if (video instanceof OptionalVideoSeq) {
                nameColumns.add(video.description.videoid)
                content += video.description.videoid + ";"
            } else if (video instanceof AlternativeVideoSeq) {
                for (videodesc : video.videodescs){
                    nameColumns.add(videodesc.videoid)
                    content += videodesc.videoid + ";"
                }
            }
        }
        
        content += "size\n"
        //gestion de l'écriture des cas true/false
        var int ii = 1
        for (listVariantVideos : listVariantVid) {
            var int size = 0;
            content += (ii) + ";"
            var List<String> listName = new ArrayList<String>()
            for (var int i = 0; i < listVariantVideos.size(); i++) {
                val File currentFile = new File(listVariantVideos.get(i).location)
                val currentFileSize = currentFile.length() as int
                size += currentFileSize
                print(listVariantVideos.get(i).videoid+"\n")
                listName.add(listVariantVideos.get(i).videoid)
            }
            
            // Pour chaque colonne de noms on met à true ou false selon le cas
            for (name : nameColumns) {
                if (listName.contains(name)) {
                    content += "TRUE;"
                } else {
                    content += "FALSE;"
                }
            }
            
            content += size + "\n"
            ii++
        }
        val file = new FileWriter("videosListExample"+id+".csv")
        file.write(content)
        file.close()    
        println("CSV contenu \n" + content)
    }
}

