package util;

public class MemoryInformation {

	public static String getStatistics() {
		Runtime rt = Runtime.getRuntime();
		rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();
		rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();
		long totalMem = rt.totalMemory();
		long freeMem  = rt.freeMemory();
		String str = String.format("total:%-7s  used:%-7s  free:%-7s",
				getMegaBytes(totalMem), getMegaBytes(totalMem - freeMem), getMegaBytes(freeMem));
		return str;
	}

	public static String getUsedMemory() {
		Runtime rt = Runtime.getRuntime();
		rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();
		rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();rt.gc();
		long totalMem = rt.totalMemory();
		long freeMem  = rt.freeMemory();
		String str = String.format("%-7s", getMegaBytes(totalMem - freeMem));
		return str;
	}

	private static String getMegaBytes(long value) {
		return Round.getRoundedValue((float) (value / (1024*1024.0)), 1) + "MB";
	}

}
