package projects.CTAP.attractiveness;

public abstract class AttractivenessAbstract implements AttractivenessI {
	
	
	private final Double initialTime;
	private final Double finalTime;
	private final Double ordinaryFrequency;
	
	
	public AttractivenessAbstract(Double initialTime,Double finalTime) {
		this.initialTime = initialTime;
		this.finalTime = finalTime;
		this.ordinaryFrequency = 1/(finalTime-initialTime);
	}
	
	
	protected double seasonalitySineFunction(Double amplitude, Double ordinaryFrequency, Double phase, Double time) {
		return amplitude*0.5*(1 + Math.sin(2*Math.PI*ordinaryFrequency*(time-this.initialTime)+phase));
	}
	
	protected double seasonalitySummerSinglePeakSineFunction(Double amplitude, Double time) {
		
		return seasonalitySineFunction(amplitude,this.ordinaryFrequency,1.5*Math.PI,time);
	}
	
	protected double seasonalityWinterSinglePeakSineFunction(Double amplitude, Double time) {
		return seasonalitySineFunction(amplitude,this.ordinaryFrequency,Math.PI/2,time);
	}
	
	protected double seasonalityDoublePeakSummerWinterSineFunction(Double amplitude, Double time) {
		return seasonalitySineFunction(amplitude,this.ordinaryFrequency/2,Math.PI/2,time);
	}
	
	protected double seasonalityDoublePeakSpringFallSineFunction(Double amplitude, Double time) {
		return seasonalitySineFunction(amplitude,this.ordinaryFrequency/2,1.5*Math.PI,time);
	}
	
	//TODO
	protected double seasonalitySquareFunction(Double amplitude, Integer ordinaryFrequency, Double phase ) {
		return 0;
	}
	//TODO
	protected double seasonalityTriangularFunction(Double amplitude, Integer ordinaryFrequency, Double phase ) {
		return 0;
	}
	//TODO
	protected double seasonalitySawtoothFunction(Double amplitude, Integer ordinaryFrequency, Double phase ) {
		return 0;
	}

}
