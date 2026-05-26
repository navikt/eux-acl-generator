package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.TestdataFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Testdata_2_lagTestdata extends Versjoner {

	public static void main(String[] args) {
		lagTestdata(NY_VERSJON);
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
