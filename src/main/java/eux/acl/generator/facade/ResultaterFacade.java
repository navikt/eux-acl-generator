package eux.acl.generator.facade;

import eux.acl.generator.service.AclService;
import eux.acl.generator.service.FilService;
import eux.acl.generator.service.KodeverkService;
import eux.acl.generator.service.PdfService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ResultaterFacade {
    private final FilService filService;
    private final PdfService pdfService;
    private final KodeverkService kodeverkService;
    private final AclService aclService;

    public ResultaterFacade() {
        this.filService = new FilService();
        this.pdfService = new PdfService();
        this.kodeverkService = new KodeverkService();
        this.aclService = new AclService();
    }

    public void lagNyeEnumfilerForAcl(String cdmVersjon) {
        List<String> endredeEnumer = filService.lesNavneneTilEndredeKodeverksfiler(cdmVersjon);
        endredeEnumer.forEach(enumNavn -> kodeverkService.opprettEnumFilForAcl(enumNavn, cdmVersjon));


    }

    public void lagAclMaler(String sektor, String cdmVersjon) {
        aclService.lagAclMal(sektor, cdmVersjon);
    }

    public void lagPdfMaler(List<String> sedTyper, String cdmVersjon) {
        sedTyper.forEach(sedType -> pdfService.lagPdfMal(sedType, cdmVersjon));
    }

}
