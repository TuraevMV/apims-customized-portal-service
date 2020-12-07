package apims.cps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceListModel {
    private String routes;
    private Boolean hasroutes;
    private String title;
    private String description;
    private Boolean compcheck;
    private String servicelogo;
    private Boolean isnew;
    private Boolean hascomponents;
    private String uuid;
}