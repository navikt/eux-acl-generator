package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.facade.TestdataFacade;

import static eux.acl.generator.modell.SedTyper.*;
import static eux.acl.generator.service.FilService.*;

public class Testdata_2_lagTestdata {

    public static void main(String[] args) {
        lagTestdata(V44);
    }

    public static void lagTestdata(String cdmVersjon) {
        TestdataFacade testdataFacade = new TestdataFacade();
        testdataFacade.lagTestdata(SEDTYPER_A, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_F, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_H, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_DA, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_M, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_P, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_R, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_S, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_U, cdmVersjon, 0);
        testdataFacade.lagTestdata(SEDTYPER_X, cdmVersjon, 0);
    }
}
