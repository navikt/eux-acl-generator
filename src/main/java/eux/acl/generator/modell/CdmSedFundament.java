package eux.acl.generator.modell;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CdmSedFundament {

    private String type;
    private String indeks;
    private String ledetekst;
    private String enumNavn;
    // Teori: ulike versjoner kan ha samme tekstId, men ulike stier. Da har vi sammenhengen mellom ny og gammel sti!
    private String tekstId;

}
