package info.fshi.oppnetdemo1.utils;

public abstract class Constants {

	public static long SCAN_INTERVAL = 15000;
	public static long SCAN_DURATION = 10000;
	public static long DATA_RATE = 60000;

	public static final long SCAN_DURATION_SAVING = 5000;
	public static final long SCAN_DURATION_NORMAL = 10000;
	public static final long SCAN_INTERVAL_NORMAL = 15000;
	public static final long SCAN_INTERVAL_SAVING = 300000;
	
	public static double ENERGY_PENALTY_COEFF = 0.1;
	public static final double ENERGY_PENALTY_COEFF_ON = 0.1;

	public static String TAG_ACT_TEST = "test";
	
	public static final String DEFAULT_DEVICE_NAME = "OppNet:0:0";
	public static final int QUEUE_DIFF = 5;
	
	public static long BT_CLIENT_TIMEOUT = 5000;
	public static long BT_CLIENT_TIMEOUT_SAVING = 3000;

	public static long SENSOR_CONTACT_INTERVAL = 60000;
	public static long SINK_CONTACT_INTERVAL = 240000;

	public static String SP_KEY_BATTERY = "sp_battery_saving_mode";
	public static String SP_KEY_ENERGY = "sp_energy_awareness_mode";
	
	public static String INTENT_KEY_POSITION = "intent_key_position";
	
	// configure one's experiment id and application id
	public static String EXPERIMENT_ID = null;
	public static String APPLICATION_ID = null;
	public static String USER_ID = null;
}
