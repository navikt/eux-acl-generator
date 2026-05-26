package eux.acl.generator.eksekvering.pdf;

import eux.acl.generator.eksekvering.Versjoner;

import static eux.acl.generator.eksekvering.acl.Acl_1a__kopierInnEuSedOgKodeverkFundament.kopierInnEuSedFundament;
import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;

public class Pdf_1a__kopierTilFundament extends Versjoner {

	public static void main(String[] args) {
		kopierInnKodeverkFundament(MAPPE_VERSJON_v45);
		kopierInnEuSedFundament(MAPPE_VERSJON_v45);
	}
}
