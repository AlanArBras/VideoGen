package fr.binome.vtff;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.binome.vtff.Playlist;

public class Playlist {
	private LinkedList<Video> videos;

	public Playlist() {
		videos = new LinkedList<>();
	}
	
	public Playlist(LinkedList<Video> list) {
		videos = list;
	}
	
	public void add(Video video) {
		if(video != null && video instanceof Video) {
			videos.add(video);
		}
	}
	
	public void addEnd(Video video) {
		if(video != null && video instanceof Video) {
			videos.addLast(video);
		}
	}
	
	public Video get(int index) {
		return videos.get(index);
	}
	
	public Video pop() {
		return videos.pop();
	}
	
	public void addAll(Playlist list) {
		videos.addAll(list.videos);
	}
	
	public int sizeList() {
		return videos.size();
	}

	@Override
	public String toString() {
		return "VideoList [videos=" + videos.toString() + "]";
	}
	


}
