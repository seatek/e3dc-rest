package seatek.e3dc.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
	private int solarleistung;
	private int netzleistung;
	private int hausleistung;
	private int batterieleistung;
	private int batteriekapazität;
}
