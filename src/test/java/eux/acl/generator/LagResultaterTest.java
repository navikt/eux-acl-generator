package eux.acl.generator;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.ResultaterFacade;
import org.junit.jupiter.api.Test;

public class LagResultaterTest extends Versjoner {

    @Test
    public void lagKodeverksfiler() {
        ResultaterFacade resultaterFacade = new ResultaterFacade();
        resultaterFacade.lagNyeEnumfilerForAcl(V44);
    }

    @Test
    public void lagACL_malerV44() {
        ResultaterFacade resultaterFacade = new ResultaterFacade();
        resultaterFacade.lagAclMaler("A", V44);
        resultaterFacade.lagAclMaler("DA", V44);
        resultaterFacade.lagAclMaler("F", V44);
        resultaterFacade.lagAclMaler("H", V44);
        resultaterFacade.lagAclMaler("M", V44);
        resultaterFacade.lagAclMaler("P", V44);
        resultaterFacade.lagAclMaler("R", V44);
        resultaterFacade.lagAclMaler("S", V44);
        resultaterFacade.lagAclMaler("U", V44);
        resultaterFacade.lagAclMaler("X", V44);
    }


}
