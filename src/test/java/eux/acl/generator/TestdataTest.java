package eux.acl.generator;

import eux.acl.generator.facade.ResultaterFacade;
import eux.acl.generator.facade.TestdataFacade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static eux.acl.generator.service.FilService.*;

public class TestdataTest {



    @Test
    public void lag_testdata_v44() {
        TestdataFacade testdataFacade = new TestdataFacade();
        testdataFacade.lagTestdata(SEDTYPER_F, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_H, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_DA, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_M, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_P, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_R, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_S, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_U, V44, 0);
        testdataFacade.lagTestdata(SEDTYPER_X, V44, 0);
    }

}
