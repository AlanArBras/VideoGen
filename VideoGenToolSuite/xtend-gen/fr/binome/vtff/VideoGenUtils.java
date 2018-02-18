package fr.binome.vtff;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

@SuppressWarnings("all")
public class VideoGenUtils {
  private int alternativesNumber = 0;
  
  private int optionalNumber = 0;
  
  public int nbVariantes(final VideoGeneratorModel model) {
    final EList<Media> videos = model.getMedias();
    int variantes = 1;
    int _size = videos.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      variantes = 0;
    } else {
      for (final Media video : videos) {
        if ((video instanceof OptionalVideoSeq)) {
          int _variantes = variantes;
          variantes = (_variantes * 2);
        } else {
          if ((video instanceof AlternativeVideoSeq)) {
            int _variantes_1 = variantes;
            int _size_1 = ((AlternativeVideoSeq)video).getVideodescs().size();
            variantes = (_variantes_1 * _size_1);
          }
        }
      }
    }
    return variantes;
  }
  
  public int countVideoTotalNumber(final VideoGeneratorModel model) {
    int counter = 0;
    EList<Media> _medias = model.getMedias();
    for (final Media video : _medias) {
      if ((video instanceof MandatoryVideoSeq)) {
        counter++;
      } else {
        if ((video instanceof OptionalVideoSeq)) {
          counter++;
        } else {
          if ((video instanceof AlternativeVideoSeq)) {
            int _size = ((AlternativeVideoSeq)video).getVideodescs().size();
            int _plus = (counter + _size);
            counter = _plus;
            this.alternativesNumber = ((AlternativeVideoSeq)video).getVideodescs().size();
          }
        }
      }
    }
    return counter;
  }
  
  public int getTotalAlternativesNumber() {
    return this.alternativesNumber;
  }
  
  public int getTotalOptionalNumber() {
    return this.optionalNumber;
  }
}
