package eux.acl.generator.eksekvering.pdf;

import eux.acl.generator.facade.FundamentFacade;

import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1_lagKodeverkFundament.lagKodeverkFundament;
import static eux.acl.generator.service.FilService.*;

public class Pdf_1_lagKodeverkFundament {


    public static void main(String[] args) {
        lagKodeverkFundament(V43);
        lagKodeverkFundament(V44);
    }

}
