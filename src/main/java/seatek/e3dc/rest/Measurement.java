package seatek.e3dc.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Measurement {
	private int solarleistung;
	private int netzleistung;
	private int hausleistung;
}
