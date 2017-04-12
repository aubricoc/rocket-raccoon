package cat.aubricoc.rocketraccoon.model;

import com.canteratech.apa.annotation.Entity;
import com.canteratech.apa.annotation.Id;
import com.canteratech.apa.annotation.Transient;

import java.util.List;

@Entity
public class Comic {

	@Id
	private Integer id;

	private String title;

	private String description;

	@Transient
	private Image thumbnail;

	@Transient
	private List<Image> images;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
