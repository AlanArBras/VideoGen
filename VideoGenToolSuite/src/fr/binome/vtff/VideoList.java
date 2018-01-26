package fr.binome.vtff;

import java.util.ArrayList;

public class VideoList {
	private ArrayList<Video> videos;

	public VideoList() {
		videos = new ArrayList<>();
	}
	
	public VideoList(ArrayList<Video> list) {
		videos = list;
	}
	
	public void add(Video video) {
		if(video != null && video instanceof Video) {
			video.setId(new Long(videos.size()+1));
			videos.add(video);
		}
	}
}
