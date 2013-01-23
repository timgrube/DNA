package dynamicGraphs.util;

import java.util.Arrays;

public class Stats {
	public Stats() {
		this(null);
	}

	public Stats(String name) {
		this.name = name;
		this.memory = null;
		this.timer = new Timer();
	}

	public String toString() {
		return this.timer + "  " + this.memory;
	}

	private String name;

	private Memory memory;

	private Timer timer;

	public String getName() {
		return name;
	}

	public Memory getMemory() {
		return memory;
	}

	public Timer getTimer() {
		return timer;
	}

	public void restart() {
		this.timer.restart();
	}

	public void end() {
		this.timer.end();
		this.memory = new Memory();
		if (this.name == null) {
			System.out.println(this.toString());
		}
	}

	public static String avg(Stats[] stats) {
		long runtime = 0;
		long memory = 0;
		for (Stats s : stats) {
			runtime += s.getTimer().getDutation();
			memory += s.getMemory().getUsed();
		}
		long runtimeTotal = runtime;
		runtime /= stats.length;
		memory /= stats.length;
		return runtime + " msec  " + memory + " Mb (" + runtimeTotal + " msec)";
	}

	public static double avgRuntime(Stats[] stats) {
		return Stats.totalRuntime(stats) / stats.length;
	}

	public static double totalRuntime(Stats[] stats) {
		long runtime = 0;
		for (Stats s : stats) {
			runtime += s.getTimer().getDutation();
		}
		return (double) runtime;
	}
}
