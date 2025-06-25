package eux.acl;

import lombok.Builder;

@Builder
public class BrukerPersonInfo {


    private Brukeradresse brukeradresse;
    private String statsborgerskap;
    private String kjoenn;
    private String fornavn;
    private Adresse adresse;
}
