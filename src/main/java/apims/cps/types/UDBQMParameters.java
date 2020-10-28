package apims.cps.types;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class UDBQMParameters {
    private String itemType;  //STRING, INTEGER, BIGDECIMAL
    private String itemString;
    private Integer itemInteger;
    private BigDecimal itemBigDecimal;
}
