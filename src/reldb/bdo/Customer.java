/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */
package reldb.bdo;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Customer {
    
    private Integer id;
    private String name;
    private LocalDate birthdate;
    private String street;
    private String city;
    private String postcode;
    private ObservableList<RentTitle> titles;
    
    public Customer(){
        titles = FXCollections.observableArrayList();
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public ObservableList<RentTitle> getTitles() {
        return titles;
    }

    public void setTitles(ObservableList<RentTitle> titles) {
        this.titles = titles;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setBirthdate(LocalDate  birthdate){
        this.birthdate = birthdate;
    }
    
    public LocalDate getBirthdate(){
        return this.birthdate;
    }
    
    public void setStreet(String street){
        this.street = street;
    }
    
    public String getStreet(){
        return this.street;
    }
    
    public void setCity(String city){
        this.city = city;
    }
    
    public String getCity(){
        return this.city;
    }
    
    public void setPostcode(String postcode){
        this.postcode = postcode;
    }
    
    public String getPostcode(){
        return this.postcode;
    }
    
}
