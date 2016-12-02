package po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_person")
public class InfoPerson extends BaseEntity{
	@Id
    private Integer personId;

    private String personName;

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }
}