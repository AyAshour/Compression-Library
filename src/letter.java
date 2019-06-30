
public class letter {

	private Character ch;
	private Double lower;
	private Double upper;
	letter(){}
	letter(Character c, Double l, Double u)
	{
		ch=c;
		lower=l;
		upper=u;
	}
	public Double getUpper() {
		return upper;
	}
	public void setUpper(Double upper) {
		this.upper = upper;
	}
	public Double getLower() {
		return lower;
	}
	public void setLower(Double lower) {
		this.lower = lower;
	}
	public Character getCh() {
		return ch;
	}
	public void setCh(Character ch) {
		this.ch = ch;
	}
	
}
