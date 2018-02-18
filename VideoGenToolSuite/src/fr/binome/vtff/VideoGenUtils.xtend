package fr.binome.vtff

import org.xtext.example.mydsl.videoGen.OptionalVideoSeq
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq

class VideoGenUtils {
	
	int alternativesNumber = 0
	int optionalNumber = 0
	
	def int nbVariantes(VideoGeneratorModel model) {

		val videos = model.medias
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
	
	def int countVideoTotalNumber(VideoGeneratorModel model){
		var counter = 0
		for (video : model.medias){
			if(video instanceof MandatoryVideoSeq){
				counter++
			}
			else if(video instanceof OptionalVideoSeq){
				counter++
			}
			else if(video instanceof AlternativeVideoSeq){
				counter = counter+video.videodescs.size
				alternativesNumber = video.videodescs.size
			}
		}
		return counter;
	}
	
	def int getTotalAlternativesNumber(){
		return alternativesNumber
	}
	
	def int getTotalOptionalNumber(){
		return optionalNumber
	}
}