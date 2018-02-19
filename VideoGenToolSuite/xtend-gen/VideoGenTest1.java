import com.google.common.base.Objects;
import fr.huitel.monfort.VideoGenFiltersAndText;
import fr.huitel.monfort.VideoGenUtils;
import fr.huitel.monfort.VideoGeneratorHelper;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

@SuppressWarnings("all")
public class VideoGenTest1 {
  private static HashMap<Integer, String> videoIdAndPath = new HashMap<Integer, String>();
  
  @Test
  public void testVerifNbVariantes() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example3.videogen"));
    Assert.assertEquals(new VideoGenUtils().nbVariantes(videoGen), VideoGeneratorHelper.getLineNumber("videosListExample3.csv"));
  }
  
  @Test
  public void testLoadModel() {
    try {
      final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example7.videogen"));
      Assert.assertNotNull(videoGen);
      final FileWriter writer = new FileWriter("playlistOfVideos.txt");
      final Consumer<Media> _function = (Media video) -> {
        try {
          if ((video instanceof MandatoryVideoSeq)) {
            String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
            String _plus = ("Mandatory video list adding... " + _videoid);
            InputOutput.<String>println(_plus);
            String _location = ((MandatoryVideoSeq)video).getDescription().getLocation();
            String _plus_1 = ("file \'" + _location);
            String _plus_2 = (_plus_1 + "\'\n");
            writer.write(_plus_2);
          }
          if ((video instanceof OptionalVideoSeq)) {
            String _videoid_1 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_3 = ("Optional list adding... " + _videoid_1);
            InputOutput.<String>println(_plus_3);
            int _nextInt = new Random().nextInt(2);
            boolean _equals = (_nextInt == 0);
            if (_equals) {
              String _location_1 = ((OptionalVideoSeq)video).getDescription().getLocation();
              String _plus_4 = ("file \'" + _location_1);
              String _plus_5 = (_plus_4 + "\'\n");
              writer.write(_plus_5);
            }
          }
          if ((video instanceof AlternativeVideoSeq)) {
            int randomizedValue = new Random().nextInt(((AlternativeVideoSeq)video).getVideodescs().size());
            String _videoid_2 = ((AlternativeVideoSeq)video).getVideodescs().get(randomizedValue).getVideoid();
            String _plus_6 = ("Alternative list adding... " + _videoid_2);
            InputOutput.<String>println(_plus_6);
            String _location_2 = ((AlternativeVideoSeq)video).getVideodescs().get(randomizedValue).getLocation();
            String _plus_7 = ("file \'" + _location_2);
            String _plus_8 = (_plus_7 + "\'\n");
            writer.write(_plus_8);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      videoGen.getMedias().forEach(_function);
      writer.close();
      Process p = Runtime.getRuntime().exec("ffmpeg -y -f concat -safe 0 -i playlistOfVideos.txt -c copy finalMp4.mp4");
      p.waitFor();
      Runtime.getRuntime().exec("ffplay -autoexit finalMp4.mp4");
      this.generateGif("finalMp4.mp4");
      this.generateImage("finalMp4.mp4");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void generateImage(final String filename) {
    try {
      Process cmd = Runtime.getRuntime().exec((((("ffmpeg -i " + filename) + " -ss 00:00:02.000 -vframes 1 thumbnails/") + filename) + "1.png"));
      cmd.waitFor();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testFiltersAndProbability() {
    try {
      final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
      Assert.assertNotNull(videoGen);
      final FileWriter writer = new FileWriter("playlistOfVideosFiltered.txt");
      String probability = "";
      final Random random = new Random();
      final double randomNumber = random.nextDouble();
      final Consumer<Media> _function = (Media video) -> {
        try {
          if ((video instanceof MandatoryVideoSeq)) {
            String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
            String _plus = ("Mandatory video list adding... " + _videoid);
            InputOutput.<String>println(_plus);
            String _replace = ((MandatoryVideoSeq)video).getDescription().getLocation().replace(".mp4", "");
            String _plus_1 = ("file \'" + _replace);
            String _plus_2 = (_plus_1 + "filtered.mp4");
            String _plus_3 = (_plus_2 + "\'\n");
            writer.write(_plus_3);
            new VideoGenFiltersAndText().createrFilterCommand(((MandatoryVideoSeq)video).getDescription());
          } else {
            if ((video instanceof OptionalVideoSeq)) {
              int _probability = ((OptionalVideoSeq)video).getDescription().getProbability();
              boolean _notEquals = (!Objects.equal(Integer.valueOf(_probability), ""));
              if (_notEquals) {
                int _probability_1 = ((OptionalVideoSeq)video).getDescription().getProbability();
                double _divide = (((double) _probability_1) / 100);
                boolean _lessEqualsThan = (randomNumber <= _divide);
                if (_lessEqualsThan) {
                  String _videoid_1 = ((OptionalVideoSeq)video).getDescription().getVideoid();
                  String _plus_4 = ("Optional video list adding... " + _videoid_1);
                  InputOutput.<String>println(_plus_4);
                  String _replace_1 = ((OptionalVideoSeq)video).getDescription().getLocation().replace(".mp4", "");
                  String _plus_5 = ("file \'" + _replace_1);
                  String _plus_6 = (_plus_5 + "filtered.mp4");
                  String _plus_7 = (_plus_6 + "\'\n");
                  writer.write(_plus_7);
                  new VideoGenFiltersAndText().createrFilterCommand(((OptionalVideoSeq)video).getDescription());
                }
              }
            } else {
              if ((video instanceof AlternativeVideoSeq)) {
                EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
                for (final VideoDescription videodesc : _videodescs) {
                  {
                    String _replace_2 = videodesc.getLocation().replace(".mp4", "");
                    String _plus_8 = ("file \'" + _replace_2);
                    String _plus_9 = (_plus_8 + "filtered.mp4");
                    String _plus_10 = (_plus_9 + "\' \n");
                    writer.write(_plus_10);
                    new VideoGenFiltersAndText().createrFilterCommand(videodesc);
                  }
                }
              }
            }
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      videoGen.getMedias().forEach(_function);
      writer.close();
      Process p = Runtime.getRuntime().exec("ffmpeg -y -f concat -safe 0 -i playlistOfVideosFiltered.txt -c copy filteredMp4.mp4");
      p.waitFor();
      Runtime.getRuntime().exec("ffplay -autoexit filteredMp4.mp4");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Question 3
   */
  public void generateGif(final String location) {
    try {
      Runtime _runtime = Runtime.getRuntime();
      String _replace = location.replace(".mp4", "").replace("/", "");
      String _plus = (((("ffmpeg -i " + location) + " -ss 00:00:00.000 -pix_fmt rgb24 -r 10 -s 800x600 -t 00:00:05.000 ") + "gif/") + _replace);
      String _plus_1 = (_plus + ".gif");
      Process cmd = _runtime.exec(_plus_1);
      cmd.waitFor();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testCreateCSV() {
    VideoGenTest1.generateCsv(6);
  }
  
  /**
   * Question 1 et 2 TP3
   * Générateur de toute les listes de playlist
   */
  public static ArrayList<ArrayList<VideoDescription>> listOfPlaylistVideos(final VideoGeneratorModel videoGen) {
    ArrayList<ArrayList<VideoDescription>> listOfPlaylist = new ArrayList<ArrayList<VideoDescription>>();
    ArrayList<VideoDescription> _arrayList = new ArrayList<VideoDescription>();
    listOfPlaylist.add(_arrayList);
    EList<Media> _medias = videoGen.getMedias();
    for (final Media video : _medias) {
      if ((video instanceof MandatoryVideoSeq)) {
        for (final ArrayList<VideoDescription> list : listOfPlaylist) {
          list.add(((MandatoryVideoSeq)video).getDescription());
        }
      } else {
        if ((video instanceof OptionalVideoSeq)) {
          ArrayList<ArrayList<VideoDescription>> listTmp = new ArrayList<ArrayList<VideoDescription>>();
          for (final ArrayList<VideoDescription> list_1 : listOfPlaylist) {
            {
              Object _clone = list_1.clone();
              ArrayList<VideoDescription> tmp = ((ArrayList<VideoDescription>) _clone);
              listTmp.add(list_1);
              tmp.add(((OptionalVideoSeq)video).getDescription());
              listTmp.add(tmp);
            }
          }
          listOfPlaylist = listTmp;
        } else {
          if ((video instanceof AlternativeVideoSeq)) {
            ArrayList<ArrayList<VideoDescription>> listTmp_1 = new ArrayList<ArrayList<VideoDescription>>();
            EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
            for (final VideoDescription vids : _videodescs) {
              for (final ArrayList<VideoDescription> list_2 : listOfPlaylist) {
                {
                  Object _clone = list_2.clone();
                  ArrayList<VideoDescription> tmp = ((ArrayList<VideoDescription>) _clone);
                  tmp.add(vids);
                  listTmp_1.add(tmp);
                }
              }
            }
            listOfPlaylist = listTmp_1;
          }
        }
      }
    }
    return listOfPlaylist;
  }
  
  /**
   * Génère les fichiers CSV avec l'id des fichiers example.videogen lus
   * @param int :id
   */
  public static void generateCsv(final int id) {
    try {
      String content = "";
      final VideoGenHelper videoGenHelper = new VideoGenHelper();
      VideoGeneratorModel videogen = videoGenHelper.loadVideoGenerator(URI.createURI((("example" + Integer.valueOf(id)) + ".videogen")));
      Assert.assertNotNull(videogen);
      InputOutput.<String>print((("example" + Integer.valueOf(id)) + ".videogen \n"));
      LinkedList<LinkedList<VideoDescription>> listVariantVid = new LinkedList<LinkedList<VideoDescription>>();
      String _content = content;
      content = (_content + "id;");
      final VideoGeneratorModel videos = videogen;
      List<String> nameColumns = new LinkedList<String>();
      int tempId = 1;
      EList<Media> _medias = videos.getMedias();
      for (final Media video : _medias) {
        if ((video instanceof MandatoryVideoSeq)) {
          nameColumns.add(((MandatoryVideoSeq)video).getDescription().getVideoid());
          String _location = ((MandatoryVideoSeq)video).getDescription().getLocation();
          String _plus = ("location" + _location);
          InputOutput.<String>print(_plus);
          int _plusPlus = tempId++;
          VideoGenTest1.videoIdAndPath.put(Integer.valueOf(_plusPlus), ((MandatoryVideoSeq)video).getDescription().getLocation());
          String _content_1 = content;
          String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus_1 = (_videoid + ";");
          content = (_content_1 + _plus_1);
        } else {
          if ((video instanceof OptionalVideoSeq)) {
            nameColumns.add(((OptionalVideoSeq)video).getDescription().getVideoid());
            String _location_1 = ((OptionalVideoSeq)video).getDescription().getLocation();
            String _plus_2 = ("location" + _location_1);
            InputOutput.<String>print(_plus_2);
            String _content_2 = content;
            String _videoid_1 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_3 = (_videoid_1 + ";");
            content = (_content_2 + _plus_3);
            int _plusPlus_1 = tempId++;
            VideoGenTest1.videoIdAndPath.put(Integer.valueOf(_plusPlus_1), ((OptionalVideoSeq)video).getDescription().getLocation());
          } else {
            if ((video instanceof AlternativeVideoSeq)) {
              EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
              for (final VideoDescription videodesc : _videodescs) {
                {
                  int _plusPlus_2 = tempId++;
                  VideoGenTest1.videoIdAndPath.put(Integer.valueOf(_plusPlus_2), videodesc.getLocation());
                  nameColumns.add(videodesc.getVideoid());
                  String _content_3 = content;
                  String _videoid_2 = videodesc.getVideoid();
                  String _plus_4 = (_videoid_2 + ";");
                  content = (_content_3 + _plus_4);
                }
              }
            }
          }
        }
      }
      ArrayList<ArrayList<VideoDescription>> _listOfPlaylistVideos = VideoGenTest1.listOfPlaylistVideos(videogen);
      for (final ArrayList<VideoDescription> listVariantVideos : _listOfPlaylistVideos) {
        {
          LinkedList<VideoDescription> temp = new LinkedList<VideoDescription>();
          for (int i = 0; (i < listVariantVideos.size()); i++) {
            {
              VideoDescription currentVideo = listVariantVideos.get(i);
              temp.add(currentVideo);
            }
          }
          listVariantVid.add(temp);
        }
      }
      String _content_3 = content;
      content = (_content_3 + "size;");
      String _content_4 = content;
      content = (_content_4 + "realSize\n");
      int ii = 1;
      for (final LinkedList<VideoDescription> listVariantVideos_1 : listVariantVid) {
        {
          int size = 0;
          String _content_5 = content;
          String _plus_4 = (Integer.valueOf(ii) + ";");
          content = (_content_5 + _plus_4);
          List<String> listName = new ArrayList<String>();
          for (int i = 0; (i < listVariantVideos_1.size()); i++) {
            {
              String _location_2 = listVariantVideos_1.get(i).getLocation();
              final File currentFile = new File(_location_2);
              long _length = currentFile.length();
              final int currentFileSize = ((int) _length);
              int _size = size;
              size = (_size + currentFileSize);
              String _videoid_2 = listVariantVideos_1.get(i).getVideoid();
              String _plus_5 = (_videoid_2 + "\n");
              InputOutput.<String>print(_plus_5);
              listName.add(listVariantVideos_1.get(i).getVideoid());
            }
          }
          for (final String name : nameColumns) {
            boolean _contains = listName.contains(name);
            if (_contains) {
              String _content_6 = content;
              content = (_content_6 + "TRUE;");
            } else {
              String _content_7 = content;
              content = (_content_7 + "FALSE;");
            }
          }
          String _content_8 = content;
          String _plus_5 = (Integer.valueOf(size) + "\n");
          content = (_content_8 + _plus_5);
          InputOutput.<String>print(content);
          ii++;
        }
      }
      String filename = (("videosListExample" + Integer.valueOf(id)) + ".csv");
      final FileWriter file = new FileWriter(filename);
      file.write(content);
      file.close();
      VideoGeneratorHelper.getRealSize(filename, VideoGenTest1.videoIdAndPath);
      InputOutput.<String>println(("CSV contenu \n" + content));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
