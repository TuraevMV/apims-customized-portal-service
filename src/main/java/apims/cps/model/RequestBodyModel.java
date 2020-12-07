package apims.cps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyModel {
   private int firstResult;
   private int maxResult;
   private String searchQuery;
}
