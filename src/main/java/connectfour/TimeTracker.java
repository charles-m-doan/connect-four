package connectfour;

public class TimeTracker
	{
	//Metric Prefix Powers (Array corresponds to the prefixSymbols array)
	public static final int EXA = 18;
	public static final int PETA = 15;
	public static final int TERA = 12;
	public static final int GIGA = 9;
	public static final int MEGA = 6;
	public static final int HECTOKILO = 5;
	public static final int MYRIA = 4;
	public static final int KILO = 3;
	public static final int HECTO = 2;
	public static final int DEKA = 1;
	public static final int DECI = -1;
	public static final int CENTI = -2;
	public static final int MILLI = -3;
	public static final int DECIMILLI = -4;
	public static final int CENTIMILLI = -5;
	public static final int MICRO = -6;
	public static final int NANO = -9;
	public static final int PICO = -12;
	public static final int FEMTO = -15;
	public static final int ATTO = -18;
	public static final int[] metricPrefixPowers = { EXA, PETA, TERA, GIGA, MEGA, HECTOKILO, MYRIA, KILO, HECTO, DEKA, DECI, CENTI, MILLI, DECIMILLI, CENTIMILLI, MICRO, NANO, PICO, FEMTO, ATTO };
	//Metric Prefix Symbols (Case Sensitive)
	public static final String[] metricPrefixSymbols = { "E", "P", "T", "G", "M", "hk", "ma", "k", "h", "da", "d", "c", "m", "dm", "cm", "u", "n", "p", "f", "a" };

	//Time Tracking - All time fields are in nanoseconds.
	public final long timeLimit;
	long startTime = 0L;

	//"timeLimit" should be entered in milliseconds.
	public TimeTracker(long timeLimit)
		{
		this.timeLimit = convertMetric(timeLimit, MILLI, NANO);
		startTime = System.nanoTime();
		}

	public void begin()
		{
		startTime = System.nanoTime();
		}

	public boolean isTimeUp()
		{
		return (getTimeElapsed() >= timeLimit);
		}

	public long getTimeLimit(int power)
		{
		return convertMetric(timeLimit, NANO, power);
		}

	public long getTimeElapsed()
		{
		long timeElapsed = (System.nanoTime() - startTime);
		return timeElapsed;
		}

	public long getTimeElapsed(String unit)
		{
		long timeElapsed = (System.nanoTime() - startTime);
		return convertMetric(timeElapsed, NANO, symbolToPower(unit));
		}

	public long getTimeElapsed(int power)
		{
		long timeElapsed = (System.nanoTime() - startTime);
		return convertMetric(timeElapsed, NANO, power);
		}

	public static long convertMetric(long value, int fromUnitPower, int toUnitPower)
		{
		return (long) convertMetric((double) value, fromUnitPower, toUnitPower);
		}

	public static double convertMetric(double value, int fromUnitPower, int toUnitPower)
		{
		double conversionFactor = Math.pow(10, (fromUnitPower - toUnitPower));
		return (value * conversionFactor);
		}

	public static int symbolToPower(String symbol)
		{
		for (int i = 0; i < metricPrefixSymbols.length; i++)
			{
			if (symbol == metricPrefixSymbols[i])
				{
				return metricPrefixPowers[i];
				}
			}
		return 0;
		}

	public static String powerToSymbol(int power)
		{
		for (int i = 0; i < metricPrefixPowers.length; i++)
			{
			if (power == metricPrefixPowers[i])
				{
				return metricPrefixSymbols[i];
				}
			}
		return "";
		}

	}
