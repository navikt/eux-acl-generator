
package eux.acl.generator.facade;

import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.service.AclService;
import eux.acl.generator.service.FilService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class AclMalerFacade {

    private final AclService aclService;
    private final FilService filService;

    public AclMalerFacade() {
        this.filService = new FilService();
        this.aclService = new AclService();
    }
    public void sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(List<String> sedTyper, String cdmVersjonForrige, String cdmVersjonNy) {
        // samtlige filer med euSti -> aclSti eller tom
        // stier med enum
        // gruppere i objekter?

        sedTyper.forEach(sedType -> {
            TreeMap<String, EuSedFundament> euSedFundamentForrige = filService.lesEuSedFundamentFil(sedType, cdmVersjonForrige);
            TreeMap<String, EuSedFundament> euSedFundamentNy = filService.lesEuSedFundamentFil(sedType, cdmVersjonNy);
        });



    }
}
