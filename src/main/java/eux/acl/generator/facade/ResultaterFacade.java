package eux.acl.generator.facade;

import eux.acl.generator.service.FilService;
import eux.acl.generator.service.ResultaterService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ResultaterFacade {
    private final FilService filService;
    private final ResultaterService resultaterService;

    public ResultaterFacade() {
        this.filService = new FilService();
        this.resultaterService = new ResultaterService();
    }

    public void lagNyeEnumfilerForAcl(String cdmVersjon) {
        List<String> endredeEnumer = filService.lesNavneneTilEndredeKodeverksfiler(cdmVersjon);
        endredeEnumer.forEach(enumNavn -> resultaterService.opprettEnumFilForAcl(enumNavn, cdmVersjon));
    }
}
