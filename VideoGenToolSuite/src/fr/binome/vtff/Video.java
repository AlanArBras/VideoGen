package fr.binome.vtff;

import java.io.File;

public class Video {
	private Long id;
	private String name;
	private String path;
	private Long size;
	
	
	
	public Video(String name, String path) {
		super();
		this.name = name;
		this.path = path;
		try {
			size = new File(path).length();
		}catch(Exception e) {
			System.out.println("TABARNAK CA CRASH, TON FILE MARCHE PAS, CALICE!");
		}
	}
	public Video(String name, String path, Long size) {
		super();
		this.name = name;
		this.path = path;
		this.size = size;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
}
