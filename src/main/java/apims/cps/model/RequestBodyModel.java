package apims.cps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestBodyModel {
   private int firstResult;
   private int maxResult;
   private String searchQuery;
}
