import fr.binome.vtff.Video;
import fr.binome.vtff.VideoList;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

@SuppressWarnings("all")
public class VideoGenTest1 {
  public int nbVariantes(final VideoGeneratorModel model) {
    final EList<VideoSeq> videos = model.getVideoseqs();
    int variantes = 1;
    int _size = videos.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      variantes = 0;
    } else {
      for (final VideoSeq video : videos) {
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
  
  @Test
  public void testVerifNbVariantes() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field listPlayList is undefined"
      + "\nsize cannot be resolved");
  }
  
  @Test
  public void testLoadModel() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    Assert.assertNotNull(videoGen);
    InputOutput.<String>println(videoGen.getInformation().getAuthorName());
    final Consumer<VideoSeq> _function = (VideoSeq video) -> {
      try {
        if ((video instanceof MandatoryVideoSeq)) {
          final VideoDescription desc = ((MandatoryVideoSeq)video).getDescription();
          String _videoid = desc.getVideoid();
          String _plus = ("Playing... " + _videoid);
          InputOutput.<String>println(_plus);
          Runtime _runtime = Runtime.getRuntime();
          String _location = desc.getLocation();
          String _plus_1 = ("/usr/bin/vlc --play-and-exit " + _location);
          String _plus_2 = (_plus_1 + "");
          Process p = _runtime.exec(_plus_2);
          p.waitFor();
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    videoGen.getVideoseqs().forEach(_function);
  }
  
  @Test
  public void testCreateCSV() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    Assert.assertNotNull(videoGen);
    InputOutput.<String>println(videoGen.getInformation().getAuthorName());
    final VideoList videoList = new VideoList();
    EList<VideoSeq> _videoseqs = videoGen.getVideoseqs();
    for (final VideoSeq video : _videoseqs) {
      if ((video instanceof MandatoryVideoSeq)) {
        String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
        String _location = ((MandatoryVideoSeq)video).getDescription().getLocation();
        Video _video = new Video(_videoid, _location);
        videoList.add(_video);
      } else {
        if ((video instanceof AlternativeVideoSeq)) {
        } else {
          if ((video instanceof OptionalVideoSeq)) {
          }
        }
      }
    }
  }
}
