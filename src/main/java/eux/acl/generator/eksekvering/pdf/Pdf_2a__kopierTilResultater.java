package eux.acl.generator.eksekvering.pdf;

import eux.acl.generator.eksekvering.Versjoner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_PDF;
import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_PDF_TARGET;

@Slf4j
public class Pdf_2a__kopierTilResultater extends Versjoner {


	public static void main(String[] args) {
		kopierInnPdfMaler(NY_MAPPE_VERSJON);
	}

	public static void kopierInnPdfMaler(String versjon) {
		File fraEuSedFundamentTarget = new File(MAPPE_RESULTATER_PDF_TARGET + "/" + versjon);
		File tilEuSedFundamentResources = new File(MAPPE_RESULTATER_PDF);
		try {
			FileUtils.copyDirectoryToDirectory(fraEuSedFundamentTarget, tilEuSedFundamentResources);
			log.info("Kopiert pdfMaler. Se: {}/{}", MAPPE_RESULTATER_PDF, versjon);
		} catch (IOException e) {
			log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
				, fraEuSedFundamentTarget.getAbsolutePath(), tilEuSedFundamentResources.getAbsolutePath(), e);
		}
	}
}
