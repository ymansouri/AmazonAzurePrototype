package org.cloudbus.intercloudsim.config;

//import org.cloudbus.intercloudsim.Log;
import org.cloudbus.cloudsim.Log;


/**
 * All InterCloud Properties.
 * 
 * This class's methods must be used instead of System.{get|set}Property()
 * methods.
 * 
 * This code is adapted from ProActive Parallel Suite (PAProperties.java):
 * http://proactive.inria.fr/
 * 
 * @author Adel Nadjaran Toosi
 * @author Mohsen Amini Salehi
 * @author Alexandre di Costanzo
 */
public enum ICProperties {

	IC_EXPERIMENT_ID("experiment.id", ICPropertiesType.STRING),
	
	IC_EXPERIMENT_RUN("experiment.run", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_PERIOD_TIME("period.time", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_WINDOW_TIME("window.time", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_DATACENTER_SPECIFICATION("datacenter.specification", ICPropertiesType.STRING),
	IC_SIMULATION_DATACENTER_DELAY("datacenter.delay", ICPropertiesType.STRING),
	IC_SIMULATION_AMAZON_KEY("amazon.key", ICPropertiesType.STRING),
	IC_SIMULATION_AZURE_KEY("azure.key", ICPropertiesType.STRING),
	IC_SIMULATION_OBJECT_PLACEMENT("object.placement", ICPropertiesType.STRING),
	
	
	IC_SIMULATION_DELAY_CONSTRAINT("delay.constraint", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_NINES_NUMBER("nines.number", ICPropertiesType.LONG),
	
	IC_SIMULATION_REPLICA_NUMBER("replica.number", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_OBJECT_NUMBER("object.number", ICPropertiesType.LONG),
	
	IC_SIMULATION_OBJECT_TOTAL_SIZE("object.total.size", ICPropertiesType.LONG),
	IC_SIMULATION_SIZE_FACTOR("size.factor", ICPropertiesType.DOUBLE),
	IC_SIMULATION_BUCKET_SIZE("bucket.size", ICPropertiesType.INTEGER),
	IC_SIMULATION_OBJECT_TOTAL_READ_RATE("object.total.read.rate", ICPropertiesType.LONG),
	
	IC_SIMULATION_OBJECT_MIN_SIZE("object.min.size", ICPropertiesType.INTEGER),
	IC_SIMULATION_OBJECT_MAX_SIZE("object.max.size", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_OBJECT_SIZE_DISTRIBUTION("object.size.distribution", ICPropertiesType.STRING),
	
	IC_SIMULATION_REGION_NUMBER("region.number", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_LATENCY_SENCETIVE("latency.sencetive", ICPropertiesType.DOUBLE),
	IC_SIMULATION_PRECICES_COST("precicse.cost", ICPropertiesType.BOOLEAN),
	IC_SIMULATION_MASTER_TYPE("master.type", ICPropertiesType.INTEGER),
	IC_SIMULATION_GAMA("gama", ICPropertiesType.DOUBLE),
	
	IC_SIMULATION_OBJECT_TYPE("object.type", ICPropertiesType.STRING),
	
	IC_SIMULATION_OBJECT_READ_TO_WRITE_RATE("object.read.to.write.rate", ICPropertiesType.INTEGER),
	IC_SIMULATION_MAX_OBJECT_READ_TO_WRITE_RATE("max.object.read.to.write.rate", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_WEIBULL_SHAPE("weibull.shape", ICPropertiesType.DOUBLE),
	IC_SIMULATION_WEIBULL_SCALE("weibull.scale", ICPropertiesType.DOUBLE),
	IC_SIMULATION_SC_STATRT_INDEX("sc.start.index", ICPropertiesType.INTEGER),
	IC_SIMULATION_OBJECT_READ_RATE("object.read.rate", ICPropertiesType.INTEGER),
	
	IC_SIMULATION_OBJECT_READ_RATE_DISTRIBUTION("object.read.rate.distribution", ICPropertiesType.STRING),
	
	/*IC_SIMULATION_EMPTY_SPOT_QUEUE_AT_SIMULATION_END("simulation.empty.spot.queue.at.simulation.end", ICPropertiesType.BOOLEAN),
	
	IC_SPOT_PERCENTAGE("spot.percentage", ICPropertiesType.DOUBLE),
	
	IC_PERSISTENT_SPOT_PERCENTAGE("persistent.spot.percentage", ICPropertiesType.DOUBLE),
	
	IC_SPOT_RESERVED_PRICE("spot.reserved.price", ICPropertiesType.LONG),
	
	IC_SPOT_DYNAMIC_RESERVED_PRICE("spot.dynamic.reserved.price", ICPropertiesType.BOOLEAN),
	
	IC_SPOT_WAITING_TIME_HOURS("spot.waitinig.time.hours", ICPropertiesType.LONG),
	
	IC_RECORD_START_HOUR("record.start.hour", ICPropertiesType.LONG),
	
	IC_RECORD_FINISH_HOUR("record.finish.hour", ICPropertiesType.LONG),
	
	IC_CHARGING_PERIOD("charging.period", ICPropertiesType.INTEGER),
	
	IC_LOG_LOAD_ENABLED("log.load.enabled", ICPropertiesType.BOOLEAN),
	
	IC_LOG_AFTER_EXECUTION_LOAD_ENABLED("log.after.execution.load.enabled", ICPropertiesType.BOOLEAN),
	
	IC_LOG_INPUT_WORKLOAD_ENABLED("log.input.workload.enabled", ICPropertiesType.BOOLEAN),
	
	IC_LOG_SCREEN_ENABLED("log.screen.enabled", ICPropertiesType.BOOLEAN),
	
	IC_LOG_FILE_ENABLED("log.file.enabled", ICPropertiesType.BOOLEAN),
	
	IC_LOG_OPTION_HISTORY("log.option.history", ICPropertiesType.BOOLEAN),

	IC_LOG_FED_SPOT_PRICE_HISTORY("log.fed.spot.price.history", ICPropertiesType.BOOLEAN),
	
	IC_LOG_WORKLOAD_FOR_GRAPH("log.workload.for.graph", ICPropertiesType.BOOLEAN),
	
	IC_LOG_SPOT_PRICE_HISTORY("log.spot.price.history", ICPropertiesType.BOOLEAN),
	
	IC_LOG_SPOT_RESERVED_PRICE_HISTORY("log.spot.reserved.price.history", ICPropertiesType.BOOLEAN),
	
	IC_GLOBAL_SEED("global.seed", ICPropertiesType.LONG),

	IC_POLICY("policy", ICPropertiesType.STRING),
	IC_POLICY_OPTION("policy.option", ICPropertiesType.STRING),
	IC_POLICY_FEDERATION("policy.federation", ICPropertiesType.STRING),
	
	IC_POWER_HOST("power.usage.host",ICPropertiesType.DOUBLE),
	IC_POWER_PACKING_INTERVAL("power.packing.interval", ICPropertiesType.DOUBLE),
	IC_POWER_PRICE_ONPEAK("power.price.onpeak", ICPropertiesType.LONG),
	IC_POWER_PRICE_OFFPEAK("power.price.offpeak", ICPropertiesType.LONG),
	IC_POWER_ONPEAK_START("power.onpeak.start", ICPropertiesType.INTEGER),
	IC_POWER_ONPEAK_END("power.onpeak.end", ICPropertiesType.INTEGER),
	IC_POWER_PUE_ACTIVE("power.pue.active", ICPropertiesType.BOOLEAN),
	IC_POWER_PUE_LOAD_FLOOR("power.pue.load.floor", ICPropertiesType.DOUBLE),
	

	IC_REQUESTS_PER_DAY_WEEKDAYS_ONDEMAND("requests.per.day.weekdays.ondemand", ICPropertiesType.INTEGER),
	IC_REQUESTS_PER_DAY_WEEKENDS_ONDEMAND("requests.per.day.weekends.ondemand", ICPropertiesType.INTEGER),
	IC_REQUESTS_PER_DAY_WEEKDAYS_RESERVED("requests.per.day.weekdays.reserved", ICPropertiesType.INTEGER),
	IC_REQUESTS_PER_DAY_WEEKENDS_RESERVED("requests.per.day.weekends.reserved", ICPropertiesType.INTEGER),
	IC_SHORT_TERM_RESERVATION_CONTRACT("short.term.reservation.contract", ICPropertiesType.INTEGER),
	IC_LONG_TERM_RESERVATION_CONTRACT("long.term.reservation.contract", ICPropertiesType.INTEGER),
	IC_NO_RESERVATIONS_PER_SIMULATION("no.reservations.per.siumlation", ICPropertiesType.INTEGER),
	IC_MIN_RESERVATION_SIZE_IN_DAYS("min.reservation.size.in.days", ICPropertiesType.INTEGER),
	IC_MAX_RESERVATION_SIZE_IN_DAYS("max.reservation.size.in.days", ICPropertiesType.INTEGER),
	IC_RESERVED_PERCENTAGE("reserved.percentage", ICPropertiesType.DOUBLE),
	IC_RESERVATION_SIZE("reservation.size", ICPropertiesType.INTEGER),
	
	IC_ONDEMAND_SERIAL_PROB("ondemand.SERIAL.PROB", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_POW2_PROB("ondemand.POW2.PROB", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_ULOW("ondemand.ULOW", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_UMED("ondemand.UMED", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_UHI("ondemand.UHI", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_UPROB("ondemand.UPROB", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_A1("ondemand.A1", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_B1("ondemand.B1", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_A2("ondemand.A2", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_B2("ondemand.B2", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_PA("ondemand.PA", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_PB("ondemand.PB", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_AARR("ondemand.AARR", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_BARR("ondemand.BARR", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_ANUM("ondemand.ANUM", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_BNUM("ondemand.BNUM", ICPropertiesType.DOUBLE),
	IC_ONDEMAND_ARAR("ondemand.ARAR", ICPropertiesType.DOUBLE),
	
	IC_RESERVED_SERIAL_PROB("reserved.SERIAL.PROB", ICPropertiesType.DOUBLE),
	IC_RESERVED_POW2_PROB("reserved.POW2.PROB", ICPropertiesType.DOUBLE),
	IC_RESERVED_ULOW("reserved.ULOW", ICPropertiesType.DOUBLE),
	IC_RESERVED_UMED("reserved.UMED", ICPropertiesType.DOUBLE),
	IC_RESERVED_UHI("reserved.UHI", ICPropertiesType.DOUBLE),
	IC_RESERVED_UPROB("reserved.UPROB", ICPropertiesType.DOUBLE),
	IC_RESERVED_A1("reserved.A1", ICPropertiesType.DOUBLE),
	IC_RESERVED_B1("reserved.B1", ICPropertiesType.DOUBLE),
	IC_RESERVED_A2("reserved.A2", ICPropertiesType.DOUBLE),
	IC_RESERVED_B2("reserved.B2", ICPropertiesType.DOUBLE),
	IC_RESERVED_PA("reserved.PA", ICPropertiesType.DOUBLE),
	IC_RESERVED_PB("reserved.PB", ICPropertiesType.DOUBLE),
	IC_RESERVED_AARR("reserved.AARR", ICPropertiesType.DOUBLE),
	IC_RESERVED_BARR("reserved.BARR", ICPropertiesType.DOUBLE),
	IC_RESERVED_ANUM("reserved.ANUM", ICPropertiesType.DOUBLE),
	IC_RESERVED_BNUM("reserved.BNUM", ICPropertiesType.DOUBLE),
	IC_RESERVED_ARAR("reserved.ARAR", ICPropertiesType.DOUBLE),
	
	IC_SPOT_SERIAL_PROB("spot.SERIAL.PROB", ICPropertiesType.DOUBLE),
	IC_SPOT_POW2_PROB("spot.POW2.PROB", ICPropertiesType.DOUBLE),
	IC_SPOT_ULOW("spot.ULOW", ICPropertiesType.DOUBLE),
	IC_SPOT_UMED("spot.UMED", ICPropertiesType.DOUBLE),
	IC_SPOT_UHI("spot.UHI", ICPropertiesType.DOUBLE),
	IC_SPOT_UPROB("spot.UPROB", ICPropertiesType.DOUBLE),
	IC_SPOT_A1("spot.A1", ICPropertiesType.DOUBLE),
	IC_SPOT_B1("spot.B1", ICPropertiesType.DOUBLE),
	IC_SPOT_A2("spot.A2", ICPropertiesType.DOUBLE),
	IC_SPOT_B2("spot.B2", ICPropertiesType.DOUBLE),
	IC_SPOT_PA("spot.PA", ICPropertiesType.DOUBLE),
	IC_SPOT_PB("spot.PB", ICPropertiesType.DOUBLE),
	IC_SPOT_AARR("spot.AARR", ICPropertiesType.DOUBLE),
	IC_SPOT_BARR("spot.BARR", ICPropertiesType.DOUBLE),
	IC_SPOT_ANUM("spot.ANUM", ICPropertiesType.DOUBLE),
	IC_SPOT_BNUM("spot.BNUM", ICPropertiesType.DOUBLE),
	IC_SPOT_ARAR("spot.ARAR", ICPropertiesType.DOUBLE),

	IC_CLOUD_NUMBER("cloud.number", ICPropertiesType.INTEGER),
	
	IC_CLOUD_ID("cloud.id", ICPropertiesType.INTEGER),
	IC_CLOUD_NODES("cloud.nodes", ICPropertiesType.INTEGER),
	IC_CLOUD_PES("cloud.pes", ICPropertiesType.INTEGER),
	
	IC_VOLATILITY_OBSERVING_INTERVAL("volatility.observing.interval", ICPropertiesType.INTEGER),
	IC_VOLATILITY_OBSERVATION_NUMBER("volatiltiy.observation.number", ICPropertiesType.INTEGER),
	IC_VOLATILITY_SIGMA("volatility.sigma", ICPropertiesType.DOUBLE),
	IC_BINOMIAL_RISKFREE_INTEREST_RATE("binomial.riskfree.interest.rate", ICPropertiesType.DOUBLE),
	IC_BINOMIAL_STEPS("binomial.steps", ICPropertiesType.INTEGER),
	IC_OPTION_TIME_TO_MATURATION("option.time.to.maturation", ICPropertiesType.INTEGER),
	
	IC_FEDERATION_OFFERED_RATIO_OF_FREESPACE("federation.offered.ratio.of.freespace", ICPropertiesType.DOUBLE),
	IC_INCREASE_FACTOR_SPOT_TOP("increase.factor.spot.top", ICPropertiesType.DOUBLE),
	
	IC_AUCTION_MECHANISM("auction.mechanism", ICPropertiesType.STRING),
	IC_AUCTION_BID_DISTRIBUTION("auction.bid.distribution", ICPropertiesType.STRING),
	IC_AUCTION_QTY_DISTRIBUTION("auction.qty.distribution", ICPropertiesType.STRING),
	IC_AUCTION_QTY_MAX("auction.qty.max", ICPropertiesType.INTEGER),
	IC_AUCTION_BID_MAX("auction.bid.max", ICPropertiesType.INTEGER),
	IC_AUCTION_ORDERS_NO("auction.orders.no", ICPropertiesType.INTEGER),
	
	IC_REVENUE_MANAGEMENT_MECHANISM("revenueManagement.management.mechanism", ICPropertiesType.STRING),
	IC_REVENUE_MANAGEMENT_WARMUP_TIME("revenueManagement.warmup.time", ICPropertiesType.INTEGER),
	IC_REVENUE_MANAGEMENT_COOLDOWN_TIME("revenueManagement.cooldown.time", ICPropertiesType.INTEGER),
	IC_REVENUE_MANAGEMENT_RESERVATION_LENGTH("revenueManagement.reservation.length", ICPropertiesType.INTEGER),
	IC_REVENUE_MANAGEMENT_VMBLOCK_SIZE("revenueManagement.vmblock.size", ICPropertiesType.INTEGER),
	IC_REVENUE_MANAGEMENT_TIMESLOT_SIZE("revenueManagement.timeslot.size", ICPropertiesType.INTEGER),
	IC_REVENUE_MANAGEMENT_RESERVED_PREMIUM("revenueManagement.reserved.premium", ICPropertiesType.INTEGER),
	IC_REVENUE_MANAGEMENT_SPOT_DISCOUNT_FACTOR("revenueManagement.spot.discount.factor", ICPropertiesType.DOUBLE),
	IC_REVENUE_MANAGEMENT_RESERVED_UTIL_INTERVAL_NO("revenueManagement.reserved.util.interval.no", ICPropertiesType.INTEGER)
*/	
//	IC_CLOUD_0_TIMEZONE("cloud.0.timezone", ICPropertiesType.INTEGER),
//	IC_CLOUD_1_ID("cloud.1.id", ICPropertiesType.INTEGER),
//	IC_CLOUD_1_NODES("cloud.1.nodes", ICPropertiesType.INTEGER),
//	IC_CLOUD_1_PES("cloud.1.pes", ICPropertiesType.INTEGER),
//	IC_CLOUD_1_TIMEZONE("cloud.1.timezone", ICPropertiesType.INTEGER),
//
//	
//	IC_CLOUD_2_ID("cloud.2.id", ICPropertiesType.INTEGER),
//	IC_CLOUD_2_NODES("cloud.2.nodes", ICPropertiesType.INTEGER),
//	IC_CLOUD_2_PES("cloud.2.pes", ICPropertiesType.INTEGER),
//	IC_CLOUD_2_TIMEZONE("cloud.2.timezone", ICPropertiesType.INTEGER),
//
//
//	IC_CLOUD_3_ID("cloud.3.id", ICPropertiesType.INTEGER),
//	IC_CLOUD_3_NODES("cloud.3.nodes", ICPropertiesType.INTEGER),
//	IC_CLOUD_3_PES("cloud.3.pes", ICPropertiesType.INTEGER),
//	IC_CLOUD_3_TIMEZONE("cloud.3.timezone", ICPropertiesType.INTEGER),
//
//	
//	IC_CLOUD_4_ID("cloud.4.id", ICPropertiesType.INTEGER),
//	IC_CLOUD_4_NODES("cloud.4.nodes", ICPropertiesType.INTEGER),
//	IC_CLOUD_4_PES("cloud.4.pes", ICPropertiesType.INTEGER),
//	IC_CLOUD_4_TIMEZONE("cloud.4.timezone", ICPropertiesType.INTEGER),
//
//	IC_CLOUD_5_ID("cloud.5.id", ICPropertiesType.INTEGER),
//	IC_CLOUD_5_NODES("cloud.5.nodes", ICPropertiesType.INTEGER),
//	IC_CLOUD_5_PES("cloud.5.pes", ICPropertiesType.INTEGER),
//	IC_CLOUD_5_TIMEZONE("cloud.5.timezone", ICPropertiesType.INTEGER),
//
//	IC_CLOUD_6_ID("cloud.6.id", ICPropertiesType.INTEGER),
//	IC_CLOUD_6_NODES("cloud.6.nodes", ICPropertiesType.INTEGER),
//	IC_CLOUD_6_PES("cloud.6.pes", ICPropertiesType.INTEGER),
//	IC_CLOUD_6_TIMEZONE("cloud.6.timezone", ICPropertiesType.INTEGER)
	;
	
	private String key;
	private ICPropertiesType type;

	/**
	 * @param str
	 *            string representing the property's key.
	 * @param type
	 *            type of the property.
	 */
	ICProperties(String str, ICPropertiesType type) {
		this.key = str;
		this.type = type;
	}

	/**
	 * Returns the key associated to this property
	 * 
	 * @return the key associated to this property
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @return the type of this property.
	 */
	public ICPropertiesType getType() {
		return this.type;
	}

	/**
	 * Returns the value of this property.
	 * 
	 * @return the value of this property.
	 */
	public String getValue() {
		return InterCloudSimConfiguration.getInstance().getProperty(this.key);
	}

	/**
	 * @return this property's value as a int.
	 */
	public int getValueAsInt() {
		if (type != ICPropertiesType.INTEGER) {
			//Log.logger.warning(this.key + " is not an integer property. getValueAsInt cannot be called on this property");
             Log.print(this.key+ " is not an integer property. getValueAsInt cannot be called on this property");
			System.exit(2);
		}

		return Integer.parseInt(this.getValue());
	}

	/**
	 * @return this property's value as a long.
	 * 
	 * @exception IllegalArgumentException
	 *                if the value is not a long.
	 */
	public long getValueAsLong() {
		if (type != ICPropertiesType.LONG) {
			//Log.logger.warning(this.key	+ " is not a long property. getValueAslong cannot be called on this property");
			Log.print(this.key+ " is not a long property. getValueAslong cannot be called on this property");
			System.exit(2);
		}

		return Long.parseLong(this.getValue());
	}

	/**
	 * @return this property's value as a double.
	 * 
	 * @exception IllegalArgumentException
	 *                if the value is not a double.
	 */
	public double getValueAsDouble() {
		if (type != ICPropertiesType.DOUBLE) {
			//Log.logger.warning(this.key	+ " is not double property. getValueAsDouble cannot be called on this property");
			Log.print(this.key+" is not double property. getValueAsDouble cannot be called on this property");
			System.exit(2);
		}
		return Double.parseDouble(this.getValue());
	}

	/**
	 * @return this property's value as a boolean.
	 * 
	 * @exception IllegalArgumentException
	 *                if the value is not a boolean.
	 */
	public boolean getValueAsBoolean() {
		if (this.type != ICPropertiesType.BOOLEAN) {
			//Log.logger.warning(this.key	+ " is not a boolean. getValueAsBoolean cannot be called on this property");
			Log.print(this.key+ " is not a boolean. getValueAsBoolean cannot be called on this property");
			System.exit(2);
		}
		return Boolean.parseBoolean(this.getValue());
	}

	/**
	 * Set the value of this property.
	 * 
	 * @param value
	 *            new value of the property.
	 */
	public void setValue(String value) {
		InterCloudSimConfiguration.getInstance().setProperty(this.key, value);
	}

	/**
	 * Set the value of this property.
	 * 
	 * @param i
	 *            new value.
	 */
	public void setValue(int i) {
		InterCloudSimConfiguration.getInstance().setProperty(this.key,	Integer.toString(i));
	}

	/**
	 * Set the value of this property.
	 * 
	 * @param bool
	 *            new value.
	 */
	public void setValue(boolean bool) {
		InterCloudSimConfiguration.getInstance().setProperty(this.key,	Boolean.toString(bool));
	}

	/**
	 * Set the value of this property.
	 * 
	 * @param dbl
	 *            new value.
	 */
	public void setValue(double dbl) {
		InterCloudSimConfiguration.getInstance().setProperty(this.key, Double.toString(dbl));
	}

	@Override
	public String toString() {
		return this.key + "=" + this.getValue();
	}

	/**
	 * @return true is this property is set.
	 */
	public boolean isSet() {
		return InterCloudSimConfiguration.getInstance().getProperty(key) != null;
	}

	public enum ICPropertiesType {
		STRING, INTEGER, BOOLEAN, DOUBLE, LONG;
	}

}
