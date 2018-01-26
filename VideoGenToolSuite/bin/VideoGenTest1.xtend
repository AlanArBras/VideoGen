import fr.binome.vtff.Video
import fr.binome.vtff.VideoList
import org.eclipse.emf.common.util.URI
import org.junit.Test
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel

import static org.junit.Assert.*

class VideoGenTest1 {

	def int nbVariantes(VideoGeneratorModel model) {

		val videos = model.videoseqs
		var variantes = 1;
		if (videos.size == 0) {
			variantes = 0;
		} else {
			for (video : videos) {
				if (video instanceof OptionalVideoSeq) {
					variantes *= 2;
				} else if (video instanceof AlternativeVideoSeq) {
					variantes *= video.videodescs.size
				}
			}
		}
		return variantes;
	}

	@Test
	def void testVerifNbVariantes() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertEquals(nbVariantes(videoGen), listPlayList.size)
	}

	@Test
	def void testLoadModel() {

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertNotNull(videoGen)
		println(videoGen.information.authorName)
		// and then visit the model
		// eg access video sequences: videoGen.videoseqs
		videoGen.videoseqs.forEach [ video |
			if (video instanceof MandatoryVideoSeq) {
				val desc = video.description
				println('Playing... ' + desc.videoid)
				var p = Runtime.runtime.exec("/usr/bin/vlc --play-and-exit " + desc.location + "")
				p.waitFor

			}
		]
	}

	@Test
	def void testCreateCSV() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertNotNull(videoGen)
		println(videoGen.information.authorName)
		val videoList = new VideoList();
		for (video : videoGen.videoseqs) {
			if (video instanceof MandatoryVideoSeq) {
				videoList.add(new Video(video.description.videoid, video.description.location));
			} else if (video instanceof AlternativeVideoSeq) {
				
			} else if (video instanceof OptionalVideoSeq) {
			}
		}

	}
}
