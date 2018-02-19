package fr.binome.vtff;

import java.io.File;

public class Video {
	private Integer id;
	private String name;
	private String path;
	private String type;
	private Long size;
	private boolean presence;
	//TODO : Marquer 
	
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
	
	public Video(String name, String path, String type, boolean presence) {
		super();
		this.name = name;
		this.path = path;
		this.type = type;
		try {
			size = new File(path).length();
		}catch(Exception e) {
			System.out.println("Le chemin " + path + " est introuvable");
		}
		this.presence = presence;
	}
	
	
	public Video(String name, String path, boolean presence) {
		super();
		this.name = name;
		this.path = path;
		try {
			size = new File(path).length();
		}catch(Exception e) {
			System.out.println("Le chemin " + path + " est introuvable");
		}
		this.presence = presence;
	}
	
	public Video(String name, String path, Long size, boolean presence) {
		super();
		this.name = name;
		this.path = path;
		this.size = size;
		this.presence = presence;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPresence() {
		return presence;
	}
	public void setPresence(boolean presence) {
		this.presence = presence;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Video [name=" + name + ", path=" + path + ", presence=" + presence + "]";
	}
	
}
