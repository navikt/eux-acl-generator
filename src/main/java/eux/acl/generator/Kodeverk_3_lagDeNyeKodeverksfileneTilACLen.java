package eux.acl.generator;

import eux.acl.generator.facade.ResultaterFacade;

import static eux.acl.generator.service.FilService.V44;

public class Kodeverk_3_lagDeNyeKodeverksfileneTilACLen {


    /**
     * Manuell justering av (for lange) enumkoder?
     * <p>
     * Kopier aktuelle enum-filer og legg dem i diff/kodeverk/../manuelleJusteringer - pakka. (Disse filene blir aldri overskrevet)
     * Endre 'enumkode' i disse filene til ønsket verdi - og: Kjør.
     *
     */
    public static void main(String[] args) {
        new ResultaterFacade()
                .lagNyeEnumfilerForAcl(V44);
    }

}
