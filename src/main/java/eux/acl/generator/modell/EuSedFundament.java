package eux.acl.generator.modell;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EuSedFundament {

    private String verdiType;
    private String indeks;
    private String ledetekst;
    private String enumNavn;
    // Teori: ulike versjoner kan ha samme tekstId, men ulike stier. Da har vi sammenhengen mellom ny og gammel sti!
    private String tekstId;

}
