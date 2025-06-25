package eux.acl.v43;

import static no.nav.eux.acl.v43.AclV43TestdataTest.MAPPE_TESTDATA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import no.nav.eux.rina.cpi.service.CdmMalService;
import no.nav.eux.rina.cpi.service.PdfGenerator;
import no.nav.eux.convert.eused.pdf.PdfMetaInfo;
import no.nav.eux.rina.cpi.service.PdfSedService;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;


public class PdfGeneratorTest {

  private PdfSedService pdfSedService;
  private CdmMalService cdmMalService;
  private PdfGenerator pdfGenerator;
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  @Before
  public void init() {

    pdfGenerator = new PdfGenerator(null);
    cdmMalService = new CdmMalService();
    pdfSedService = new PdfSedService(null, cdmMalService, pdfGenerator);
  }

  @Test
  public void lagePlainPdfTest() throws IOException {
    String sedType = "F002";
    String cdmSektor = "family";
    String sedFilnavn = MAPPE_TESTDATA + "/" + cdmSektor + "/" + sedType + "-EU.json";
    String euSedJson = FileUtils.readFileToString(new File(sedFilnavn), UTF_8);

    byte[] sedAsPdf = pdfSedService.getEuSedAsPlainPdf(euSedJson, sedType, "4.3");
//    lagPdfFil(sedAsPdf, "target/sedPreview43.pdf");
    Assertions.assertThat(sedAsPdf).contains("%PDF-".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void lagePdfMedHeaderOgFooterTest() throws IOException {
    String sedType = "F027";
    String cdmSektor = "family";
    String sedFilnavn = MAPPE_TESTDATA + "/" + cdmSektor + "/" + sedType + "-EU.json";
    String euSedJson = FileUtils.readFileToString(new File(sedFilnavn), UTF_8);
    PdfMetaInfo sedMeta = new PdfMetaInfo();
    sedMeta.setSedTypeVersion("4.3");
    sedMeta.setSedType(sedType);
    sedMeta.setUpdatedSed(true);
    sedMeta.setIncoming(false);
    sedMeta.setDate("2001-01-01");
    sedMeta.setVersion("1");
    sedMeta.setCaseId("343434");
    sedMeta.setCounterparties(List.of("FI:888 (Finland)"));
    sedMeta.setLinkToDocumentInRina("http://rina.nav.no/sed");
    byte[] sedAsPdf = pdfSedService.createSedAsPdfWithHeader(sedMeta, euSedJson, "11", "12ab");
//    lagPdfFil(sedAsPdf, "target/sedWithHeader43Test.pdf");
    Assertions.assertThat(sedAsPdf).contains("%PDF-".getBytes(StandardCharsets.UTF_8));
  }

  private void lagPdfFil(byte[] pdf, String filename) {
    try {
      String pdfFileName = filename;
      OutputStream out = new FileOutputStream(pdfFileName);
      out.write(pdf);
      out.close();
    } catch (IOException e) {
      System.out.println("ERROR - kunne ikke lage pdf-fil. " + e.getLocalizedMessage());
    }
  }

}

