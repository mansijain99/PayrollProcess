package com.payroll.pojo;

import javax.persistence.*;

@Entity
@Table(name = "PayrollEvent")
public class PayrollEvent {
	
	@Id
    @Column(name = "SequenceNo")
    private int sequenceNo;

    @Column(name = "EmpID")
    private String empId;

    @Column(name = "EventType")
    private String eventType;

    @Column(name = "EventValue")
    private String eventValue;

    @Column(name = "EventDate")
    private String eventDate;

    @Column(name = "Notes")
    private String notes;

    public PayrollEvent() {
    }

    // Getters and Setters
    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

	@Override
	public String toString() {
		return "PayrollEvent [sequenceNo=" + sequenceNo + ", empId=" + empId + ", eventType=" + eventType
				+ ", eventValue=" + eventValue + ", eventDate=" + eventDate + ", notes=" + notes + "]";
	}
}
