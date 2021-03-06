package no.srib.app.server.model.jpa;

import javax.persistence.*;

import org.apache.commons.lang3.StringEscapeUtils;

import no.srib.app.server.model.AbstractModel;

/**
 * The persistent class for the programinfo database table.
 * 
 */
@Entity
@Table(name = "PROGRAMINFO")
@NamedQuery(name = "Programinfo.findAll", query = "SELECT p FROM Programinfo p ORDER BY p.title")
public class Programinfo extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private int program;
    private String category;
    private String description;
    private String imglink;
    private String subtitle;
    private String summary;
    private String title;

    protected Programinfo() {
    }

    @Id
    @Column(insertable = false, unique = true, nullable = false)
    public int getProgram() {
        return this.program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    @Column(insertable = false, updatable = false, length = 60)
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Lob
    @Column(insertable = false, updatable = false)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = StringEscapeUtils.unescapeHtml4(description);
    }

    @Column(insertable = false, updatable = false, length = 50)
    public String getImglink() {
        return this.imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    @Lob
    @Column(insertable = false, updatable = false)
    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = StringEscapeUtils.unescapeHtml4(subtitle);
    }

    @Lob
    @Column(insertable = false, updatable = false)
    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = StringEscapeUtils.unescapeHtml4(summary);
    }

    @Column(insertable = false, updatable = false, length = 50)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = StringEscapeUtils.unescapeHtml4(title);
    }
}
