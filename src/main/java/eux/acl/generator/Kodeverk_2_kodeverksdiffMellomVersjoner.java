package eux.acl.generator;

import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.service.FilService.V43;
import static eux.acl.generator.service.FilService.V44;

public class Kodeverk_2_kodeverksdiffMellomVersjoner {

    public static void main(String[] args) {
        new DiffFacade().sammenliknerEnumeneForToCdmVersjonerOgKopiererEnumFundamentFilene(V43, V44);
    }

}
