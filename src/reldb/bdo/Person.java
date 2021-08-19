/*
 *  Copyright 2014 Rainer Stoermer
 */
package reldb.bdo;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Rainer Stoermer
 */
public class Person {

    private IntegerProperty nameId = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty height = new SimpleStringProperty();
    private StringProperty birthDate = new SimpleStringProperty();
    private StringProperty birthNotes = new SimpleStringProperty();
    private StringProperty deathDate = new SimpleStringProperty();
    private StringProperty deathNotes = new SimpleStringProperty();
    private StringProperty gender = new SimpleStringProperty();
    private List<String> roles;
    private ObservableList<PersonBiography> biographies;
    private String rolesString;
    
    
    public Person() {
        roles = new ArrayList<>();
        biographies = FXCollections.observableArrayList();
    }

    public Integer getNameId() {
        return nameId.get();
    }

    public IntegerProperty getNameIdProperty() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId.set(nameId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getHeight() {
        return height.get();
    }

    public StringProperty getHeightProperty() {
        return height;
    }

    public void setHeight(String height) {
        this.height.set(height);
    }

    public String getBirthDate() {
        return birthDate.get();
    }

    public StringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }

    public String getBirthNotes() {
        return birthNotes.get();
    }

    public StringProperty getBirthNotesProperty() {
        return birthNotes;
    }

    public void setBirthNotes(String birthNotes) {
        this.birthNotes.set(birthNotes);
    }

    public String getDeathDate() {
        return deathDate.get();
    }

    public StringProperty getDeathDateProperty() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate.set(deathDate);
    }

    public String getDeathNotes() {
        return deathNotes.get();
    }

    public StringProperty getDeathNotesProperty() {
        return deathNotes;
    }

    public void setDeathNotes(String deathNotes) {
        this.deathNotes.set(deathNotes);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public ObservableList<PersonBiography> getBiographies() {
        return biographies;
    }

    public void setBiographies(ObservableList<PersonBiography> biographies) {
        this.biographies = biographies;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }
    
    public String getRolesString(){
        String roleString = "";
        for(String role : roles){
            roleString =roleString + " " + role;
        }
        rolesString = roleString;
        return rolesString;
    }
}
