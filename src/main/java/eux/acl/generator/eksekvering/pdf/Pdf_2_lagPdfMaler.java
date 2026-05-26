package eux.acl.generator.eksekvering.pdf;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.ResultaterFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Pdf_2_lagPdfMaler extends Versjoner {


	public static void main(String[] args) {
		lagPdfMaler(NY_VERSJON);
	}

	public static void lagPdfMaler(String cdmVersjon) {
		ResultaterFacade pdfMalerFacade = new ResultaterFacade();
		pdfMalerFacade.lagPdfMaler(SEDTYPER_A, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_DA, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_F, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_H, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_M, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_P, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_R, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_S, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_U, cdmVersjon);
		pdfMalerFacade.lagPdfMaler(SEDTYPER_X, cdmVersjon);
	}

}
