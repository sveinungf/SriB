package no.srib.app.client.model;

import java.io.Serializable;
import java.util.List;

public class ArticleMedia implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String altText;
	private List<ArticleImage> sizes;

	public ArticleMedia() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAltText() {
		return altText;
	}

	public void setAlt_text(String altText) {
		this.altText = altText;
	}

	public List<ArticleImage> getSizes() {
		return sizes;
	}

	public void setSizes(List<ArticleImage> sizes) {
		this.sizes = sizes;
	}

	@Override
	public String toString() {
		return "ArticleMedia [id=" + id + ", altText=" + altText + ", sizes="
				+ sizes + "]";
	}
}
