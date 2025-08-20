package eux.acl.generator;

import eux.acl.generator.facade.ResultaterFacade;
import org.junit.jupiter.api.Test;

import static eux.acl.generator.service.FilService.*;

public class LagResultaterTest {

    @Test
    public void lagerKodeverksfiler() {
        ResultaterFacade resultaterFacade = new ResultaterFacade();
        resultaterFacade.lagNyeEnumfilerForAcl(V44);
    }

}
