package app.data.aircraft;

import javax.persistence.*;

@Entity
public class Aircraft {

    @Id
    private String registration;

    @Column(name = "type")
    private String type;

    public Aircraft() {
    }

    public Aircraft(String type, String registration) {
        this.type = type;
        this.registration = registration;
    }
    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return type.hashCode() + registration.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(super.equals(obj))
            return true;
        if(obj instanceof Aircraft) {
            Aircraft object = (Aircraft) obj;
            if(type.equals(object.type) && registration.equals(object.registration))
                return true;
        }
        return false;
    }
}
