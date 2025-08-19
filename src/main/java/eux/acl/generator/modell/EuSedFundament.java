package eux.acl.generator.modell;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EuSedFundament {

    private String verdiType;
    private String cdmType;
    private String indeks;
    private String ledetekst;
    private String enumNavn;
    private String enumNavnId;
//    private String tekstId;
    // Teori: ulike versjoner kan ha samme tekstId, men ulike stier. Da har vi sammenhengen mellom ny og gammel sti!
    private String tekstlengde;
}
