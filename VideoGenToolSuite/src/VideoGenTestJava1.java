import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class VideoGenTestJava1 {
	
	private List<List<String>> addVideoIdToAllConfigurations(String id, List<List<String>> allConfs) {
		
		if (allConfs.isEmpty()) {
			List<String> nPartialConf = new ArrayList<String>();
			nPartialConf.add(id);
			allConfs.add(nPartialConf);
		}
		else {	
			System.out.println("" + allConfs);
			for (List<String> partialConf : allConfs) {
				partialConf.add(id); 
			}	
		}
		List<List<String>> nAllConfs = new ArrayList<List<String>>(allConfs);
		return  nAllConfs;
	}

}