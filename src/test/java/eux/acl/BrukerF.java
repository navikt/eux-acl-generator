package eux.acl;

public class BrukerF {
    private String fornavn;
    private String etternavn;
    private String kjoenn;
    private String foedselsdato;

    private Adresse44 adresse;
    private Kontakt kontakt;
    private Pin pin;
    private StatsborgerskapF statsborgerskap;

    public static BrukerF populerMedSedFraAcl(/*AclBruker aclBruker*/) {
        return new BrukerF();
    }
    public String lagSedElement() {
        return "sedBrukerF";
    }
}
