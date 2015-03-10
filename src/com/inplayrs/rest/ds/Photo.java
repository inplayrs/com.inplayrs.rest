package com.inplayrs.rest.ds;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "photo")
public class Photo {

	@Id
	@Column(name = "photo_id")
	@GeneratedValue
	private Integer photo_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game", nullable = false)
	@JsonIgnore
	private Game game;
	
	@Column(name = "game", insertable=false, updatable = false)
	private int game_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false)
	@JsonIgnore
	private User user;
	
	@Column(name = "user", insertable=false, updatable = false)
	private Integer user_id;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "caption")
	private String caption;
	
	@Column(name = "likes")
	private int likes;
	
	@Column(name = "flags")
	private int flags;
	
	@Column(name = "active")
	private boolean active;

	@OneToMany(mappedBy="photo", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<PhotoLike> photoLikes;
	
	public Photo() {}

	public Integer getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(Integer photo_id) {
		this.photo_id = photo_id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getFlags() {
		return flags;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public Set<PhotoLike> getPhotoLikes() {
		return photoLikes;
	}

	public void setPhotoLikes(Set<PhotoLike> photoLikes) {
		this.photoLikes = photoLikes;
	}
	
	
}
