package no.srib.sribapp.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;

import java.sql.Time;


/**
 * The persistent class for the schedule database table.
 * 
 */
@Entity
@Table(name="schedule")
@NamedQuery(name="Schedule.findAll", query="SELECT s FROM Schedule s")
@XmlType(name = "") // Remove "@type" from the marshalled JSON
public class Schedule extends AbstractModel  {
	private static final long serialVersionUID = 1L;
	private int id;
	private byte day;
	private Time fromtime;
	private int program;
	private Time totime;

	public Schedule() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(nullable=false)
	public byte getDay() {
		return this.day;
	}

	public void setDay(byte day) {
		this.day = day;
	}


	@Column(nullable=false)
	public Time getFromtime() {
		return this.fromtime;
	}

	public void setFromtime(Time fromtime) {
		this.fromtime = fromtime;
	}


	@Column(nullable=false)
	public int getProgram() {
		return this.program;
	}

	public void setProgram(int program) {
		this.program = program;
	}


	@Column(nullable=false)
	public Time getTotime() {
		return this.totime;
	}

	public void setTotime(Time totime) {
		this.totime = totime;
	}

}