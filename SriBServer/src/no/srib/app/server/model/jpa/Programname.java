package no.srib.app.server.model.jpa;

import javax.persistence.*;

import no.srib.app.server.model.AbstractModel;

/**
 * The persistent class for the programname database table.
 * 
 */
@Entity
@Table(name = "app_programname")
@NamedQuery(name = "Programname.findAll", query = "SELECT p FROM Programname p")
public class Programname extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    protected Programname() {
    }
    
    public Programname(final String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
