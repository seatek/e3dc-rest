package seatek.e3dc.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.Register;

@Component
public class ModbusRepository {
	AbstractModbusMaster master;

	@Value("${e3dc.ip}")
	private String address;

	@PostConstruct
	public void init() {
		this.master = new ModbusTCPMaster(this.address, 502);
		try {
			this.master.connect();
		} catch (Exception e) {
			throw new IllegalStateException("Could not start modbus client", e);
		}
	}

	@PreDestroy
	public void shutdown() {
		this.master.disconnect();
	}

	public int getHausleistung() {
		int ref = 40072;
		return readInt(ref, 1);
	}

	private int readInt(final int ref, final int count) {
		Register[] mulReg;
		try {
			mulReg = this.master.readMultipleRegisters(ref - 1, count);
		} catch (ModbusException e) {
			throw new RuntimeException(e);
		}

		return mulReg[0].getValue();
	}

	public int getSolarleistung() {
		return readInt(40068, 1);
	}

	public int getBatterieleistung() {
		return readInt(40070, 1);
	}

	public int getBatteriekapazität() {
		return readInt(40083, 1);
	}

	public int getNetzleistung() {
		int possiblyNegative = readInt(40074, 1);
		int adjusted = convertNegativeShort(possiblyNegative);
		return adjusted;
	}

	private int convertNegativeShort(int possiblyNegative) {
		if ((possiblyNegative & 0xF000) > 0) {
			possiblyNegative ^= 0xFFFF;
			possiblyNegative++;
			possiblyNegative *= -1;

		}
		return possiblyNegative;
	}

	public Measurement getMeasurement() {
		Measurement m = new Measurement();
		BeanUtils.copyProperties(this, m);
		return m;
	}
}
