package de.uni.bremen.stummk.psp.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents an pip entry
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "pip")
public class PIP implements PersistenceItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private String problemDescription;

  @JoinColumn
  private String proposalDescription;

  @JoinColumn
  private String notes;

  @ManyToOne
  private Project project;

  /**
   * Constructor
   */
  public PIP() {
    // for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param problemDescription the problem description of the pip entry
   * @param proposalDescription the proposal description of the pip entry
   * @param notes the notes
   * @param project the project the pip entry belongs to
   */
  public PIP(String problemDescription, String proposalDescription, String notes, Project project) {
    this.problemDescription = problemDescription;
    this.proposalDescription = proposalDescription;
    this.notes = notes;
    this.project = project;
  }

  /**
   * Constructor
   * 
   * @param ID the id of the pip entry
   * @param problemDescription the problem description of the pip entry
   * @param proposalDescription the proposal description of the pip entry
   * @param notes the notes
   * @param project the project the pip entry belongs to
   */
  public PIP(long ID, String problemDescription, String proposalDescription, String notes, Project project) {
    this.ID = ID;
    this.problemDescription = problemDescription;
    this.proposalDescription = proposalDescription;
    this.notes = notes;
    this.project = project;
  }

  /**
   * @return the problem description
   */
  public String getProblemDescription() {
    return this.problemDescription;
  }

  /**
   * @return the proposal description
   */
  public String getProposalDescription() {
    return this.proposalDescription;
  }

  /**
   * @return the description
   */
  public String getNotes() {
    return this.notes;
  }

  @Override
  public long getID() {
    return this.ID;
  }

  @Override
  public List<String> getElements() {
    List<String> elements = new ArrayList<>();
    elements.add("" + ID);
    elements.add(problemDescription);
    elements.add(proposalDescription);
    elements.add(notes);

    return elements;
  }

  /**
   * Refreshes the attributes of the PIP entry
   * 
   * @param problem the problem description of the pip entry
   * @param proposal the proposal description of the pip entry
   * @param notes the notes
   */
  public void refreshItems(String problem, String proposal, String notes) {
    this.problemDescription = problem;
    this.proposalDescription = proposal;
    this.notes = notes;
  }

  @Override
  public String toString() {
    return ID + "#" + problemDescription + "#" + proposalDescription + "#" + notes;
  }

  @Override
  public Project getProject() {
    return project;
  }

}
