package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.TestdataFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.modell.SedTyper.SEDTYPER_P;
import static eux.acl.generator.modell.SedTyper.SEDTYPER_P3000;
import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_TESTDATAR;
import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_TESTDATA_TARGET;

@Slf4j
public class Testdata_2_lagTestdata extends Versjoner {

	public static class LagTestdata {

		public static void main(String[] args) {
			lagTestdata(NY_VERSJON);
		}
	}

	public static class KopierTestdataFraTargetTilResultater {

		public static void main(String[] args) {
			kopierInnPdfMaler(NY_MAPPE_VERSJON);
		}
	}

	public static void lagTestdata(String cdmVersjon) {
		TestdataFacade testdataFacade = new TestdataFacade();
//		testdataFacade.lagTestdata(SEDTYPER_A, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_F, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_H, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_DA, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_M, cdmVersjon, 0);
		testdataFacade.lagTestdata(SEDTYPER_P, cdmVersjon, 0);
		testdataFacade.lagTestdata(SEDTYPER_P3000, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_R, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_S, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_U, cdmVersjon, 0);
//		testdataFacade.lagTestdata(SEDTYPER_X, cdmVersjon, 0);
	}

	public static void kopierInnPdfMaler(String versjon) {
		File fraEuSedFundamentTarget = new File(MAPPE_RESULTATER_TESTDATA_TARGET + "/" + versjon);
		File tilEuSedFundamentResources = new File(MAPPE_RESULTATER_TESTDATAR);
		try {
			FileUtils.copyDirectoryToDirectory(fraEuSedFundamentTarget, tilEuSedFundamentResources);
			log.info("Kopiert testdata. Se: {}/{}", MAPPE_RESULTATER_TESTDATAR, versjon);
		} catch (IOException e) {
			log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
				, fraEuSedFundamentTarget.getAbsolutePath(), tilEuSedFundamentResources.getAbsolutePath(), e);
		}
	}
}
